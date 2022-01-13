import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Tile extends JPanel {
    private final int row;
    private final int col;
    private final Color color;
    private ChessCoin coin = null;
    private boolean isHighlighted;

    public Tile(ChessBoard board, int row, int col, int side, Color color) {
        this.row = row;
        this.col = col;
        this.color = color;

        setPreferredSize(new Dimension(side, side));
        setBackground(color);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createLineBorder(Color.black));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boolean wasHighlighted = isHighlighted;
                clearAllTileHighlighting();

                if (wasHighlighted) {
                    if (hasCoin()) board.captureCoin(Tile.this);
                    else board.moveCoin(Tile.this);
                }
                else if (hasCoin()) {
                    board.setSelectedTile(Tile.this);

                    List<Move> moves = coin.getLegalMoves(board, row, col);
                    for (Move move : moves) {
                        board.highlightTile(move.getRow(), move.getCol());
                    }
                }
                else {
                    board.setSelectedTile(null);
                }
            }

            private void clearAllTileHighlighting() {
                for (int row = 0; row < 8; row++) {
                    for (int col = 0; col < 8; col++) {
                        if (board.isHighlightedTile(row, col)) {
                            board.resetTile(row, col);
                        }
                    }
                }
            }
        });
    }

    public ChessCoin getCoin() {
        return coin;
    }

    public void setCoin(ChessCoin newCoin) {
        if (hasCoin()) remove(coin);
        this.coin = newCoin;
        add(newCoin);
        revalidate();
    }

    public void removeCoin() {
        if (hasCoin()) {
            remove(coin);
            this.coin = null;
            repaint();
        }
    }

    public boolean hasCoin() {
        return coin != null;
    }

    public void highlight(Color c) {
        isHighlighted = true;
        setBackground(c);
    }

    public void reset() {
        isHighlighted = false;
        setBackground(color);
    }

    public boolean getIsHighlighted() {
        return isHighlighted;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
