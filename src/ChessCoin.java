import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.io.IOException;
import java.io.InputStream;
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

    protected ImageIcon getCoinImageIcon(String path) {
        InputStream inputStream = getClass().getResourceAsStream(path);
        if (inputStream != null) {
            try {
                return new ImageIcon(inputStream.readAllBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void addAxialMoves(ChessCoinContainer container, Position from, List<Move> moves) {
        int row = from.getRow(), col = from.getCol();
        for (int i = 1; canExpandMove(container, from, new Position(row + i, col), moves); i++); // Top
        for (int i = 1; canExpandMove(container, from, new Position(row - i, col), moves); i++); // Bottom
        for (int i = 1; canExpandMove(container, from, new Position(row, col - i), moves); i++); // Left
        for (int i = 1; canExpandMove(container, from, new Position(row, col + i), moves); i++); // Right
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void addDiagonalMoves(ChessCoinContainer container, Position from, List<Move> moves) {
        int row = from.getRow(), col = from.getCol();
        for (int i = 1; canExpandMove(container, from, new Position(row + i, col - i), moves); i++); // T-L
        for (int i = 1; canExpandMove(container, from, new Position(row + i, col + i), moves); i++); // T-R
        for (int i = 1; canExpandMove(container, from, new Position(row - i, col - i), moves); i++); // B-L
        for (int i = 1; canExpandMove(container, from, new Position(row - i, col + i), moves); i++); // B-R
    }

    // Adds the move if it is valid. Returns true if there is a possibility to expand the move.
    // That is, returns false if the (row, col) is invalid OR if the tile is not empty (in which case
    // the move cannot be expanded).
    private boolean canExpandMove(ChessCoinContainer container, Position from, Position to, List<Move> moves) {
        int row = to.getRow(), col = to.getCol();

        if (!isValid(row, col)) return false;

        if (container.hasNoCoinAt(row, col)) {
            moves.add(new Move(from, to));
        } else {
            if (canCapture(container.getCoinAt(row, col))) {
                moves.add(new Move(from, to));
            }
            return false;
        }

        return true;
    }

    public List<Move> generateMoves(ChessCoinContainer container, Position from, int[][] possibilities) {
        List<Move> moves = new ArrayList<>();

        int row = from.getRow(), col = from.getCol();
        for (int[] possibility : possibilities) {
            int dr = possibility[0], dc = possibility[1];
            int nRow = row + dr, nCol = col + dc;
            if (isValid(nRow, nCol)) {
                if (container.hasNoCoinAt(nRow, nCol) || canCapture(container.getCoinAt(nRow, nCol))) {
                    moves.add(new Move(from, new Position(nRow, nCol)));
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

    public abstract List<Move> getPossibleMoves(ChessCoinContainer container, Position from);

    // Filters the moves from getPossibleMoves that puts the King in check
    public List<Move> getLegalMoves(ChessCoinContainer container, Position from) {
        List<Move> legalMoves = new ArrayList<>();

        for (Move possibleMove : getPossibleMoves(container, from)) {
            AnticipatedChessBoard anticipatedChessBoard = new AnticipatedChessBoard(container, possibleMove);
            if (!CheckmateDetector.isKingInCheck(anticipatedChessBoard, alliance)) {
                legalMoves.add(possibleMove);
            }
        }

        return legalMoves;
    }
}
