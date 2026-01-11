package labyrinth;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages PostgreSQL database operations for the game
 */
public class DatabaseManager {
    // PostgreSQL connection settings
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/labyrinthdb";
    private static final String DB_USER = "labyrinth";
    private static final String DB_PASSWORD = "labyrinth123";
    private Connection connection;
    
    public DatabaseManager() {
        initializeDatabase();
    }
    
    /**
     * Initialize database connection and create tables if they don't exist
     */
    private void initializeDatabase() {
        try {
            // Load PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");
            
            // Create connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Create high_scores table
            String createTableSQL = 
                "CREATE TABLE IF NOT EXISTS high_scores (" +
                "id SERIAL PRIMARY KEY, " +
                "player_name VARCHAR(100) NOT NULL, " +
                "score INTEGER NOT NULL, " +
                "date_achieved TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
            
            Statement stmt = connection.createStatement();
            stmt.execute(createTableSQL);
            stmt.close();
            
            System.out.println("PostgreSQL Database initialized successfully");
            System.out.println("Connected to: " + DB_URL);
            
        } catch (ClassNotFoundException e) {
            System.err.println("WARNING: PostgreSQL JDBC driver not found!");
            System.err.println("High scores will not be saved.");
            System.err.println("Please add lib/postgresql-42.7.1.jar to Libraries in NetBeans project properties.");
            e.printStackTrace();
            // Don't throw - let game continue without database
        } catch (SQLException e) {
            System.err.println("WARNING: Database connection error: " + e.getMessage());
            System.err.println("Make sure PostgreSQL container is running:");
            System.err.println("  docker ps | grep labyrinth-db");
            System.err.println("High scores will not be saved.");
            e.printStackTrace();
            // Don't throw - let game continue without database
        }
    }
    
    /**
     * Save a high score to the database
     */
    public boolean saveHighScore(String playerName, int score) {
        if (connection == null) {
            System.err.println("Database not available - score not saved");
            return false;
        }
        
        String insertSQL = "INSERT INTO high_scores (player_name, score) VALUES (?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, playerName);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error saving high score: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get top N high scores from the database
     */
    public List<HighScore> getTopScores(int limit) {
        List<HighScore> scores = new ArrayList<>();
        
        if (connection == null) {
            return scores; // Return empty list if no database
        }
        
        String querySQL = 
            "SELECT player_name, score FROM high_scores " +
            "ORDER BY score DESC, date_achieved ASC LIMIT ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(querySQL)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String playerName = rs.getString("player_name");
                int score = rs.getInt("score");
                scores.add(new HighScore(playerName, score));
            }
            
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error loading high scores: " + e.getMessage());
            e.printStackTrace();
        }
        
        return scores;
    }
    
    /**
     * Get all high scores
     */
    public List<HighScore> getAllScores() {
        List<HighScore> scores = new ArrayList<>();
        
        if (connection == null) {
            return scores; // Return empty list if no database
        }
        
        String querySQL = 
            "SELECT player_name, score FROM high_scores " +
            "ORDER BY score DESC, date_achieved ASC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL)) {
            
            while (rs.next()) {
                String playerName = rs.getString("player_name");
                int score = rs.getInt("score");
                scores.add(new HighScore(playerName, score));
            }
            
        } catch (SQLException e) {
            System.err.println("Error loading all scores: " + e.getMessage());
            e.printStackTrace();
        }
        
        return scores;
    }
    
    /**
     * Get the count of high scores in database
     */
    public int getScoreCount() {
        String countSQL = "SELECT COUNT(*) as count FROM high_scores";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(countSQL)) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting scores: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Clear all high scores (for testing or reset)
     */
    public boolean clearAllScores() {
        String deleteSQL = "DELETE FROM high_scores";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(deleteSQL);
            return true;
        } catch (SQLException e) {
            System.err.println("Error clearing scores: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Close database connection
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Get singleton instance
     */
    private static DatabaseManager instance;
    
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
}
