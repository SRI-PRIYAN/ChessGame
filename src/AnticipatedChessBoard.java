// ChessBoard after the given move
public class AnticipatedChessBoard implements ChessCoinContainer {
    private final ChessCoinContainer container;
    private Move move;

    public AnticipatedChessBoard(ChessCoinContainer container, Move move) {
        this.container = container;
        this.move = move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    @Override
    public ChessCoin getCoinAt(int row, int col) {
        if (isFrom(row, col)) return null;
        if (isTo(row, col)) return container.getCoinAt(move.from.getRow(), move.from.getCol());

        return container.getCoinAt(row, col);
    }

    @Override
    public Alliance getPlayerAlliance() {
        return container.getPlayerAlliance();
    }

    public boolean isFrom(int row, int col) {
        return move.from.getRow() == row && move.from.getCol() == col;
    }

    public boolean isTo(int row, int col) {
        return move.to.getRow() == row && move.to.getCol() == col;
    }
}
