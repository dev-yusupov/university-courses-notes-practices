package tron.db;

/**
 * Represents a player's score entry (name and number of wins).
 * 
 * @param name The player's name.
 * @param wins The number of wins the player has accumulated.
 */
public record PlayerScore(String name, int wins) implements Comparable<PlayerScore> {
    @Override
    public int compareTo(PlayerScore o) {
        return Integer.compare(o.wins, this.wins);
    }

    @Override
    public String toString() {
        return name + ": " + wins;
    }
}
