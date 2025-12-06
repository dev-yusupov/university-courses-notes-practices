package tron.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import tron.database.Database;
import tron.database.FileDatabase;
import tron.database.PlayerScore;
import tron.database.PostgresDatabase;
import tron.logic.LevelGenerator;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final Database database;
    private final MenuPanel menuPanel;
    private final GamePanel gamePanel;
    private final HighScorePanel highScorePanel;

    public MainFrame() {
        setTitle("Tron Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // database = new FileDatabase();
        database = new PostgresDatabase();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        menuPanel = new MenuPanel(this);
        gamePanel = new GamePanel(this, database);
        highScorePanel = new HighScorePanel(this, database);

        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(gamePanel, "GAME");
        mainPanel.add(highScorePanel, "HIGHSCORE");

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    public void showMenu() {
        cardLayout.show(mainPanel, "MENU");
        pack(); // Resize potentially
        setLocationRelativeTo(null);
    }

    public void startGame(String p1Name, Color p1Color, String p2Name, Color p2Color) {
        gamePanel.startNewGame(p1Name, p1Color, p2Name, p2Color);
        cardLayout.show(mainPanel, "GAME");
        gamePanel.requestFocusInWindow();
        pack();
        setLocationRelativeTo(null);
    }

    public void showHighScores() {
        highScorePanel.refresh();
        cardLayout.show(mainPanel, "HIGHSCORE");
        pack(); // Resize
        setLocationRelativeTo(null);
    }
}
