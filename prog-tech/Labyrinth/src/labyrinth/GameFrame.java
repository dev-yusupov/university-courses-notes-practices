package labyrinth;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Main game window with menu and high score display
 */
public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private GameState gameState;
    
    public GameFrame() {
        setTitle("Labyrinth Game - Escape from the Dragon!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Initialize game state
        gameState = new GameState();
        
        // Create game panel
        gamePanel = new GamePanel(gameState);
        add(gamePanel, BorderLayout.CENTER);
        
        // Create menu bar
        createMenuBar();
        
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Game menu
        JMenu gameMenu = new JMenu("Game");
        
        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> restartGame());
        gameMenu.add(newGameItem);
        
        JMenuItem highScoreItem = new JMenuItem("High Scores");
        highScoreItem.addActionListener(e -> showHighScores());
        gameMenu.add(highScoreItem);
        
        gameMenu.addSeparator();
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        gameMenu.add(exitItem);
        
        menuBar.add(gameMenu);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        
        JMenuItem instructionsItem = new JMenuItem("Instructions");
        instructionsItem.addActionListener(e -> showInstructions());
        helpMenu.add(instructionsItem);
        
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);
        
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void restartGame() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to start a new game?\n" +
            "Current progress will be lost.",
            "Restart Game",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            gameState.initNewGame();
            gamePanel.repaint();
            gamePanel.requestFocusInWindow();
        }
    }
    
    private void showHighScores() {
        List<HighScore> scores = HighScore.getTop10();
        
        StringBuilder message = new StringBuilder();
        message.append("=== TOP 10 HIGH SCORES ===\n\n");
        
        if (scores.isEmpty()) {
            message.append("No high scores yet!\n");
            message.append("Be the first to set a record!");
        } else {
            for (int i = 0; i < scores.size(); i++) {
                HighScore score = scores.get(i);
                message.append(String.format("%2d. %-20s %3d labyrinths\n", 
                    i + 1, score.getPlayerName(), score.getScore()));
            }
        }
        
        JTextArea textArea = new JTextArea(message.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setBackground(new Color(240, 240, 240));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JOptionPane.showMessageDialog(
            this,
            textArea,
            "High Scores",
            JOptionPane.PLAIN_MESSAGE
        );
    }
    
    private void showInstructions() {
        String instructions = 
            "=== HOW TO PLAY ===\n\n" +
            "OBJECTIVE:\n" +
            "Escape from the labyrinth by reaching the green area\n" +
            "in the top-right corner. Avoid the dragon!\n\n" +
            "CONTROLS:\n" +
            "W / ↑  - Move Up\n" +
            "A / ←  - Move Left\n" +
            "S / ↓  - Move Down\n" +
            "D / →  - Move Right\n\n" +
            "GAME ELEMENTS:\n" +
            "• Blue Square = You (the player)\n" +
            "• Red Circle = Dragon (deadly enemy)\n" +
            "• Dark Gray = Walls\n" +
            "• Light Gray = Paths\n" +
            "• Green Area = Goal (top-right corner)\n" +
            "• Black Areas = Fog of war (unexplored)\n\n" +
            "RULES:\n" +
            "• You can only see 3 units around you\n" +
            "• The dragon moves randomly through the maze\n" +
            "• If the dragon gets adjacent to you, you lose!\n" +
            "• Solve as many labyrinths as possible\n" +
            "• Your score is saved when you lose\n\n" +
            "Good luck escaping!";
        
        JTextArea textArea = new JTextArea(instructions);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 12));
        textArea.setBackground(new Color(240, 240, 240));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JOptionPane.showMessageDialog(
            this,
            textArea,
            "Instructions",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void showAbout() {
        String about = 
            "Labyrinth Game\n\n" +
            "Version 1.0\n\n" +
            "A thrilling maze escape game where you must\n" +
            "navigate through dark labyrinths while avoiding\n" +
            "a dangerous dragon.\n\n" +
            "Features:\n" +
            "• Procedurally generated mazes\n" +
            "• Fog of war visibility system\n" +
            "• Smart dragon AI\n" +
            "• High score tracking\n" +
            "• Progressive difficulty\n\n" +
            "Created as a Programming Technology project\n" +
            "2025";
        
        JOptionPane.showMessageDialog(
            this,
            about,
            "About Labyrinth Game",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    @Override
    public void dispose() {
        if (gamePanel != null) {
            gamePanel.stopTimer();
        }
        // Close database connection
        DatabaseManager.getInstance().close();
        super.dispose();
    }
}
