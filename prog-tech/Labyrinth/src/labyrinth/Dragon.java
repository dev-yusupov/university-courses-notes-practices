package labyrinth;

import java.util.Random;

/**
 * Represents the dragon enemy in the labyrinth game
 */
public class Dragon {
    private Position position;
    private int direction; // 0=up, 1=right, 2=down, 3=left
    private Random random;
    private static final int[] DX = {0, 1, 0, -1};
    private static final int[] DY = {-1, 0, 1, 0};
    
    public Dragon(int startX, int startY) {
        this.position = new Position(startX, startY);
        this.random = new Random();
        this.direction = random.nextInt(4);
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    /**
     * Move the dragon in its current direction until it hits a wall,
     * then choose a new random direction
     */
    public void move(int[][] maze) {
        int newX = position.getX() + DX[direction];
        int newY = position.getY() + DY[direction];
        
        // Check if can move in current direction
        if (newX >= 0 && newX < maze[0].length && 
            newY >= 0 && newY < maze.length && 
            maze[newY][newX] == 0) {
            // Move in current direction
            position.setX(newX);
            position.setY(newY);
        } else {
            // Hit a wall, choose new direction
            changeDirection(maze);
        }
    }
    
    private void changeDirection(int[][] maze) {
        // Try to find a valid direction
        int attempts = 0;
        while (attempts < 4) {
            int newDirection = random.nextInt(4);
            int newX = position.getX() + DX[newDirection];
            int newY = position.getY() + DY[newDirection];
            
            if (newX >= 0 && newX < maze[0].length && 
                newY >= 0 && newY < maze.length && 
                maze[newY][newX] == 0) {
                direction = newDirection;
                return;
            }
            attempts++;
        }
    }
    
    public boolean isNearPlayer(Position playerPos) {
        // Check if dragon is in neighboring field (distance <= 1)
        return position.distanceTo(playerPos) <= 1.5;
    }
}
