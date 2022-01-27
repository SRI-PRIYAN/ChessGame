// ChessBoard after the given move
public class AnticipatedChessBoard implements Board {
    private final Board board;
    private final Move move;

    public AnticipatedChessBoard(Board board, Move move) {
        this.board = board;
        this.move = move;
    }

    @Override
    public ChessCoin getCoinAt(int row, int col) {
        if (isFrom(row, col)) return null;
        if (isTo(row, col)) return board.getCoinAt(move.from.getRow(), move.from.getCol());

        return board.getCoinAt(row, col);
    }

    @Override
    public Alliance getPlayerAlliance() {
        return board.getPlayerAlliance();
    }

    public boolean isFrom(int row, int col) {
        return move.from.getRow() == row && move.from.getCol() == col;
    }

    public boolean isTo(int row, int col) {
        return move.to.getRow() == row && move.to.getCol() == col;
    }
}
