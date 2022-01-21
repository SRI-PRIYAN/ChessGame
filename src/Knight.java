import javax.swing.ImageIcon;
import java.util.List;

public class Knight extends ChessCoin {
    public Knight(Alliance alliance) {
        super(alliance);
        if (alliance == Alliance.WHITE) setIcon(new ImageIcon("resources/wKnight.png"));
        else setIcon(new ImageIcon("resources/bKnight.png"));
    }

    @Override
    public List<Move> getPossibleMoves(ChessCoinContainer container, Position from) {
        int[][] possibilities = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        return generateMoves(container, from, possibilities);
    }

}
