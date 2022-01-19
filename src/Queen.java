import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;

public class Queen extends ChessCoin {
    public Queen(Alliance alliance) {
        super(alliance);
        if (alliance == Alliance.WHITE) setIcon(new ImageIcon("resources/wQueen.png"));
        else setIcon(new ImageIcon("resources/bQueen.png"));
    }

    @Override
    public List<Move> getLegalMoves(ChessBoard board, int row, int col) {
        List<Move> moves = new ArrayList<>();

        addAxialMoves(board, row, col, moves);
        addDiagonalMoves(board, row, col, moves);

        return moves;
    }

}
