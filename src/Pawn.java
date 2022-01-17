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

        if (!isValid(row - 1)) return moves;

        if (board.isEmptyTile(row - 1, col)) {
            moves.add(new Move(row - 1, col));
            if (hasNotMoved() && board.isEmptyTile(row - 2, col)) {
                moves.add(new Move(row - 2, col));
            }
        }

        if (isValid(col - 1) && canCapture(board.getCoinAt(row - 1, col - 1))) {
            moves.add(new Move(row - 1, col - 1));
        }

        if (isValid(col + 1) && canCapture(board.getCoinAt(row - 1, col + 1))) {
            moves.add(new Move(row - 1, col + 1));
        }

        return moves;
    }

    public boolean canPromote(int row) {
        return row == 0;
    }
}
