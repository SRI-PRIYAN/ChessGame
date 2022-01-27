import java.util.List;

public class Knight extends ChessCoin {
    public Knight(Alliance alliance) {
        super(alliance);
        if (alliance == Alliance.WHITE) setIcon(getCoinImageIcon("resources/wKnight.png"));
        else setIcon(getCoinImageIcon("resources/bKnight.png"));
    }

    @Override
    public List<Move> getPossibleMoves(Board board, Position from) {
        int[][] possibilities = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        return generateMoves(board, from, possibilities);
    }

}
