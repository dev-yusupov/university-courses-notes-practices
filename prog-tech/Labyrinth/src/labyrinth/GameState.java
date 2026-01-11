package labyrinth;

import javax.swing.Timer;

/**
 * Manages the game state including current level, timer, and game status
 */
public class GameState {
    private int[][] currentMaze;
    private Player player;
    private Dragon dragon;
    private LabyrinthGenerator generator;
    private long startTime;
    private long elapsedTime;
    private boolean gameRunning;
    private boolean gameWon;
    private boolean gameLost;
    private Timer gameTimer;
    
    private static final int MAZE_WIDTH = 25;
    private static final int MAZE_HEIGHT = 25;
    
    public GameState() {
        this.generator = new LabyrinthGenerator();
        initNewGame();
    }
    
    public void initNewGame() {
        player = new Player(0, 0);
        player.resetScore();
        startNewLevel();
    }
    
    public void startNewLevel() {
        // Generate new maze
        currentMaze = generator.generateLabyrinth(MAZE_WIDTH, MAZE_HEIGHT);
        
        // Place player at bottom-left corner (find first path cell)
        for (int y = currentMaze.length - 1; y >= 0; y--) {
            for (int x = 0; x < currentMaze[0].length; x++) {
                if (currentMaze[y][x] == 0) {
                    player.setPosition(new Position(x, y));
                    player.setAlive(true);
                    break;
                }
            }
            if (player.getPosition().getX() > 0) break;
        }
        
        // Place dragon at random position far from player
        Position dragonPos = generator.findRandomPosition(
            currentMaze, player.getPosition(), 10.0);
        dragon = new Dragon(dragonPos.getX(), dragonPos.getY());
        
        // Reset timer
        startTime = System.currentTimeMillis();
        elapsedTime = 0;
        gameRunning = true;
        gameWon = false;
        gameLost = false;
    }
    
    public void update() {
        if (!gameRunning) return;
        
        // Update elapsed time
        elapsedTime = System.currentTimeMillis() - startTime;
        
        // Move dragon
        dragon.move(currentMaze);
        
        // Check if dragon caught player
        if (dragon.isNearPlayer(player.getPosition())) {
            gameLost = true;
            gameRunning = false;
            player.setAlive(false);
        }
        
        // Check if player reached goal (top-right area)
        Position playerPos = player.getPosition();
        if (playerPos.getX() >= currentMaze[0].length - 3 && 
            playerPos.getY() <= 2) {
            gameWon = true;
            gameRunning = false;
            player.incrementLabyrinthsSolved();
        }
    }
    
    public boolean movePlayer(int dx, int dy) {
        if (!gameRunning) return false;
        return player.move(dx, dy, currentMaze);
    }
    
    // Getters
    public int[][] getCurrentMaze() {
        return currentMaze;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public Dragon getDragon() {
        return dragon;
    }
    
    public long getElapsedTime() {
        return elapsedTime;
    }
    
    public boolean isGameRunning() {
        return gameRunning;
    }
    
    public boolean isGameWon() {
        return gameWon;
    }
    
    public boolean isGameLost() {
        return gameLost;
    }
    
    public String getElapsedTimeString() {
        long seconds = elapsedTime / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
