package chess.coin;

import chess.board.Board;
import chess.board.ChessBoard;
import chess.util.Move;
import chess.util.Position;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

class ChessCoinUtil {
    static ImageIcon getCoinImageIcon(String imageName) {
        InputStream inputStream = ChessCoinUtil.class.getResourceAsStream("/resources/" + imageName);
        if (inputStream != null) {
            try {
                Image coinImage = new ImageIcon(inputStream.readAllBytes()).getImage();
                return new ImageIcon(coinImage.getScaledInstance(75, 75, Image.SCALE_SMOOTH));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    static List<Move> generateRelativeMoves(Board board, Position from, int[][] relatives) {
        List<Move> moves = new ArrayList<>();

        ChessCoin coin = board.getCoinAt(from);
        assert coin != null;

        int row = from.getRow(), col = from.getCol();

        for (int[] relative : relatives) {
            int dr = relative[0], dc = relative[1];
            int nRow = row + dr, nCol = col + dc;

            if (!ChessBoard.isValid(nRow, nCol)) continue;

            if (board.hasNoCoinAt(nRow, nCol) || coin.canCapture(board.getCoinAt(nRow, nCol))) {
                moves.add(new Move(from, new Position(nRow, nCol)));
            }
        }

        return moves;
    }

    static void addAxialMoves(Board board, Position from, List<Move> moves) {
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1} // Top, Bottom, Left, Right
        };

        addDirectionalMoves(board, from, directions, moves);
    }

    static void addDiagonalMoves(Board board, Position from, List<Move> moves) {
        int[][] directions = {
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // T-L, T-R, B-L, B-R
        };

        addDirectionalMoves(board, from, directions, moves);
    }

    private static void addDirectionalMoves(Board board, Position from, int[][] directions, List<Move> moves) {
        ChessCoin coin = board.getCoinAt(from);
        assert coin != null;

        int row = from.getRow(), col = from.getCol();
        for (int[] direction : directions) {
            int dr = direction[0], dc = direction[1];
            for (int r = row + dr, c = col + dc; ChessBoard.isValid(r, c); r += dr, c += dc) {
                boolean hasNoCoinAtRC = board.hasNoCoinAt(r, c);

                if (hasNoCoinAtRC || coin.canCapture(board.getCoinAt(r, c))) {
                    moves.add(new Move(from, new Position(r, c)));
                }

                if (!hasNoCoinAtRC) break;
            }
        }
    }
}
