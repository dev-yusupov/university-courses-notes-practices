package tron.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import tron.database.Database;
import tron.logic.Direction;
import tron.logic.GameModel;
import tron.logic.LevelGenerator;
import tron.logic.Motor;
import tron.logic.Position;

public class GamePanel extends JPanel implements ActionListener {

    private static final int CELL_SIZE = 10;
    private static final int GRID_WIDTH = 60;
    private static final int GRID_HEIGHT = 40;

    private final MainFrame mainFrame;
    private final Database database;
    private GameModel gameModel;
    private Timer timer;
    private final LevelGenerator levelGenerator;
    private int currentLevel = 1; // Unused for now, but good for structure

    public GamePanel(MainFrame mainFrame, Database database) {
        this.mainFrame = mainFrame;
        this.database = database;
        this.levelGenerator = new LevelGenerator(GRID_WIDTH, GRID_HEIGHT);

        setPreferredSize(new Dimension(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE + 30)); // +30 for info bar
        setBackground(Color.BLACK);

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameModel == null || gameModel.isGameOver())
                    return;

                switch (e.getKeyCode()) {
                    // P1 (WASD)
                    case KeyEvent.VK_W -> gameModel.getPlayer1().setDirection(Direction.UP);
                    case KeyEvent.VK_S -> gameModel.getPlayer1().setDirection(Direction.DOWN);
                    case KeyEvent.VK_A -> gameModel.getPlayer1().setDirection(Direction.LEFT);
                    case KeyEvent.VK_D -> gameModel.getPlayer1().setDirection(Direction.RIGHT);

                    // P2 (Arrows)
                    case KeyEvent.VK_UP -> gameModel.getPlayer2().setDirection(Direction.UP);
                    case KeyEvent.VK_DOWN -> gameModel.getPlayer2().setDirection(Direction.DOWN);
                    case KeyEvent.VK_LEFT -> gameModel.getPlayer2().setDirection(Direction.LEFT);
                    case KeyEvent.VK_RIGHT -> gameModel.getPlayer2().setDirection(Direction.RIGHT);

                    case KeyEvent.VK_ESCAPE -> {
                        timer.stop();
                        mainFrame.showMenu();
                    }
                }
            }
        });

        // 100ms or so tick.
        timer = new Timer(100, this);
    }

    public void startNewGame(String p1Name, Color p1Color, String p2Name, Color p2Color) {
        // Generate Level
        List<Position> obstacles = levelGenerator.generateLevel(currentLevel);

        gameModel = new GameModel(GRID_WIDTH, GRID_HEIGHT, p1Name, p1Color, p2Name, p2Color, obstacles, database);
        timer.start();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameModel != null && !gameModel.isGameOver()) {
            gameModel.update();

            if (gameModel.isGameOver()) {
                timer.stop();
                repaint();
                JOptionPane.showMessageDialog(this,
                        "Game Over! Winner: " + gameModel.getWinnerName(),
                        "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
                mainFrame.showMenu();
            } else {
                repaint();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameModel == null)
            return;

        // Draw Info Bar (Time)
        g.setColor(Color.WHITE);
        g.drawString("Time: " + gameModel.getElapsedTimeSeconds() + "s", 10, GRID_HEIGHT * CELL_SIZE + 20);
        g.drawString(gameModel.getPlayer1().getName(), 100, GRID_HEIGHT * CELL_SIZE + 20);
        g.drawString(gameModel.getPlayer2().getName(), 300, GRID_HEIGHT * CELL_SIZE + 20);

        // Draw Obstacles
        g.setColor(Color.GRAY);
        for (Position pos : gameModel.getObstacles()) {
            g.fillRect(pos.x() * CELL_SIZE, pos.y() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        // Draw Players
        drawPlayer(g, gameModel.getPlayer1());
        drawPlayer(g, gameModel.getPlayer2());
    }

    private void drawPlayer(Graphics g, Motor player) {
        g.setColor(player.getColor());

        // Draw Trail
        for (Position pos : player.getTrail()) {
            g.fillRect(pos.x() * CELL_SIZE, pos.y() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        // Head (slightly brighter or just same?)
        // Let's make head specific
        Position head = player.getPosition();
        g.setColor(player.getColor().brighter());
        g.fillRect(head.x() * CELL_SIZE, head.y() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }
}
