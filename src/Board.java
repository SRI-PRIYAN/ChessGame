public interface Board {
    ChessCoin getCoinAt(int row, int col);

    default boolean hasNoCoinAt(int row, int col) {
        return getCoinAt(row, col) == null;
    }

    Alliance getPlayerAlliance();
}
