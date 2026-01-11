package labyrinth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Main game panel that renders the labyrinth, player, and dragon
 */
public class GamePanel extends JPanel {
    private GameState gameState;
    private static final int CELL_SIZE = 20;
    private static final int VISIBILITY_RANGE = 3;
    private Timer gameTimer;
    
    // Colors
    private static final Color WALL_COLOR = new Color(50, 50, 50);
    private static final Color PATH_COLOR = new Color(200, 200, 200);
    private static final Color PLAYER_COLOR = new Color(0, 120, 255);
    private static final Color DRAGON_COLOR = new Color(255, 50, 0);
    private static final Color FOG_COLOR = new Color(20, 20, 20);
    private static final Color GOAL_COLOR = new Color(0, 200, 0);
    
    public GamePanel(GameState gameState) {
        this.gameState = gameState;
        setFocusable(true);
        setBackground(Color.BLACK);
        
        // Add keyboard listener
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
        
        // Game update timer (move dragon, check collisions)
        gameTimer = new Timer(200, e -> {
            gameState.update();
            repaint();
            checkGameOver();
        });
        gameTimer.start();
    }
    
    private void handleKeyPress(KeyEvent e) {
        int key = e.getKeyCode();
        boolean moved = false;
        
        switch (key) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                moved = gameState.movePlayer(0, -1);
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                moved = gameState.movePlayer(0, 1);
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                moved = gameState.movePlayer(-1, 0);
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                moved = gameState.movePlayer(1, 0);
                break;
        }
        
        if (moved) {
            repaint();
        }
    }
    
    private void checkGameOver() {
        if (gameState.isGameWon()) {
            gameTimer.stop();
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Congratulations! You escaped the labyrinth!\n" +
                "Labyrinths solved: " + gameState.getPlayer().getLabyrinthsSolved() + "\n" +
                "Time: " + gameState.getElapsedTimeString() + "\n\n" +
                "Continue to next level?",
                "Level Complete!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
            );
            
            if (choice == JOptionPane.YES_OPTION) {
                gameState.startNewLevel();
                gameTimer.start();
                repaint();
            }
        } else if (gameState.isGameLost()) {
            gameTimer.stop();
            String name = JOptionPane.showInputDialog(
                this,
                "Game Over! The dragon caught you!\n" +
                "You solved " + gameState.getPlayer().getLabyrinthsSolved() + " labyrinths.\n\n" +
                "Enter your name for the high score:",
                "Game Over",
                JOptionPane.PLAIN_MESSAGE
            );
            
            if (name != null && !name.trim().isEmpty()) {
                HighScore.saveHighScore(name.trim(), 
                    gameState.getPlayer().getLabyrinthsSolved());
            }
            
            // Ask to restart
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Start a new game?",
                "Game Over",
                JOptionPane.YES_NO_OPTION
            );
            
            if (choice == JOptionPane.YES_OPTION) {
                gameState.initNewGame();
                gameTimer.start();
                repaint();
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        int[][] maze = gameState.getCurrentMaze();
        Position playerPos = gameState.getPlayer().getPosition();
        Position dragonPos = gameState.getDragon().getPosition();
        
        // Calculate offset to center the view on the player
        int offsetX = getWidth() / 2 - playerPos.getX() * CELL_SIZE - CELL_SIZE / 2;
        int offsetY = getHeight() / 2 - playerPos.getY() * CELL_SIZE - CELL_SIZE / 2;
        
        // Draw maze with fog of war
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[0].length; x++) {
                int screenX = offsetX + x * CELL_SIZE;
                int screenY = offsetY + y * CELL_SIZE;
                
                // Calculate distance from player
                double distance = Math.abs(x - playerPos.getX()) + 
                                Math.abs(y - playerPos.getY());
                
                if (distance <= VISIBILITY_RANGE) {
                    // Visible area
                    // Draw goal area
                    if (x >= maze[0].length - 3 && y <= 2) {
                        g2d.setColor(GOAL_COLOR);
                        g2d.fillRect(screenX, screenY, CELL_SIZE, CELL_SIZE);
                    }
                    
                    // Draw cell
                    if (maze[y][x] == 1) {
                        g2d.setColor(WALL_COLOR);
                    } else {
                        g2d.setColor(PATH_COLOR);
                    }
                    g2d.fillRect(screenX, screenY, CELL_SIZE, CELL_SIZE);
                    
                    // Draw grid lines
                    g2d.setColor(Color.GRAY);
                    g2d.drawRect(screenX, screenY, CELL_SIZE, CELL_SIZE);
                } else {
                    // Fog of war
                    g2d.setColor(FOG_COLOR);
                    g2d.fillRect(screenX, screenY, CELL_SIZE, CELL_SIZE);
                }
            }
        }
        
        // Draw dragon if visible
        double dragonDistance = Math.abs(dragonPos.getX() - playerPos.getX()) + 
                               Math.abs(dragonPos.getY() - playerPos.getY());
        if (dragonDistance <= VISIBILITY_RANGE) {
            int dragonScreenX = offsetX + dragonPos.getX() * CELL_SIZE;
            int dragonScreenY = offsetY + dragonPos.getY() * CELL_SIZE;
            
            g2d.setColor(DRAGON_COLOR);
            g2d.fillOval(dragonScreenX + 2, dragonScreenY + 2, 
                        CELL_SIZE - 4, CELL_SIZE - 4);
            
            // Dragon eyes
            g2d.setColor(Color.YELLOW);
            g2d.fillOval(dragonScreenX + 5, dragonScreenY + 6, 4, 4);
            g2d.fillOval(dragonScreenX + 11, dragonScreenY + 6, 4, 4);
        }
        
        // Draw player
        int playerScreenX = offsetX + playerPos.getX() * CELL_SIZE;
        int playerScreenY = offsetY + playerPos.getY() * CELL_SIZE;
        
        g2d.setColor(PLAYER_COLOR);
        g2d.fillRect(playerScreenX + 3, playerScreenY + 3, 
                    CELL_SIZE - 6, CELL_SIZE - 6);
        
        // Player face
        g2d.setColor(Color.WHITE);
        g2d.fillOval(playerScreenX + 6, playerScreenY + 7, 3, 3);
        g2d.fillOval(playerScreenX + 11, playerScreenY + 7, 3, 3);
        
        // Draw HUD
        drawHUD(g2d);
    }
    
    private void drawHUD(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Score
        g2d.drawString("Labyrinths Solved: " + 
                      gameState.getPlayer().getLabyrinthsSolved(), 10, 25);
        
        // Timer
        g2d.drawString("Time: " + gameState.getElapsedTimeString(), 10, 50);
        
        // Instructions
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString("Use WASD to move | Reach the green area (top-right)", 
                      10, getHeight() - 10);
        
        // Goal indicator
        g2d.setColor(GOAL_COLOR);
        g2d.fillRect(getWidth() - 120, 10, 15, 15);
        g2d.setColor(Color.WHITE);
        g2d.drawString("= Goal", getWidth() - 100, 22);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }
    
    public void stopTimer() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }
}
