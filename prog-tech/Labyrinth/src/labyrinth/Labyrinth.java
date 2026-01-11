/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package labyrinth;

import javax.swing.SwingUtilities;

/**
 * Main class for the Labyrinth Game
 * 
 * Objective: Escape from the labyrinth by reaching the top-right corner
 * while avoiding the dragon. The player starts at the bottom-left corner.
 * 
 * @author User
 */
public class Labyrinth {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Launch the game on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            GameFrame frame = new GameFrame();
            frame.setVisible(true);
        });
    }
}
