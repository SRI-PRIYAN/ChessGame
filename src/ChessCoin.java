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

    @SuppressWarnings("StatementWithEmptyBody")
    public void addAxialMoves(ChessBoard board, int row, int col, List<Move> moves) {
        for (int i = 1; canExpandMove(board, row + i, col, moves); i++); // Top
        for (int i = 1; canExpandMove(board, row - i, col, moves); i++); // Bottom
        for (int i = 1; canExpandMove(board, row, col - i, moves); i++); // Left
        for (int i = 1; canExpandMove(board, row, col + i, moves); i++); // Right
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void addDiagonalMoves(ChessBoard board, int row, int col, List<Move> moves) {
        for (int i = 1; canExpandMove(board, row + i, col - i, moves); i++); // Top Left
        for (int i = 1; canExpandMove(board, row + i, col + i, moves); i++); // Top Right
        for (int i = 1; canExpandMove(board, row - i, col - i, moves); i++); // Bottom Left
        for (int i = 1; canExpandMove(board, row - i, col + i, moves); i++); // Bottom Right
    }

    // Adds the move if it is valid. Returns true if there is a possibility to expand the move.
    // That is, returns false if the (row, col) is invalid OR if the tile is not empty (in which case
    // the move cannot be expanded).
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
