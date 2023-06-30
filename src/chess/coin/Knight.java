package chess.coin;

import chess.board.Board;
import chess.util.Move;
import chess.util.Position;

import java.util.List;

public class Knight extends ChessCoin {
    public Knight(Alliance alliance) {
        super(alliance);
        String imageName = (alliance == Alliance.WHITE) ? "wKnight.png" : "bKnight.png";
        setIcon(ChessCoinUtil.getCoinImageIcon(imageName));
    }

    @Override
    public List<Move> getPossibleMoves(Board board, Position from) {
        int[][] relatives = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        return ChessCoinUtil.generateRelativeMoves(board, from, relatives);
    }

}
