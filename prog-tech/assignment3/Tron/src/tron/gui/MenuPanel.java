package tron.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MenuPanel extends JPanel {

    private final MainFrame mainFrame;
    private final JTextField p1NameField;
    private final JComboBox<ColorItem> p1ColorCombo;
    private final JTextField p2NameField;
    private final JComboBox<ColorItem> p2ColorCombo;

    public MenuPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridLayout(0, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        setPreferredSize(new Dimension(400, 350));

        JLabel title = new JLabel("TRON GAME", JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(24.0f));
        add(title);

        JPanel p1Panel = new JPanel(new GridLayout(2, 2));
        p1Panel.setBorder(BorderFactory.createTitledBorder("Player 1 (WASD)"));
        p1NameField = new JTextField("Player 1");
        p1ColorCombo = new JComboBox<>(getColorItems());
        p1ColorCombo.setSelectedIndex(0);
        p1Panel.add(new JLabel("Name:"));
        p1Panel.add(p1NameField);
        p1Panel.add(new JLabel("Color:"));
        p1Panel.add(p1ColorCombo);
        add(p1Panel);

        JPanel p2Panel = new JPanel(new GridLayout(2, 2));
        p2Panel.setBorder(BorderFactory.createTitledBorder("Player 2 (Arrows)"));
        p2NameField = new JTextField("Player 2");
        p2ColorCombo = new JComboBox<>(getColorItems());
        p2ColorCombo.setSelectedIndex(1);
        p2Panel.add(new JLabel("Name:"));
        p2Panel.add(p2NameField);
        p2Panel.add(new JLabel("Color:"));
        p2Panel.add(p2ColorCombo);
        add(p2Panel);

        JButton startBtn = new JButton("Start Game");
        startBtn.addActionListener(this::onStart);
        add(startBtn);

        JButton highscoreBtn = new JButton("Highscores");
        highscoreBtn.addActionListener(e -> mainFrame.showHighScores());
        add(highscoreBtn);

        JButton exitBtn = new JButton("Exit");
        exitBtn.addActionListener(e -> System.exit(0));
        add(exitBtn);
    }

    private void onStart(ActionEvent e) {
        String p1Name = p1NameField.getText().trim();
        String p2Name = p2NameField.getText().trim();

        if (p1Name.isEmpty() || p2Name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter names for both players.");
            return;
        }

        if (p1Name.equals(p2Name)) {
            JOptionPane.showMessageDialog(this, "Players must have different names.");
            return;
        }

        Color p1Color = ((ColorItem) p1ColorCombo.getSelectedItem()).color;
        Color p2Color = ((ColorItem) p2ColorCombo.getSelectedItem()).color;

        if (p1Color.equals(p2Color)) {
            JOptionPane.showMessageDialog(this, "Players must choose different colors.");
            return;
        }

        mainFrame.startGame(p1Name, p1Color, p2Name, p2Color);
    }

    private ColorItem[] getColorItems() {
        return new ColorItem[] {
                new ColorItem("Red", Color.RED),
                new ColorItem("Blue", Color.BLUE),
                new ColorItem("Green", Color.GREEN),
                new ColorItem("Yellow", Color.YELLOW),
                new ColorItem("Magenta", Color.MAGENTA),
                new ColorItem("Orange", Color.ORANGE),
                new ColorItem("Cyan", Color.CYAN)
        };
    }

    private record ColorItem(String name, Color color) {
        @Override
        public String toString() {
            return name;
        }
    }
}
