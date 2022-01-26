import java.util.List;

public class King extends ChessCoin {
    public King(Alliance alliance) {
        super(alliance);
        if (alliance == Alliance.WHITE) setIcon(getCoinImageIcon("resources/wKing.png"));
        else setIcon(getCoinImageIcon("resources/bKing.png"));
    }

    @Override
    public List<Move> getPossibleMoves(ChessCoinContainer container, Position from) {
        int[][] possibilities = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                {1, 1}, {-1, 1}, {1, -1}, {-1, -1}
        };

        return generateMoves(container, from, possibilities);
    }

}
