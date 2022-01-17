import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ChessBoard extends JPanel {
    private static final int rows = 8;
    private static final int cols = 8;
    private final Tile[][] tiles = new Tile[rows][cols];
    private Tile selectedTile = null;
    private final Alliance playerAlliance;
    private final Alliance opponentAlliance;
    private boolean isTurn;
    private final DataInputStream dis;
    private final DataOutputStream dos;

    public ChessBoard(int side, Alliance playerAlliance, DataInputStream dis, DataOutputStream dos) {
        this.playerAlliance = playerAlliance;
        this.opponentAlliance = (playerAlliance == Alliance.WHITE) ? Alliance.BLACK : Alliance.WHITE;
        this.isTurn = playerAlliance == Alliance.WHITE;
        this.dis = dis;
        this.dos = dos;

        setPreferredSize(new Dimension(side, side));
        setLayout(new GridLayout(8, 8));
        setBorder(BorderFactory.createLineBorder(Color.black));

        setupTiles(side / 8);
        setupCoins(playerAlliance);

        if (!isTurn) new Thread(this::getAndPerformOpponentMove).start();
    }

    private void setupTiles(int tileSide) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Color tileColor = new Color(227, 193, 111);
                if ((row + col) % 2 == 1) tileColor = new Color(184, 139, 74);
                tiles[row][col] = new Tile(this, row, col, tileSide, tileColor);
                add(tiles[row][col]);
            }
        }
    }

    private void setupCoins(Alliance playerAlliance) {
        String[] coins = {"Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook"};

        for (int col = 0; col < cols; col++) {
            int index = playerAlliance == Alliance.WHITE ? col : cols - col - 1;

            tiles[6][col].setCoin(ChessCoinFactory.create("Pawn", playerAlliance));
            tiles[7][col].setCoin(ChessCoinFactory.create(coins[index], playerAlliance));

            tiles[1][col].setCoin(ChessCoinFactory.create("Pawn", opponentAlliance));
            tiles[0][col].setCoin(ChessCoinFactory.create(coins[index], opponentAlliance));
        }
    }

    public void moveCoin(Tile targetTile) {
        if (selectedTile == null) return;

        ChessCoin selectedCoin = selectedTile.getCoin();
        selectedTile.getCoin().incrementNumberOfMovesMade();
        targetTile.setCoin(selectedTile.getCoin());
        selectedTile.removeCoin();

        if (isTurn) {
            if (selectedCoin instanceof Pawn p && p.canPromote(targetTile.getRow())) {
                promotePawn(p, selectedTile, targetTile);
            }
            else sendMoveToServer(selectedTile, targetTile, null);
        }

        isTurn = !isTurn;
        if (!isTurn) new Thread(this::getAndPerformOpponentMove).start();
        selectedTile = null;
    }

//    Also sends the move to the server with the coin type to which the pawn is promoted
    private void promotePawn(Pawn p, Tile selectedTile, Tile targetTile) {
        this.setVisible(false);

        JFrame promotionFrame = new JFrame("Choose A Coin");
        promotionFrame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        promotionFrame.setSize(new Dimension(500, 120));

        String[] options = { "Queen", "Rook", "Bishop", "Knight" };
        for (String option : options) {
            JButton button = new JButton();
            button.add(ChessCoinFactory.create(option, p.alliance));
            button.addActionListener(e -> {
                targetTile.setCoin(ChessCoinFactory.create(option, p.alliance));
                promotionFrame.dispose();
                ChessBoard.this.setVisible(true);
                sendMoveToServer(selectedTile, targetTile, option);
            });

            promotionFrame.add(button);
        }

        promotionFrame.setLocationRelativeTo(null);
        promotionFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        promotionFrame.setVisible(true);
    }

    private void getAndPerformOpponentMove() {
        try {
            String command = dis.readUTF();
            String coinType = command.equals("promote") ? dis.readUTF() : null;

            int row1 = dis.readInt();
            int col1 = dis.readInt();
            int row2 = dis.readInt();
            int col2 = dis.readInt();

            selectedTile = tiles[row1][col1];
            moveCoin(tiles[row2][col2]);
            if (coinType != null) {
                tiles[row2][col2].setCoin(ChessCoinFactory.create(coinType, opponentAlliance));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMoveToServer(Tile selectedTile, Tile targetTile, String promotionCoinType) {
        try {
            if (promotionCoinType != null) {
                dos.writeUTF("promote");
                dos.writeUTF(promotionCoinType);
            }
            else dos.writeUTF("move");

            dos.writeInt(selectedTile.getRow());
            dos.writeInt(selectedTile.getCol());
            dos.writeInt(targetTile.getRow());
            dos.writeInt(targetTile.getCol());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSelectedTile(Tile selectedTile) {
        this.selectedTile = selectedTile;
    }

    public ChessCoin getCoinAt(int row, int col) {
        return tiles[row][col].getCoin();
    }

    public boolean isEmptyTile(int row, int col) {
        return !tiles[row][col].hasCoin();
    }

    public boolean isHighlightedTile(int row, int col) {
        return tiles[row][col].getIsHighlighted();
    }

    public void highlightTile(int row, int col) {
        tiles[row][col].highlight(Color.cyan);
    }

    public void resetTile(int row, int col) {
        tiles[row][col].reset();
    }

    public Alliance getPlayerAlliance() {
        return playerAlliance;
    }

    public boolean isTurn() {
        return isTurn;
    }
}