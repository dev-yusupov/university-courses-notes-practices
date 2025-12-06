package tron.logic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.Test;
import static org.junit.Assert.*;
import tron.db.Database;

public class GameModelTest {

    // Stub class to avoid real DB connection
    private static class DatabaseStub extends Database {
        String lastWinner = null;

        @Override
        public void addWin(String playerName) {
            lastWinner = playerName;
        }
    }

    @Test
    public void testInitialization() {
        DatabaseStub db = new DatabaseStub();
        GameModel model = new GameModel(60, 40, "P1", Color.RED, "P2", Color.BLUE, new ArrayList<>(), db);

        assertNotNull(model.getPlayer1());
        assertNotNull(model.getPlayer2());
        assertFalse(model.isGameOver());

        // Check start positions
        // P1: (5, 20)
        // P2: (60-6, 20) = (54, 20)
        assertEquals(new Position(5, 20), model.getPlayer1().getPosition());
        assertEquals(new Position(54, 20), model.getPlayer2().getPosition());
    }

    @Test
    public void testUpdateMovesPlayers() {
        DatabaseStub db = new DatabaseStub();
        GameModel model = new GameModel(60, 40, "P1", Color.RED, "P2", Color.BLUE, new ArrayList<>(), db);

        // P1 moves RIGHT, P2 moves LEFT
        model.update();

        assertEquals(new Position(6, 20), model.getPlayer1().getPosition());
        assertEquals(new Position(53, 20), model.getPlayer2().getPosition());
    }

    @Test
    public void testWallCollision() {
        DatabaseStub db = new DatabaseStub();
        // Create small board to hit wall quickly
        // P1 at (5, 20), moving right. Width 7 means P1 is at 5, next is 6 (valid),
        // next is 7 (crash)
        // P2 at (1, 20) in 7-width board.
        GameModel model = new GameModel(7, 40, "P1", Color.RED, "P2", Color.BLUE, new ArrayList<>(), db);

        // Turn P2 UP so it doesn't hit the left wall simultaneously
        model.getPlayer2().setDirection(Direction.UP);

        model.update(); // P1 -> 6 (valid last index is 6). P2 moves UP.
        assertFalse(model.isGameOver());

        model.update(); // P1 -> 7 (out of bounds). P2 moves UP.
        assertTrue(model.isGameOver());

        // If P1 crashed, P2 (who didn't crash) wins
        assertEquals("P2", model.getWinnerName());
        assertEquals("P2", db.lastWinner);
    }

    @Test
    public void testHeadOnCollision() {
        DatabaseStub db = new DatabaseStub();
        // Place players close to each other
        // P1 at (5, 20) RIGHT
        // P2 at (width-6, 20) LEFT
        // Width = 13. P1 at 5. P2 at 13-6 = 7.
        // Step 1: P1->6, P2->6. BOOM.
        GameModel model = new GameModel(13, 40, "P1", Color.RED, "P2", Color.BLUE, new ArrayList<>(), db);

        model.update(); // Both move to (6, 20)

        assertTrue(model.isGameOver());
        assertEquals("Draw", model.getWinnerName());
    }
}
