package fourgame;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private final GameModel model;
    private final JButton[][] buttons;

    public GameFrame(int size) {
        this.model = new GameModel(size);
        this.buttons = new JButton[size][size];

        setTitle("Reach 4 Game");
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

    private void handleClick(int r, int c) {
        model.increment(r, c);
        updateBoard();

        if (model.isGameOver()) {
            String winner = model.redScore > model.blueScore ?
                    "Red Player Wins!" :
                    model.redScore < model.blueScore ?
                            "Blue Player Wins!" :
                            "It's a Draw!";

            JOptionPane.showMessageDialog(this,
                    winner + "\nFinal Score:\nRed: " + model.redScore + "\nBlue: " + model.blueScore);

            dispose();
            Main.startGame();
        } else {
            model.nextPlayer();
        }
    }

    private void updateBoard() {
        for (int r = 0; r < model.size; r++) {
            for (int c = 0; c < model.size; c++) {
                buttons[r][c].setText("" + model.values[r][c]);
                if (model.values[r][c] == 4) {
                    buttons[r][c].setBackground(model.owner[r][c] == 1 ? Color.RED : Color.BLUE);
                }
            }
        }
    }
}
