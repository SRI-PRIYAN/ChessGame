package chess.coin;

public class ChessCoinFactory {
    public static ChessCoin create(String type, Alliance alliance) {
        if (type.equals("King")) return new King(alliance);
        if (type.equals("Queen")) return new Queen(alliance);
        if (type.equals("Rook")) return new Rook(alliance);
        if (type.equals("Bishop")) return new Bishop(alliance);
        if (type.equals("Knight")) return new Knight(alliance);
        if (type.equals("Pawn")) return new Pawn(alliance);

        return null;
    }
}
