package fourgame;

/**
 * Represents the game logic and state for the Four Game.
 * This class manages the game board, scores, player turns, and game rules.
 * The game is played on an n x n grid where players compete to reach the value of 4
 * on as many fields as possible by incrementing adjacent cells.
 */
public class GameModel {
    private final int size;
    private int[][] values;
    private int[][] owner;
    private int redScore = 0;
    private int blueScore = 0;
    private boolean playerOneTurn = true;
    
    /**
     * Constructs a new GameModel with the specified board size.
     * Initializes the game board with all values set to 0.
     * 
     * @param size the dimension of the square board (3, 5, or 7)
     */
    public GameModel(int size) {
        this.size = size;
        values = new int[size][size];
        owner = new int[size][size];
    }
    
    // Getters
    public int getSize() {
        return size;
    }
    
    public int[][] getValues() {
        return values;
    }
    
    public int getValue(int row, int col) {
        return values[row][col];
    }
    
    public int[][] getOwner() {
        return owner;
    }
    
    public int getOwnerAt(int row, int col) {
        return owner[row][col];
    }
    
    public int getRedScore() {
        return redScore;
    }
    
    public int getBlueScore() {
        return blueScore;
    }
    
    public boolean getPlayerOneTurn() {
        return playerOneTurn;
    }
    
    // Setters
    public void setPlayerOneTurn(boolean val) {
        playerOneTurn = val;
    }
    
    public void setValue(int row, int col, int value) {
        values[row][col] = value;
    }
    
    public void setOwner(int row, int col, int owner) {
        this.owner[row][col] = owner;
    }
    
    /**
     * Increments the value of the specified field and its neighbors.
     * The clicked field and all adjacent fields (up, down, left, right) are incremented by 1,
     * but only if their current value is less than 4.
     * If a field reaches 4, it is assigned to the current player and their score is incremented.
     * 
     * @param r the row index of the clicked field
     * @param c the column index of the clicked field
     */
    public void increment(int r, int c) {
        int[][] dirs = {{0,0},{1,0},{-1,0},{0,1},{0,-1}};

        for (int[] d : dirs) {
            int nr = r + d[0];
            int nc = c + d[1];

            if (nr >= 0 && nr < size && nc >= 0 && nc < size) {
                if (values[nr][nc] < 4) {
                    values[nr][nc]++;
                    if (values[nr][nc] == 4 && nr == r && nc == c) {
                        if (playerOneTurn) redScore++;
                        else blueScore++;

                        owner[nr][nc] = playerOneTurn ? 1 : 2;
                    }
                }
            }
        }
    }
    
    /**
     * Switches the turn to the next player.
     * Toggles between player one (red) and player two (blue).
     */
    public void nextPlayer() {
        playerOneTurn = !playerOneTurn;
    }

    /**
     * Checks if the game has ended.
     * The game is over when all fields on the board have reached the value of 4.
     * 
     * @return true if all fields are 4, false otherwise
     */
    public boolean isGameOver() {
        for (int[] row : values) {
            for (int v : row) {
                if (v < 4) return false;
            }
        }
        return true;
    }
}
