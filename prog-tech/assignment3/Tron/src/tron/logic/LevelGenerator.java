package tron.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Generates levels (obstacles).
 */
public class LevelGenerator {

    private final int width;
    private final int height;
    private final Random random;

    public LevelGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        this.random = new Random();
    }

    /**
     * Generates a random set of obstacles (walls).
     * Ensures start positions are clear.
     */
    public List<Position> generateLevel(int levelIndex) {
        List<Position> obstacles = new ArrayList<>();

        // Simple border calls
        // Note: The game engine handles boundary checks, so explicit border walls are
        // optional but good for visual.
        // Let's rely on boundary checks for the edge of the world, and use this for
        // internal obstacles.

        // Generate some random blocks based on level index
        int obstacleCount = levelIndex * 5;

        for (int i = 0; i < obstacleCount; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            // Avoid start areas (assuming P1 starts top-leftish, P2 bottom-rightish)
            // Safety zone P1: (0-10, 0-10)
            // Safety zone P2: (width-10 - width, height-10 - height)
            if (x < 10 && y < 10)
                continue;
            if (x > width - 10 && y > height - 10)
                continue;

            obstacles.add(new Position(x, y));
        }

        return obstacles;
    }

    public List<Position> loadLevelFromFile(String filename) {
        // Placeholder for file loading if we add it later
        return Collections.emptyList();
    }
}
