package tron.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Generates obstacles for different game levels.
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
     * Generates a list of obstacle positions for the given level index.
     * The number of obstacles increases with the level index.
     * 
     * @param levelIndex The current level number.
     * @return A list of Position objects representing obstacles.
     */
    public List<Position> generateLevel(int levelIndex) {
        List<Position> obstacles = new ArrayList<>();

        int obstacleCount = levelIndex * 5;

        while (obstacles.size() < obstacleCount) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            if (x < 10 && y < 10)
                continue;
            if (x > width - 10 && y > height - 10)
                continue;

            obstacles.add(new Position(x, y));
        }

        return obstacles;
    }

    /**
     * Loads a custom level configuration from a file.
     * 
     * @param filename The path to the level file.
     * @return A list of Position objects representing obstacles loaded from the
     *         file.
     */
    public List<Position> loadLevelFromFile(String filename) {
        return Collections.emptyList();
    }
}
