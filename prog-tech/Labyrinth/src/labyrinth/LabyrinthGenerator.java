package labyrinth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

/**
 * Generates playable labyrinths using depth-first search algorithm
 */
public class LabyrinthGenerator {
    private static final int WALL = 1;
    private static final int PATH = 0;
    private Random random;
    
    public LabyrinthGenerator() {
        this.random = new Random();
    }
    
    /**
     * Generate a labyrinth with guaranteed path from bottom-left to top-right
     */
    public int[][] generateLabyrinth(int width, int height) {
        // Ensure odd dimensions for proper maze generation
        int mazeWidth = (width % 2 == 0) ? width + 1 : width;
        int mazeHeight = (height % 2 == 0) ? height + 1 : height;
        
        int[][] maze = new int[mazeHeight][mazeWidth];
        
        // Initialize all as walls
        for (int y = 0; y < mazeHeight; y++) {
            for (int x = 0; x < mazeWidth; x++) {
                maze[y][x] = WALL;
            }
        }
        
        // Generate maze using recursive backtracking
        generateMaze(maze, 1, 1);
        
        // Ensure start and end are paths
        maze[mazeHeight - 2][1] = PATH; // Bottom-left area
        maze[1][mazeWidth - 2] = PATH;  // Top-right area
        
        return maze;
    }
    
    private void generateMaze(int[][] maze, int x, int y) {
        maze[y][x] = PATH;
        
        // Create list of directions
        ArrayList<Integer> directions = new ArrayList<>();
        directions.add(0); // up
        directions.add(1); // right
        directions.add(2); // down
        directions.add(3); // left
        Collections.shuffle(directions, random);
        
        for (int dir : directions) {
            int nx = x;
            int ny = y;
            
            switch (dir) {
                case 0: ny -= 2; break; // up
                case 1: nx += 2; break; // right
                case 2: ny += 2; break; // down
                case 3: nx -= 2; break; // left
            }
            
            if (nx > 0 && nx < maze[0].length - 1 && 
                ny > 0 && ny < maze.length - 1 && 
                maze[ny][nx] == WALL) {
                
                // Carve path
                maze[y + (ny - y) / 2][x + (nx - x) / 2] = PATH;
                generateMaze(maze, nx, ny);
            }
        }
    }
    
    /**
     * Find a random valid position in the maze (not on walls or near edges)
     */
    public Position findRandomPosition(int[][] maze, Position avoid, double minDistance) {
        int maxAttempts = 100;
        int attempts = 0;
        
        while (attempts < maxAttempts) {
            int x = random.nextInt(maze[0].length - 4) + 2;
            int y = random.nextInt(maze.length - 4) + 2;
            
            if (maze[y][x] == PATH) {
                Position pos = new Position(x, y);
                if (avoid == null || pos.distanceTo(avoid) >= minDistance) {
                    return pos;
                }
            }
            attempts++;
        }
        
        // Fallback: return center of maze
        return new Position(maze[0].length / 2, maze.length / 2);
    }
}
