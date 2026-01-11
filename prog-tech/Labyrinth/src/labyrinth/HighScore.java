package labyrinth;

import java.util.List;

/**
 * Manages high scores with persistence to database
 */
public class HighScore implements Comparable<HighScore> {
    private String playerName;
    private int score;
    
    public HighScore(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public int getScore() {
        return score;
    }
    
    @Override
    public int compareTo(HighScore other) {
        // Sort in descending order (higher scores first)
        return Integer.compare(other.score, this.score);
    }
    
    @Override
    public String toString() {
        return playerName + ": " + score + " labyrinths";
    }
    
    /**
     * Save a high score to the database
     */
    public static void saveHighScore(String playerName, int score) {
        DatabaseManager db = DatabaseManager.getInstance();
        boolean success = db.saveHighScore(playerName, score);
        
        if (success) {
            System.out.println("High score saved to database: " + playerName + " - " + score);
        } else {
            System.err.println("Failed to save high score to database");
        }
    }
    
    /**
     * Load high scores from database
     */
    public static List<HighScore> loadHighScores() {
        DatabaseManager db = DatabaseManager.getInstance();
        return db.getAllScores();
    }
    
    /**
     * Get top 10 high scores from database
     */
    public static List<HighScore> getTop10() {
        DatabaseManager db = DatabaseManager.getInstance();
        return db.getTopScores(10);
    }
}
