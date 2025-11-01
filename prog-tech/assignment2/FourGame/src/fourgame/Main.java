package fourgame;

import javax.swing.*;

public class Main {

    // Test hook: forces board size if not null
    public static Integer testBoardSize = null;
    public static boolean testMode = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::startGame);
    }
    
    public static void startGame() {

        int size;

        if (testBoardSize != null) {
            size = testBoardSize;
        } else {
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

            size = switch (choice) {
                case "5 x 5" -> 5;
                case "7 x 7" -> 7;
                default -> 3;
            };
        }

        GameFrame frame = new GameFrame(size);
        frame.testMode = testMode; // Disable popups in tests
    }
}
