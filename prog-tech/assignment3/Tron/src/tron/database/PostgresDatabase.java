package tron.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgresDatabase implements Database {

    private static final String URL = "jdbc:postgresql://localhost:5432/tron";
    private static final String USER = "tron";
    private static final String PASS = "tron";

    public PostgresDatabase() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    @Override
    public void addWin(String playerName) {
        String sql = "INSERT INTO results (player_name, score) VALUES (?, 1) " +
                "ON CONFLICT (player_name) DO UPDATE SET score = results.score + 1";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, playerName);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<PlayerScore> getHighScores() {
        List<PlayerScore> scores = new ArrayList<>();
        String sql = "SELECT player_name, score FROM results ORDER BY score DESC LIMIT 10";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                scores.add(new PlayerScore(rs.getString("player_name"), rs.getInt("score")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scores;
    }
}
