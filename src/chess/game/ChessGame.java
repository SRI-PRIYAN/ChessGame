package chess.game;

import chess.board.ChessBoard;
import chess.coin.Alliance;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChessGame extends JFrame {
    public ChessGame(String playerName, Socket s) throws IOException {
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        dos.writeUTF(playerName);

        DataInputStream dis = new DataInputStream(s.getInputStream());
        Alliance playerAlliance = dis.readUTF().equals("White") ? Alliance.WHITE : Alliance.BLACK;
        String opponentName = dis.readUTF();

        setTitle(playerName + " vs " + opponentName);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.lightGray);
        setSize(720, 720);

        add(new ChessBoard(640, playerAlliance, dis, dos));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
