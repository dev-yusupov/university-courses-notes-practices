package tron.gui;

import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import tron.db.Database;

/**
 * The main window of the application, managing view switching between Menu,
 * Game, and HighScores.
 */
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

        database = new Database();
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

    /**
     * Switches the view to the main menu.
     */
    public void showMenu() {
        cardLayout.show(mainPanel, "MENU");
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Starts a new game by switching to the GamePanel and initializing the game
     * model.
     * 
     * @param p1Name  Player 1 name.
     * @param p1Color Player 1 color.
     * @param p2Name  Player 2 name.
     * @param p2Color Player 2 color.
     */
    public void startGame(String p1Name, Color p1Color, String p2Name, Color p2Color) {
        gamePanel.startNewGame(p1Name, p1Color, p2Name, p2Color);
        cardLayout.show(mainPanel, "GAME");
        gamePanel.requestFocusInWindow();
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Switches the view to the high score panel and refreshes the scores.
     */
    public void showHighScores() {
        highScorePanel.refresh();
        cardLayout.show(mainPanel, "HIGHSCORE");
        pack();
        setLocationRelativeTo(null);
    }
}
