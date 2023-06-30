package chess.util;

public class Move {
    public final Position from;
    public final Position to;

    public Move(Position from, Position to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return String.format("%s --> %s", from, to);
    }
}
