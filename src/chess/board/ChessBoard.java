package chess.board;

import chess.analyzer.CheckmateDetector;
import chess.coin.Alliance;
import chess.coin.ChessCoin;
import chess.coin.ChessCoinFactory;
import chess.coin.King;
import chess.coin.Pawn;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ChessBoard extends JPanel implements Board {
    private static final int rows = 8;
    private static final int cols = 8;
    private final Tile[][] tiles = new Tile[rows][cols];
    private Tile selectedTile = null;
    private Tile enPassantCandidateTile = null;
    private final Alliance playerAlliance;
    private final Alliance opponentAlliance;
    private boolean isTurn;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private boolean isGameOver;

    public ChessBoard(int side, Alliance playerAlliance, DataInputStream dis, DataOutputStream dos) {
        this.playerAlliance = playerAlliance;
        this.opponentAlliance = getOppositeAlliance(playerAlliance);
        this.isTurn = playerAlliance == Alliance.WHITE;
        this.dis = dis;
        this.dos = dos;
        this.isGameOver = false;

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
                Color tileColor = new Color(238, 238, 213); // Light Color
                if ((row + col) % 2 == 1) tileColor = new Color(124, 149, 93); // Dark Color
                tiles[row][col] = new Tile(this, row, col, tileSide, tileColor);
                add(tiles[row][col]);
            }
        }
    }

    private void setupCoins(Alliance playerAlliance) {
        String[] coins = {"Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook"};

        for (int col = 0; col < cols; col++) {
            // Coins will be in the reverse order for Black
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

        if (Pawn.isEnPassant(selectedCoin, selectedTile, targetTile)) {
            enPassantCandidateTile.removeCoin();
            enPassantCandidateTile = null;
        }

        if (King.isCastling(selectedCoin, selectedTile, targetTile)) {
            moveRookForCastling(selectedTile.getRow(), selectedTile.getCol(), targetTile.getCol());
        }

        targetTile.setCoin(selectedCoin);
        selectedTile.removeCoin();
        selectedCoin.incrementNumberOfMovesMade();

        if (isTurn) {
            if (Pawn.isPromotion(selectedCoin, targetTile)) {
                // Also sends the move to the Server
                promotePawn(selectedCoin, selectedTile, targetTile);
            } else {
                sendMoveToServer(selectedTile, targetTile, null);
            }
            detectAndHandleCheck(opponentAlliance);
        }

        if (Pawn.isCandidateForEnPassant(selectedCoin, selectedTile, targetTile)) {
            enPassantCandidateTile = targetTile;
        } else {
            enPassantCandidateTile = null;
        }

        isTurn = !isTurn;
        selectedTile = null;
        if (!isGameOver && !isTurn) new Thread(this::getAndPerformOpponentMove).start();
    }

    private void moveRookForCastling(int row, int kingCol, int kingTargetCol) {
        // from and to Tiles of Rook
        Tile from = (kingTargetCol > kingCol) ? tiles[row][7] : tiles[row][0];
        Tile to = (kingTargetCol > kingCol) ? tiles[row][kingTargetCol - 1] : tiles[row][kingTargetCol + 1];

        ChessCoin rook = from.getCoin();
        rook.incrementNumberOfMovesMade();
        to.setCoin(rook);
        from.removeCoin();
    }

    // Also sends the move to the server with the chess.coin type to which the pawn is being promoted
    private void promotePawn(ChessCoin pawn, Tile selectedTile, Tile targetTile) {
        this.setEnabled(false);
        this.setFocusable(false);

        Alliance pawnAlliance = pawn.getAlliance();

        JFrame promotionFrame = new JFrame("Choose A Coin");
        promotionFrame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        promotionFrame.setSize(new Dimension(500, 130));

        String[] options = { "Queen", "Rook", "Bishop", "Knight" };
        for (String option : options) {
            JButton button = new JButton();
            button.add(ChessCoinFactory.create(option, pawnAlliance));
            button.addActionListener(e -> {
                targetTile.setCoin(ChessCoinFactory.create(option, pawnAlliance));
                promotionFrame.dispose();
                ChessBoard.this.setEnabled(true);
                ChessBoard.this.setFocusable(true);
                sendMoveToServer(selectedTile, targetTile, option);
            });

            promotionFrame.add(button);
        }

        promotionFrame.setLocationRelativeTo(this);
        promotionFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        promotionFrame.setVisible(true);
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

    private void getAndPerformOpponentMove() {
        try {
            String command = dis.readUTF();
            String promotionCoinType = command.equals("promote") ? dis.readUTF() : null;

            int row1 = dis.readInt();
            int col1 = dis.readInt();
            int row2 = dis.readInt();
            int col2 = dis.readInt();

            selectedTile = tiles[row1][col1];
            moveCoin(tiles[row2][col2]);
            if (promotionCoinType != null) {
                tiles[row2][col2].setCoin(ChessCoinFactory.create(promotionCoinType, opponentAlliance));
            }

            detectAndHandleCheck(playerAlliance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void detectAndHandleCheck(Alliance alliance) {
        int result = CheckmateDetector.analyze(this, alliance);

        if (result == CheckmateDetector.SAFE || result == CheckmateDetector.CHECK) return;

        String message = "Match Drawn by Stalemate";
        if (result == CheckmateDetector.CHECKMATE) message = getOppositeAlliance(alliance) + " wins by Checkmate";

        JOptionPane.showMessageDialog(this, message);

        closeConnectionWithServer();
        isGameOver = true;
    }

    private void closeConnectionWithServer() {
        try {
            dos.writeUTF("end");
            dis.close();
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ChessCoin getCoinAt(int row, int col) {
        return tiles[row][col].getCoin();
    }

    @Override
    public Tile getTileAt(int row, int col) {
        return tiles[row][col];
    }

    @Override
    public Alliance getPlayerAlliance() {
        return playerAlliance;
    }

    private Alliance getOppositeAlliance(Alliance alliance) {
        return alliance == Alliance.WHITE ? Alliance.BLACK : Alliance.WHITE;
    }

    public Tile getEnPassantCandidateTile() {
        return enPassantCandidateTile;
    }

    public void setSelectedTile(Tile selectedTile) {
        this.selectedTile = selectedTile;
    }

    public boolean isHighlightedTile(int row, int col) {
        return tiles[row][col].isHighlighted();
    }

    public void highlightTile(int row, int col) {
        tiles[row][col].highlight();
    }

    public boolean isTurn() {
        return isTurn;
    }

    public void resetTile(int row, int col) {
        tiles[row][col].reset();
    }

    public static boolean isValid(int rowOrCol) {
        return rowOrCol >= 0 && rowOrCol < 8;
    }

    public static boolean isValid(int row, int col) {
        return isValid(row) && isValid(col);
    }
}