package tron.gui;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import tron.db.Database;
import tron.db.PlayerScore;

/**
 * The high score panel that displays the top 10 player scores.
 */
public class HighScorePanel extends JPanel {

    private final MainFrame mainFrame;
    private final Database database;
    private final JTable table;
    private final DefaultTableModel tableModel;

    public HighScorePanel(MainFrame mainFrame, Database database) {
        this.mainFrame = mainFrame;
        this.database = database;
        setLayout(new BorderLayout());

        JLabel title = new JLabel("High Scores", JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(20.0f));
        add(title, BorderLayout.NORTH);

        String[] columnNames = { "Rank", "Player Name", "Wins" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton backBtn = new JButton("Back to Menu");
        backBtn.addActionListener(e -> mainFrame.showMenu());
        add(backBtn, BorderLayout.SOUTH);
    }

    /**
     * Refreshes the high score table by fetching the latest data from the database.
     */
    public void refresh() {
        tableModel.setRowCount(0);
        List<PlayerScore> scores = database.getHighScores();
        int rank = 1;
        for (PlayerScore score : scores) {
            tableModel.addRow(new Object[] { rank++, score.name(), score.wins() });
        }
    }
}
