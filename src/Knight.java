import java.util.List;

public class Knight extends ChessCoin {
    public Knight(Alliance alliance) {
        super(alliance);
        if (alliance == Alliance.WHITE) setIcon(getCoinImageIcon("resources/wKnight.png"));
        else setIcon(getCoinImageIcon("resources/bKnight.png"));
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
