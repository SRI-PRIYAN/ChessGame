import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends ChessCoin {
    public Pawn(Alliance alliance) {
        super(alliance);
        if (alliance == Alliance.WHITE) setIcon(new ImageIcon("resources/wPawn.png"));
        else setIcon(new ImageIcon("resources/bPawn.png"));
    }

    @Override
    public List<Move> getLegalMoves(ChessBoard board, int row, int col) {
        List<Move> moves = new ArrayList<>();

        int factor = (alliance == Alliance.WHITE) ? -1 : 1;

        if (!isValid(row + factor)) return moves;

        if (board.isEmptyTile(row + factor, col)) {
            moves.add(new Move(row + factor, col));
            if (hasNotMoved() && board.isEmptyTile(row + 2 * factor, col)) {
                moves.add(new Move(row + 2 * factor, col));
            }
        }

        if (isValid(col + 1) && canCapture(board.getCoinAt(row + factor, col + 1))) {
            moves.add(new Move(row + factor, col + 1));
        }

        if (isValid(col - 1) && canCapture(board.getCoinAt(row + factor, col - 1))) {
            moves.add(new Move(row + factor, col - 1));
        }

        return moves;
    }

    public boolean canPromote(int row) {
        if (alliance == Alliance.WHITE) return row == 0;
        else return row == 7;
    }
}
