package fourgame;

import java.awt.*;
import javax.swing.*;

public class FourGame extends JFrame {
    private int size = 3;
    private JButton[][] buttons;
    private int[][] values;
    private boolean playerOneTurn = true;
    private int redScore = 0, blueScore = 0;
    
    public FourGame() {
        selectBoardSize();
        initGame();
    }
    
    private void selectBoardSize() {
        String[] options = {
            "3 x 3",
            "5 x 5",
            "7 x 7",
        };
        
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
            if (choice.equals("3 x 3")) size = 3;
            else if (choice.equals("5 x 5")) size = 5;
            else size = 7;
        }
    }
    
    private void initGame() {
        buttons = new JButton[size][size];
        values = new int[size][size];
        
        initFrame();
        initButtonGrid();
        
        setVisible(true);
    }
    
    private void initFrame() {
        setTitle("Four Game");
        setLayout(new GridLayout(size, size));
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    private void initButtonGrid() {
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
            JOptionPane.showMessageDialog(this, "Game Over!\n Red: " + redScore + " | Blue: " + blueScore + "\nWinner: " + winner);
            restartGame();
            return;
        }
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
        new FourGame();
    }
}
