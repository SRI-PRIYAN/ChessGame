import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends ChessCoin {
    public Bishop(Alliance alliance) {
        super(alliance);
        if (alliance == Alliance.WHITE) setIcon(new ImageIcon("resources/wBishop.png"));
        else setIcon(new ImageIcon("resources/bBishop.png"));
    }

    @Override
    public List<Move> getLegalMoves(ChessBoard board, int row, int col) {
        List<Move> moves = new ArrayList<>();

        addDiagonalMoves(board, row, col, moves);

        return moves;
    }

}
