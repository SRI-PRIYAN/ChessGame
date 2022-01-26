import java.util.ArrayList;
import java.util.List;

public class Bishop extends ChessCoin {
    public Bishop(Alliance alliance) {
        super(alliance);
        if (alliance == Alliance.WHITE) setIcon(getCoinImageIcon("resources/wBishop.png"));
        else setIcon(getCoinImageIcon("resources/bBishop.png"));
    }

    @Override
    public List<Move> getPossibleMoves(ChessCoinContainer container, Position from) {
        List<Move> moves = new ArrayList<>();

        addDiagonalMoves(container, from, moves);

        return moves;
    }

}
