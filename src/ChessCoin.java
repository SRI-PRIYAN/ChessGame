import javax.swing.JLabel;
import java.util.ArrayList;
import java.util.List;

public abstract class ChessCoin extends JLabel {
    protected final Alliance alliance;
    private int numberOfMovesMade;

    public ChessCoin(Alliance alliance) {
        this.alliance = alliance;
        this.numberOfMovesMade = 0;
    }

    public boolean hasNotMoved() {
        return numberOfMovesMade == 0;
    }

    public void incrementNumberOfMovesMade() {
        numberOfMovesMade++;
    }

    public void addTopMoves(ChessBoard board, int row, int col, List<Move> moves) {
        //noinspection StatementWithEmptyBody
        while (canExpandMove(board, ++row, col, moves));
    }

    public void addBottomMoves(ChessBoard board, int row, int col, List<Move> moves) {
        //noinspection StatementWithEmptyBody
        while (canExpandMove(board, --row, col, moves));
    }

    public void addLeftMoves(ChessBoard board, int row, int col, List<Move> moves) {
        //noinspection StatementWithEmptyBody
        while (canExpandMove(board, row, --col, moves));
    }

    public void addRightMoves(ChessBoard board, int row, int col, List<Move> moves) {
        //noinspection StatementWithEmptyBody
        while (canExpandMove(board, row, ++col, moves));
    }

    public void addTopLeftMoves(ChessBoard board, int row, int col, List<Move> moves) {
        //noinspection StatementWithEmptyBody
        while (canExpandMove(board, ++row, --col, moves));
    }

    public void addTopRightMoves(ChessBoard board, int row, int col, List<Move> moves) {
        //noinspection StatementWithEmptyBody
        while (canExpandMove(board, ++row, ++col, moves));
    }

    public void addBottomLeftMoves(ChessBoard board, int row, int col, List<Move> moves) {
        //noinspection StatementWithEmptyBody
        while (canExpandMove(board, --row, --col, moves));
    }

    public void addBottomRightMoves(ChessBoard board, int row, int col, List<Move> moves) {
        //noinspection StatementWithEmptyBody
        while (canExpandMove(board, --row, ++col, moves));
    }

    // Adds the move if it is valid. Returns true if there is a possibility to expand the move.
    private boolean canExpandMove(ChessBoard board, int row, int col, List<Move> moves) {
        if (!isValid(row, col)) return false;

        if (board.isEmptyTile(row, col)) {
            moves.add(new Move(row, col));
        } else {
            if (canCapture(board.getCoinAt(row, col))) {
                moves.add(new Move(row, col));
            }
            return false;
        }

        return true;
    }

    public List<Move> generateMoves(ChessBoard board, int row, int col, int[][] possibilities) {
        List<Move> moves = new ArrayList<>();

        for (int[] possibility : possibilities) {
            int dr = possibility[0], dc = possibility[1];
            int nRow = row + dr, nCol = col + dc;
            if (isValid(nRow, nCol)) {
                if (board.isEmptyTile(nRow, nCol) || canCapture(board.getCoinAt(nRow, nCol))) {
                    moves.add(new Move(nRow, nCol));
                }
            }
        }

        return moves;
    }

    public boolean canCapture(ChessCoin other) {
        if (other == null) return false;
        return this.alliance != other.alliance;
    }

    public boolean isValid(int rowOrCol) {
        return rowOrCol >= 0 && rowOrCol < 8;
    }

    public boolean isValid(int row, int col) {
        return isValid(row) && isValid(col);
    }

    public abstract List<Move> getLegalMoves(ChessBoard board, int row, int col);
}
