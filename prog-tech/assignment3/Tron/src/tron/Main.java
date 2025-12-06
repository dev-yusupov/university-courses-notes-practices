package tron;

import tron.gui.MainFrame;

/**
 *
 * @author User
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }

}
