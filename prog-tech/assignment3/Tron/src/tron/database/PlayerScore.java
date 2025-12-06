package tron.database;

/**
 * Represents a player's score.
 */
public record PlayerScore(String name, int wins) implements Comparable<PlayerScore> {
    @Override
    public int compareTo(PlayerScore o) {
        // Descending order by wins
        return Integer.compare(o.wins, this.wins);
    }

    @Override
    public String toString() {
        return name + ": " + wins;
    }
}
