import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;

public class Rook extends ChessCoin {
    public Rook(Alliance alliance) {
        super(alliance);
        if (alliance == Alliance.WHITE) setIcon(new ImageIcon("resources/wRook.png"));
        else setIcon(new ImageIcon("resources/bRook.png"));
    }

    @Override
    public List<Move> getLegalMoves(ChessBoard board, int row, int col) {
        List<Move> moves = new ArrayList<>();

        addAxialMoves(board, row, col, moves);

        return moves;
    }
}
