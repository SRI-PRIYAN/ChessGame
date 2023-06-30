package chess.coin;

import chess.board.Board;
import chess.board.ChessBoard;
import chess.board.Tile;
import chess.util.Move;
import chess.util.Position;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends ChessCoin {
    public Pawn(Alliance alliance) {
        super(alliance);
        String imageName = (alliance == Alliance.WHITE) ? "wPawn.png" : "bPawn.png";
        setIcon(ChessCoinUtil.getCoinImageIcon(imageName));
    }

    @Override
    public List<Move> getPossibleMoves(Board board, Position from) {
        List<Move> moves = new ArrayList<>();

        int row = from.getRow(), col = from.getCol();

        int factor = (alliance == board.getPlayerAlliance()) ? -1 : 1;

        if (!ChessBoard.isValid(row + factor)) return moves;

        if (board.hasNoCoinAt(row + factor, col)) {
            moves.add(new Move(from, new Position(row + factor, col)));
            if (hasNotMoved()
                    && ChessBoard.isValid(row + 2 * factor)
                    && board.hasNoCoinAt(row + 2 * factor, col)) {
                moves.add(new Move(from, new Position(row + 2 * factor, col)));
            }
        }

        if (ChessBoard.isValid(col - 1) && canCapture(board.getCoinAt(row + factor, col - 1))) {
            moves.add(new Move(from, new Position(row + factor, col - 1)));
        }

        if (ChessBoard.isValid(col + 1) && canCapture(board.getCoinAt(row + factor, col + 1))) {
            moves.add(new Move(from, new Position(row + factor, col + 1)));
        }

        return moves;
    }

    @Override
    public List<Move> getLegalMoves(Board board, Position from) {
        List<Move> legalMoves = super.getLegalMoves(board, from);

        if (!(board instanceof ChessBoard chessBoard)) return legalMoves;

        int row = from.getRow(), col = from.getCol();
        int factor = (alliance == board.getPlayerAlliance()) ? -1 : 1;

        Tile enPassantCandidateTile = chessBoard.getEnPassantCandidateTile();

        if (ChessBoard.isValid(col - 1) && enPassantCandidateTile == chessBoard.getTileAt(row, col - 1)) {
            legalMoves.add(new Move(from, new Position(row + factor, col - 1)));
        }

        if (ChessBoard.isValid(col + 1) && enPassantCandidateTile == chessBoard.getTileAt(row, col + 1)) {
            legalMoves.add(new Move(from, new Position(row + factor, col + 1)));
        }

        return legalMoves;
    }

    public static boolean isPromotion(ChessCoin selectedCoin, Tile targetTile) {
        return selectedCoin instanceof Pawn && targetTile.getRow() == 0;
    }

    public static boolean isCandidateForEnPassant(ChessCoin selectedCoin, Tile selectedTile, Tile targetTile) {
        if (!(selectedCoin instanceof Pawn)) return false;
        if (selectedCoin.getNumberOfMovesMade() != 1) return false;

        return Math.abs(selectedTile.getRow() - targetTile.getRow()) == 2;
    }

    public static boolean isEnPassant(ChessCoin selectedCoin, Tile selectedTile, Tile targetTile) {
        if (!(selectedCoin instanceof Pawn)) return false;

        int selectedCol = selectedTile.getCol();
        int targetCol = targetTile.getCol();

        // Checks if the move is not along the diagonal
        if (selectedCol - targetCol == 0) return false;

        return !targetTile.hasCoin();
    }
}
