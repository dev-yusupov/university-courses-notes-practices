package fourgame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::startGame);
    }
    
    public static void startGame() {
        String[] options = {"3 x 3", "5 x 5", "7 x 7"};
        String choice = (String) JOptionPane.showInputDialog(
                null,
                "Choose board size:",
                "Board Size",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == null) System.exit(0);

        int size = switch (choice) {
            case "5 x 5" -> 5;
            case "7 x 7" -> 7;
            default -> 3;
        };

        GameFrame gameFrame = new GameFrame(size);
    }
}
