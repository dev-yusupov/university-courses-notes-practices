package fourgame;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the graphical user interface for the Four Game.
 * This class extends JFrame and manages the game display, user interactions,
 * and communication with the GameModel.
 * The frame displays a grid of buttons representing the game board,
 * handles button click events, and manages game flow.
 */
public class GameFrame extends JFrame {
    private final GameModel model;
    private final JButton[][] buttons;
    protected boolean testMode = false;

    /**
     * Constructs a new GameFrame with the specified board size.
     * Creates the game window with a grid layout of buttons,
     * initializes the GameModel, and sets up event listeners.
     * 
     * @param size the dimension of the square board (3, 5, or 7)
     */
    public GameFrame(int size) {
        this.model = new GameModel(size);
        this.buttons = new JButton[size][size];

        setTitle("Four Game");
        setLayout(new GridLayout(size, size));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                JButton btn = new JButton("0");
                btn.setPreferredSize(new Dimension(100, 100));
                btn.setFont(new Font("Arial", Font.BOLD, 24));
                buttons[r][c] = btn;

                int rr = r, cc = c;
                btn.addActionListener(e -> handleClick(rr, cc));
                add(btn);
            }
        }

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public GameModel getModel() {
        return this.model;
    }
    
    /**
     * Handles button click events from the game board.
     * When a field is clicked, increments the field and its neighbors,
     * updates the display, checks for game over, and switches players.
     * 
     * @param r the row index of the clicked button
     * @param c the column index of the clicked button
     */
    private void handleClick(int r, int c) {
        model.increment(r, c);
        updateBoard();

        if (model.isGameOver()) {
            if (!testMode) showFinalResult();
            restartGame();
        } else {
            model.nextPlayer();
        }
    }
    
    /**
     * Displays the final game result in a message dialog.
     * Shows which player won (or if it's a draw) along with the final scores.
     * Only called when not in test mode.
     */
    private void showFinalResult() {
        String winnerMessage = getWinnerMessage();
        
        JOptionPane.showMessageDialog(this,
                    winnerMessage + 
                            "\nFinal Score:\nRed: " + model.getRedScore() + "\nBlue: " + model.getBlueScore());
    }
    
    /**
     * Determines the winner of the game based on scores.
     * Compares red and blue player scores to generate the appropriate message.
     * 
     * @return a message indicating which player won or if it's a draw
     */
    private String getWinnerMessage() {
        if (model.getRedScore() > model.getBlueScore()) return "Red Player Wins!";
        if (model.getBlueScore() > model.getRedScore()) return "Blue Player Wins!";
        return "It's a Draw!";
    }

    /**
     * Updates the visual display of the game board.
     * Refreshes all button texts to reflect current field values and
     * colorizes buttons for fields that have reached 4.
     * Red background for player one, blue background for player two.
     */
    private void updateBoard() {
        for (int r = 0; r < model.getSize(); r++) {
            for (int c = 0; c < model.getSize(); c++) {
                buttons[r][c].setText("" + model.getValue(r, c));
                if (model.getValue(r, c) == 4) {
                    buttons[r][c].setBackground(model.getOwnerAt(r, c) == 1 ? Color.RED : Color.BLUE);
                }
            }
        }
    }
    
    /**
     * Restarts the game by closing the current window and starting a new game.
     * Disposes the current frame and calls Main.startGame() to create a new game instance.
     * In test mode, only disposes the frame without creating a new game.
     */
    protected void restartGame() {
        dispose();
        if (!testMode) {
            Main.startGame();
        }
    }
}
