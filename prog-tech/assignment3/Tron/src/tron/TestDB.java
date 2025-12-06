package tron;

import tron.database.PostgresDatabase;

public class TestDB {
    public static void main(String[] args) {
        System.out.println("Testing DB Connection...");
        try {
            PostgresDatabase db = new PostgresDatabase();
            System.out.println("Connected.");

            System.out.println("Adding win for VerificationUser...");
            db.addWin("VerificationUser");

            System.out.println("Fetching Highscores:");
            System.out.println(db.getHighScores());

            System.out.println("Test Complete.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
