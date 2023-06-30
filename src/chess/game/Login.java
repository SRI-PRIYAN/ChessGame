package chess.game;

import javax.swing.JOptionPane;

public class Login {
    public static void main(String[] args) {
        String playerName = JOptionPane.showInputDialog("Enter Name");
        new Dashboard(playerName);
    }
}
