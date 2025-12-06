package tron.db;

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
