package tron.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LevelGenerator {

    private final int width;
    private final int height;
    private final Random random;

    public LevelGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        this.random = new Random();
    }

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

    public List<Position> loadLevelFromFile(String filename) {
        return Collections.emptyList();
    }
}
