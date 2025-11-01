package fourgame;

public class GameModel {
    public final int size;
    public int[][] values;
    public int[][] owner;
    public int redScore = 0;
    public int blueScore = 0;
    public boolean playerOneTurn = true;
    
    public GameModel(int size) {
        this.size = size;
        values = new int[size][size];
        owner = new int[size][size];
    }
    
    public void increment(int r, int c) {
        int[][] dirs = {{0,0},{1,0},{-1,0},{0,1},{0,-1}};

        for (int[] d : dirs) {
            int nr = r + d[0];
            int nc = c + d[1];

            if (nr >= 0 && nr < size && nc >= 0 && nc < size) {
                if (values[nr][nc] < 4) {
                    values[nr][nc]++;
                    if (values[nr][nc] == 4) {
                        if (playerOneTurn) redScore++;
                        else blueScore++;

                        owner[nr][nc] = playerOneTurn ? 1 : 2;
                    }
                }
            }
        }
    }
    
    public void nextPlayer() {
        playerOneTurn = !playerOneTurn;
    }

    public boolean isGameOver() {
        for (int[] row : values) {
            for (int v : row) {
                if (v < 4) return false;
            }
        }
        return true;
    }
}
