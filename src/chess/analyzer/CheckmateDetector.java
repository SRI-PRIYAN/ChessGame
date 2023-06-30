package chess.analyzer;

import chess.board.Board;
import chess.coin.Alliance;
import chess.coin.ChessCoin;
import chess.coin.King;
import chess.util.Move;
import chess.util.Position;

import java.util.List;

public class CheckmateDetector {
    public static final int SAFE = 0;
    public static final int CHECK = 1;
    public static final int CHECKMATE = 2;
    public static final int STALEMATE = 3;

    // Returns true if the King with the given alliance is in check
    public static boolean isKingInCheck(Board board, Alliance kingAlliance) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessCoin coin = board.getCoinAt(row, col);
                if (coin == null || coin.getAlliance() == kingAlliance) continue;
                for (Move move : coin.getPossibleMoves(board, new Position(row, col))) {
                    if (isKing(board.getCoinAt(move.to.getRow(), move.to.getCol()), kingAlliance)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static int analyze(Board board, Alliance alliance) {
        boolean isKingInCheck = isKingInCheck(board, alliance);
        boolean hasNoLegalMoves = hasNoLegalMoves(board, alliance);

        if (isKingInCheck) {
            if (hasNoLegalMoves) return CHECKMATE;
            else return CHECK;
        } else {
            if (hasNoLegalMoves) return STALEMATE;
            else return SAFE;
        }
    }

    // Returns true if the given alliance has no legal moves to make
    private static boolean hasNoLegalMoves(Board board, Alliance alliance) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessCoin coin = board.getCoinAt(row, col);
                if (coin == null || coin.getAlliance() != alliance) continue;
                List<Move> moves = coin.getLegalMoves(board, new Position(row, col));
                if (!moves.isEmpty()) return false;
            }
        }

        return true;
    }

    private static boolean isKing(ChessCoin coin, Alliance kingAlliance) {
        return coin instanceof King && coin.getAlliance() == kingAlliance;
    }
}
