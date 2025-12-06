package tron.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * File-based implementation of the Database interface.
 * Stores data in a simple text file: "Name:Wins"
 */
public class FileDatabase implements Database {

    private static final String DB_FILE = "tron_db.txt";
    private final Map<String, Integer> cache = new HashMap<>();

    public FileDatabase() {
        load();
    }

    private void load() {
        File file = new File(DB_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    try {
                        int wins = Integer.parseInt(parts[1].trim());
                        cache.put(name, wins);
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping invalid line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DB_FILE))) {
            for (Map.Entry<String, Integer> entry : cache.entrySet()) {
                bw.write(entry.getKey() + ":" + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addWin(String playerName) {
        cache.put(playerName, cache.getOrDefault(playerName, 0) + 1);
        save();
    }

    @Override
    public List<PlayerScore> getHighScores() {
        List<PlayerScore> scores = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : cache.entrySet()) {
            scores.add(new PlayerScore(entry.getKey(), entry.getValue()));
        }
        Collections.sort(scores);
        if (scores.size() > 10) {
            return scores.subList(0, 10);
        }
        return scores;
    }
}
