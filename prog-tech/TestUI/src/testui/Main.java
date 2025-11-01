package testui;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    private int size = 3; 
    private JButton[][] buttons;
    private int[][] values;
    private boolean playerOneTurn = true; // true = Red, false = Blue
    private int redScore = 0, blueScore = 0;

    public Main() {
        selectBoardSize();
        initGame();
    }

    private void selectBoardSize() {
        String[] options = {"3x3", "5x5", "7x7"};
        String choice = (String) JOptionPane.showInputDialog(
                null, 
                "Select board size:",
                "Board Size",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice != null) {
            if (choice.equals("3x3")) size = 3;
            else if (choice.equals("5x5")) size = 5;
            else size = 7;
        }
    }

    private void initGame() {
        buttons = new JButton[size][size];
        values = new int[size][size];

        setTitle("Increment Game");
        setLayout(new GridLayout(size, size));
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Build grid
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                JButton btn = new JButton("0");
                btn.setFont(new Font("Arial", Font.BOLD, 24));

                int row = r, col = c;
                btn.addActionListener(e -> handleClick(row, col));

                buttons[r][c] = btn;
                add(btn);
            }
        }

        setVisible(true);
    }

    private void handleClick(int r, int c) {
        increment(r, c);
        increment(r-1, c);
        increment(r+1, c);
        increment(r, c-1);
        increment(r, c+1);

        updateBoard();

        if (isGameOver()) {
            String winner = (redScore > blueScore) ? "RED" : (blueScore > redScore) ? "BLUE" : "DRAW";
            JOptionPane.showMessageDialog(this, "Game Over!\nRed: " + redScore + " | Blue: " + blueScore + "\nWinner: " + winner);
            restartGame();
            return;
        }

        playerOneTurn = !playerOneTurn; // switch player
    }

    private void increment(int r, int c) {
        if (r < 0 || c < 0 || r >= size || c >= size) return;

        if (values[r][c] < 4) {
            values[r][c]++;
            if (values[r][c] == 4) {
                if (playerOneTurn) redScore++;
                else blueScore++;
            }
        }
    }

    private void updateBoard() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                buttons[r][c].setText(String.valueOf(values[r][c]));

                if (values[r][c] == 4) {
                    buttons[r][c].setBackground(playerOneTurn ? Color.RED : Color.BLUE);
                    buttons[r][c].setOpaque(true);
                }
            }
        }
    }

    private boolean isGameOver() {
        for (int[] row : values) {
            for (int val : row) {
                if (val < 4) return false;
            }
        }
        return true;
    }

    private void restartGame() {
        dispose();
        new Main();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
