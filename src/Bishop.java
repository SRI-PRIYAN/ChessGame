import java.util.ArrayList;
import java.util.List;

public class Bishop extends ChessCoin {
    public Bishop(Alliance alliance) {
        super(alliance);
        if (alliance == Alliance.WHITE) setIcon(getCoinImageIcon("resources/wBishop.png"));
        else setIcon(getCoinImageIcon("resources/bBishop.png"));
    }

    @Override
    public List<Move> getPossibleMoves(Board board, Position from) {
        List<Move> moves = new ArrayList<>();

        addDiagonalMoves(board, from, moves);

        return moves;
    }

}
