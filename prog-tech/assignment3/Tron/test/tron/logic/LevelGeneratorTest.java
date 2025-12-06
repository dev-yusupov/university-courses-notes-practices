package tron.logic;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class LevelGeneratorTest {

    @Test
    public void testGenerateLevelCount() {
        LevelGenerator generator = new LevelGenerator(60, 40);
        List<Position> level1 = generator.generateLevel(1);

        // Level 1 * 5 = 5 obstacles
        assertEquals(5, level1.size());

        List<Position> level2 = generator.generateLevel(2);
        // Level 2 * 5 = 10 obstacles
        assertEquals(10, level2.size());
    }

    @Test
    public void testSafetyZones() {
        int width = 60;
        int height = 40;
        LevelGenerator generator = new LevelGenerator(width, height);
        // Generate a lot of obstacles to increase chance of hitting bad spots if logic
        // was wrong
        List<Position> obstacles = generator.generateLevel(100);

        for (Position pos : obstacles) {
            // Check bounds
            assertTrue(pos.x() >= 0 && pos.x() < width);
            assertTrue(pos.y() >= 0 && pos.y() < height);

            // Check P1 safety zone (0-10, 0-10)
            assertFalse("Obstacle in P1 safety zone", pos.x() < 10 && pos.y() < 10);

            // Check P2 safety zone (width-10 to width, height-10 to height)
            assertFalse("Obstacle in P2 safety zone", pos.x() > width - 10 && pos.y() > height - 10);
        }
    }
}
