import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

public class ChessBoard extends JPanel {
    private static final int rows = 8;
    private static final int cols = 8;
    private final Tile[][] tiles = new Tile[rows][cols];
    private Tile selectedTile = null;

    public ChessBoard(int side) {
        setPreferredSize(new Dimension(side, side));
        setLayout(new GridLayout(8, 8));
        setBorder(BorderFactory.createLineBorder(Color.black));

        setupTiles(side / 8);
        setupCoins();
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

    private void setupCoins() {
        String[] coins = {"Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook"};

        for (int col = 0; col < cols; col++) {
            // White Coins
            tiles[6][col].setCoin(ChessCoinFactory.create("Pawn", Alliance.WHITE));
            tiles[7][col].setCoin(ChessCoinFactory.create(coins[col], Alliance.WHITE));

            // Black Coins
            tiles[1][col].setCoin(ChessCoinFactory.create("Pawn", Alliance.BLACK));
            tiles[0][col].setCoin(ChessCoinFactory.create(coins[col], Alliance.BLACK));
        }
    }

    public void captureCoin(Tile targetTile) {
        moveCoin(targetTile);
    }

    public void moveCoin(Tile targetTile) {
        if (selectedTile == null) return;

        ChessCoin selectedCoin = selectedTile.getCoin();
        selectedTile.getCoin().incrementNumberOfMovesMade();
        targetTile.setCoin(selectedTile.getCoin());
        selectedTile.removeCoin();
        selectedTile = null;

        if (selectedCoin instanceof Pawn p && p.canPromote(targetTile.getRow())) {
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
                });

                promotionFrame.add(button);
            }

            promotionFrame.setLocationRelativeTo(null);
            promotionFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            promotionFrame.setVisible(true);
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chess Game");
        frame.setLayout(new GridBagLayout());
        frame.getContentPane().setBackground(Color.lightGray);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 720);
        frame.add(new ChessBoard(640));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}