package labyrinth;

/**
 * Represents the player in the labyrinth game
 */
public class Player {
    private Position position;
    private boolean alive;
    private int labyrinths_solved;
    
    public Player(int startX, int startY) {
        this.position = new Position(startX, startY);
        this.alive = true;
        this.labyrinths_solved = 0;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    public boolean isAlive() {
        return alive;
    }
    
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    
    public int getLabyrinthsSolved() {
        return labyrinths_solved;
    }
    
    public void incrementLabyrinthsSolved() {
        this.labyrinths_solved++;
    }
    
    public void resetScore() {
        this.labyrinths_solved = 0;
    }
    
    public boolean move(int dx, int dy, int[][] maze) {
        int newX = position.getX() + dx;
        int newY = position.getY() + dy;
        
        // Check boundaries
        if (newX < 0 || newX >= maze[0].length || newY < 0 || newY >= maze.length) {
            return false;
        }
        
        // Check if not a wall (0 = path, 1 = wall)
        if (maze[newY][newX] == 0) {
            position.setX(newX);
            position.setY(newY);
            return true;
        }
        
        return false;
    }
}
