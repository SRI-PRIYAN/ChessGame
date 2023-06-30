package chess.coin;

import chess.board.Board;
import chess.util.Move;
import chess.util.Position;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends ChessCoin {
    public Bishop(Alliance alliance) {
        super(alliance);
        String imageName = (alliance == Alliance.WHITE) ? "wBishop.png" : "bBishop.png";
        setIcon(ChessCoinUtil.getCoinImageIcon(imageName));
    }

    @Override
    public List<Move> getPossibleMoves(Board board, Position from) {
        List<Move> moves = new ArrayList<>();

        ChessCoinUtil.addDiagonalMoves(board, from, moves);

        return moves;
    }

}
