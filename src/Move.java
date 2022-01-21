public class Move {
    final Position from;
    final Position to;

    public Move(Position from, Position to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return String.format("%s --> %s", from, to);
    }
}
