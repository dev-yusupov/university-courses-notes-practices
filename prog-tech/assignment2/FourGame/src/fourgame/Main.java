package fourgame;

import javax.swing.*;

/**
 * Main entry point for the Four Game application.
 * This class handles game initialization and board size selection.
 * It provides test hooks for automated testing.
 */
public class Main {

    // Test hook: forces board size if not null
    public static Integer testBoardSize = null;
    public static boolean testMode = false;

    /**
     * Application entry point.
     * Launches the game on the Event Dispatch Thread.
     * 
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::startGame);
    }
    
    /**
     * Starts a new game by displaying board size selection dialog
     * and creating a new GameFrame with the selected size.
     * If in test mode, uses the testBoardSize instead of showing a dialog.
     * Supports board sizes: 3x3, 5x5, and 7x7.
     */
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
