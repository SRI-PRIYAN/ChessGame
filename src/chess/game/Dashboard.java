package chess.game;

import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.FlowLayout;
import java.io.IOException;
import java.net.Socket;

public class Dashboard extends JFrame {
    public Dashboard(String playerName) {
        super(playerName);
        setLayout(new FlowLayout());
        setSize(400, 100);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton playButton = new JButton("Play Random Player");
        playButton.setFocusable(false);

        playButton.addActionListener(e -> {
            try {
                Socket s = new Socket("localhost", 3065);
                new ChessGame(playerName, s);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        add(playButton);
        setVisible(true);
    }
}
