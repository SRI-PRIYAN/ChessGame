import java.util.ArrayList;
import java.util.List;

public class Pawn extends ChessCoin {
    public Pawn(Alliance alliance) {
        super(alliance);
        if (alliance == Alliance.WHITE) setIcon(getCoinImageIcon("resources/wPawn.png"));
        else setIcon(getCoinImageIcon("resources/bPawn.png"));
    }

    @Override
    public List<Move> getPossibleMoves(Board board, Position from) {
        List<Move> moves = new ArrayList<>();

        int row = from.getRow(), col = from.getCol();

        int factor = (board.getCoinAt(row, col).alliance == board.getPlayerAlliance()) ? -1 : 1;

        if (!isValid(row + factor)) return moves;

        if (board.hasNoCoinAt(row + factor, col)) {
            moves.add(new Move(from, new Position(row + factor, col)));
            if (hasNotMoved() && isValid(row + 2 * factor) && board.hasNoCoinAt(row + 2 * factor, col)) {
                moves.add(new Move(from, new Position(row + 2 * factor, col)));
            }
        }

        if (isValid(col - 1) && canCapture(board.getCoinAt(row + factor, col - 1))) {
            moves.add(new Move(from, new Position(row + factor, col - 1)));
        }

        if (isValid(col + 1) && canCapture(board.getCoinAt(row + factor, col + 1))) {
            moves.add(new Move(from, new Position(row + factor, col + 1)));
        }

        return moves;
    }

    public boolean canPromote(int row) {
        return row == 0;
    }
}
