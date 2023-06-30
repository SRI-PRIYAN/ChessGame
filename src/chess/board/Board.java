package chess.board;

import chess.coin.Alliance;
import chess.coin.ChessCoin;
import chess.util.Position;

public interface Board {
    ChessCoin getCoinAt(int row, int col);

    Tile getTileAt(int row, int col);

    default ChessCoin getCoinAt(Position position) {
        return getCoinAt(position.getRow(), position.getCol());
    }

    default boolean hasNoCoinAt(int row, int col) {
        return getCoinAt(row, col) == null;
    }

    Alliance getPlayerAlliance();
}
