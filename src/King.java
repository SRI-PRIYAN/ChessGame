import javax.swing.ImageIcon;
import java.util.List;

public class King extends ChessCoin {
    public King(Alliance alliance) {
        super(alliance);
        if (alliance == Alliance.WHITE) setIcon(new ImageIcon("resources/wKing.png"));
        else setIcon(new ImageIcon("resources/bKing.png"));
    }

    @Override
    public List<Move> getLegalMoves(ChessBoard board, int row, int col) {
        int[][] possibilities = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                {1, 1}, {-1, 1}, {1, -1}, {-1, -1}
        };

        return generateMoves(board, row, col, possibilities);
    }

}
