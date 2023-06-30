package chess.coin;

import chess.analyzer.CheckmateDetector;
import chess.board.AnticipatedChessBoard;
import chess.board.Board;
import chess.util.Move;
import chess.util.Position;

import javax.swing.JLabel;
import java.util.ArrayList;
import java.util.List;

public abstract class ChessCoin extends JLabel {
    protected final Alliance alliance;
    private int numberOfMovesMade;

    public ChessCoin(Alliance alliance) {
        this.alliance = alliance;
        this.numberOfMovesMade = 0;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public int getNumberOfMovesMade() {
        return numberOfMovesMade;
    }

    public boolean hasMoved() {
        return numberOfMovesMade != 0;
    }

    public boolean hasNotMoved() {
        return numberOfMovesMade == 0;
    }

    public void incrementNumberOfMovesMade() {
        numberOfMovesMade++;
    }

    public boolean canCapture(ChessCoin other) {
        if (other == null) return false;
        return this.alliance != other.alliance;
    }

    public abstract List<Move> getPossibleMoves(Board board, Position from);

    // Filters the moves from getPossibleMoves that puts the King in check
    public List<Move> getLegalMoves(Board board, Position from) {
        List<Move> legalMoves = new ArrayList<>();

        for (Move possibleMove : getPossibleMoves(board, from)) {
            AnticipatedChessBoard anticipatedChessBoard = new AnticipatedChessBoard(board, possibleMove);
            if (!CheckmateDetector.isKingInCheck(anticipatedChessBoard, alliance)) {
                legalMoves.add(possibleMove);
            }
        }

        return legalMoves;
    }
}
