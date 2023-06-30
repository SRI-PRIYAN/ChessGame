package chess.coin;

import chess.board.Board;
import chess.util.Move;
import chess.util.Position;

import java.util.ArrayList;
import java.util.List;

public class Rook extends ChessCoin {
    public Rook(Alliance alliance) {
        super(alliance);
        String imageName = (alliance == Alliance.WHITE) ? "wRook.png" : "bRook.png";
        setIcon(ChessCoinUtil.getCoinImageIcon(imageName));
    }

    @Override
    public List<Move> getPossibleMoves(Board board, Position from) {
        List<Move> moves = new ArrayList<>();

        ChessCoinUtil.addAxialMoves(board, from, moves);

        return moves;
    }
}
