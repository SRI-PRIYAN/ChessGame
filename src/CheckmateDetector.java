import java.util.List;

public class CheckmateDetector {
    public static final int SAFE = 0;
    public static final int CHECK = 1;
    public static final int CHECKMATE = 2;
    public static final int STALEMATE = 3;

    // Returns true if the King with the given alliance is in check
    public static boolean isKingInCheck(ChessCoinContainer container, Alliance kingAlliance) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessCoin coin = container.getCoinAt(row, col);
                if (coin == null || coin.alliance == kingAlliance) continue;
                for (Move move : coin.getPossibleMoves(container, new Position(row, col))) {
                    if (isKing(container.getCoinAt(move.to.getRow(), move.to.getCol()), kingAlliance)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static int analyze(ChessCoinContainer container, Alliance alliance) {
        boolean isKingInCheck = isKingInCheck(container, alliance);
        boolean hasNoLegalMoves = hasNoLegalMoves(container, alliance);

        if (isKingInCheck) {
            if (hasNoLegalMoves) return CHECKMATE;
            else return CHECK;
        } else {
            if (hasNoLegalMoves) return STALEMATE;
            else return SAFE;
        }
    }

    // Returns true if the given alliance has no legal moves to make
    private static boolean hasNoLegalMoves(ChessCoinContainer container, Alliance alliance) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessCoin coin = container.getCoinAt(row, col);
                if (coin == null || coin.alliance != alliance) continue;
                List<Move> moves = coin.getLegalMoves(container, new Position(row, col));
                if (!moves.isEmpty()) return false;
            }
        }

        return true;
    }

    private static boolean isKing(ChessCoin coin, Alliance kingAlliance) {
        return coin instanceof King && coin.alliance == kingAlliance;
    }
}
