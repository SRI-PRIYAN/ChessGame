import java.util.ArrayList;
import java.util.List;

public class Queen extends ChessCoin {
    public Queen(Alliance alliance) {
        super(alliance);
        if (alliance == Alliance.WHITE) setIcon(getCoinImageIcon("resources/wQueen.png"));
        else setIcon(getCoinImageIcon("resources/bQueen.png"));
    }

    @Override
    public List<Move> getPossibleMoves(Board board, Position from) {
        List<Move> moves = new ArrayList<>();

        addAxialMoves(board, from, moves);
        addDiagonalMoves(board, from, moves);

        return moves;
    }

}
