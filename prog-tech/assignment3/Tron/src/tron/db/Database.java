package tron.database;

import java.util.List;

/**
 * Interface for database operations.
 */
public interface Database {
    /**
     * Increases the win count for the specified player.
     * If the player does not exist, they are created with 1 win.
     * 
     * @param playerName Name of the winner
     */
    void addWin(String playerName);

    /**
     * Retrieves the top 10 players by win count.
     * 
     * @return List of PlayerScore objects
     */
    List<PlayerScore> getHighScores();
}
