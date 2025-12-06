package tron;

import tron.gui.MainFrame;

/**
 * Entry point for the Tron Game application.
 */
public class Main {

    /**
     * The main method that launches the application.
     * 
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }

}
