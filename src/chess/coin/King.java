package chess.coin;

import chess.analyzer.CheckmateDetector;
import chess.board.AnticipatedChessBoard;
import chess.board.Board;
import chess.board.Tile;
import chess.util.Move;
import chess.util.Position;

import java.util.List;

public class King extends ChessCoin {
    public King(Alliance alliance) {
        super(alliance);
        String imageName = (alliance == Alliance.WHITE) ? "wKing.png" : "bKing.png";
        setIcon(ChessCoinUtil.getCoinImageIcon(imageName));
    }

    private boolean hasNoCoinBetween(Board board, int row, int col1, int col2) {
        int fromCol = Math.min(col1, col2);
        int toCol = Math.max(col1, col2);

        for (int col = fromCol + 1; col < toCol; col++) {
            if (!board.hasNoCoinAt(row, col)) return false;
        }
        return true;
    }

    private boolean canCastle(Board board, Position kingPosition, Position rookPosition) {
        ChessCoin coin = board.getCoinAt(rookPosition);
        if (coin == null || !(coin instanceof Rook) || coin.alliance != alliance) return false;

        if (hasMoved() || coin.hasMoved()) return false;

        int row = kingPosition.getRow();
        int kingCol = kingPosition.getCol();
        int rookCol = rookPosition.getCol();

        if (!hasNoCoinBetween(board, row, kingCol, rookCol)) return false;

        if (CheckmateDetector.isKingInCheck(board, alliance)) return false;

        int sign = (kingCol < rookCol) ? 1 : -1;

        for (int i = 1; i <= 2; i++) {
            int col = kingCol + (i * sign);
            Move move = new Move(kingPosition, new Position(row, col));
            AnticipatedChessBoard anticipatedChessBoard = new AnticipatedChessBoard(board, move);
            if (CheckmateDetector.isKingInCheck(anticipatedChessBoard, alliance))
                return false;
        }

        return true;
    }

    public static boolean isCastling(ChessCoin selectedCoin, Tile selectedTile, Tile targetTile) {
        if (!(selectedCoin instanceof King)) return false;

        int selectedRow = selectedTile.getRow();
        int selectedCol = selectedTile.getCol();

        int targetRow = targetTile.getRow();
        int targetCol = targetTile.getCol();

        if (selectedRow != targetRow) return false;
        return Math.abs(selectedCol - targetCol) == 2;
    }

    @Override
    public List<Move> getPossibleMoves(Board board, Position from) {
        int[][] relatives = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                {1, 1}, {-1, 1}, {1, -1}, {-1, -1}
        };

        return ChessCoinUtil.generateRelativeMoves(board, from, relatives);
    }

    @Override
    public List<Move> getLegalMoves(Board board, Position from) {
        List<Move> legalMoves = super.getLegalMoves(board, from);

        int kingRow = from.getRow();
        int kingCol = from.getCol();

        // Right Side Castling
        if (canCastle(board, from, new Position(kingRow, 7))) {
            legalMoves.add(new Move(from, new Position(kingRow, kingCol + 2)));
        }

        // Left Side Castling
        if (canCastle(board, from, new Position(kingRow, 0))) {
            legalMoves.add(new Move(from, new Position(kingRow, kingCol - 2)));
        }

        return legalMoves;
    }

}
