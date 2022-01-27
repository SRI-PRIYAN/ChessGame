import java.util.ArrayList;
import java.util.List;

public class Rook extends ChessCoin {
    public Rook(Alliance alliance) {
        super(alliance);
        if (alliance == Alliance.WHITE) setIcon(getCoinImageIcon("resources/wRook.png"));
        else setIcon(getCoinImageIcon("resources/bRook.png"));
    }

    @Override
    public List<Move> getPossibleMoves(Board board, Position from) {
        List<Move> moves = new ArrayList<>();

        addAxialMoves(board, from, moves);

        return moves;
    }
}
