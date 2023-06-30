package chess.coin;

import chess.board.Board;
import chess.util.Move;
import chess.util.Position;

import java.util.ArrayList;
import java.util.List;

public class Queen extends ChessCoin {
    public Queen(Alliance alliance) {
        super(alliance);
        String imageName = (alliance == Alliance.WHITE) ? "wQueen.png" : "bQueen.png";
        setIcon(ChessCoinUtil.getCoinImageIcon(imageName));
    }

    @Override
    public List<Move> getPossibleMoves(Board board, Position from) {
        List<Move> moves = new ArrayList<>();

        ChessCoinUtil.addAxialMoves(board, from, moves);
        ChessCoinUtil.addDiagonalMoves(board, from, moves);

        return moves;
    }

}
