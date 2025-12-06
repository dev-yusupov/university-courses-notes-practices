package tron.logic;

import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import tron.db.Database;

/**
 * Manages the core game logic, including player states, collisions, and game
 * loop updates.
 */
public class GameModel {
    private final int width;
    private final int height;
    private final Motor player1;
    private final Motor player2;
    private final Set<Position> obstacles;
    private final Database database;
    private long startTime;
    private boolean gameOver;
    private String winnerName;

    public GameModel(int width, int height, String p1Name, Color p1Color, String p2Name, Color p2Color,
            List<Position> obstacles, Database db) {
        this.width = width;
        this.height = height;
        this.database = db;

        this.player1 = new Motor(p1Name, p1Color, new Position(5, height / 2), Direction.RIGHT);

        this.player2 = new Motor(p2Name, p2Color, new Position(width - 6, height / 2), Direction.LEFT);

        this.obstacles = new HashSet<>(obstacles);
        this.gameOver = false;
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Updates the game state by moving players and checking collisions.
     * Should be called in every game loop iteration.
     */
    public void update() {
        if (gameOver)
            return;

        player1.move();
        player2.move();

        checkCollisions();
    }

    /**
     * Checks for collisions for both players and determines the game outcome.
     */
    private void checkCollisions() {
        boolean p1Crashed = checkCrash(player1);
        boolean p2Crashed = checkCrash(player2);

        if (player1.getPosition().equals(player2.getPosition())) {
            p1Crashed = true;
            p2Crashed = true;
        }

        if (p1Crashed && p2Crashed) {
            gameOver = true;
            winnerName = "Draw";
        } else if (p1Crashed) {
            gameOver = true;
            winnerName = player2.getName();
            player1.setCrashed(true);
            database.addWin(player2.getName());
        } else if (p2Crashed) {
            gameOver = true;
            winnerName = player1.getName();
            player2.setCrashed(true);
            database.addWin(player1.getName());
        }
    }

    /**
     * Checks if a player has crashed into walls, obstacles, or trails.
     * 
     * @param player The player to check.
     * @return true if the player has crashed, false otherwise.
     */
    private boolean checkCrash(Motor player) {
        Position pos = player.getPosition();

        if (pos.x() < 0 || pos.x() >= width || pos.y() < 0 || pos.y() >= height) {
            return true;
        }

        if (obstacles.contains(pos)) {
            return true;
        }

        List<Position> p1Trail = player1.getTrail();
        for (int i = 0; i < p1Trail.size() - (player == player1 ? 1 : 0); i++) {
            if (pos.equals(p1Trail.get(i)))
                return true;
        }

        List<Position> p2Trail = player2.getTrail();
        for (int i = 0; i < p2Trail.size() - (player == player2 ? 1 : 0); i++) {
            if (pos.equals(p2Trail.get(i)))
                return true;
        }

        return false;
    }

    public Motor getPlayer1() {
        return player1;
    }

    public Motor getPlayer2() {
        return player2;
    }

    public Set<Position> getObstacles() {
        return obstacles;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public long getElapsedTimeSeconds() {
        if (gameOver)
            return 0;
        return (System.currentTimeMillis() - startTime) / 1000;
    }
}
