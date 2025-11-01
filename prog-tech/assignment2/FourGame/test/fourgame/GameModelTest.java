package fourgame;

import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;

public class GameModelTest {

    @Test
    public void testInitialState() {
        GameModel model = new GameModel(3);

        assertEquals(3, model.size);
        assertFalse(model.isGameOver());
        assertEquals(0, model.redScore);
        assertEquals(0, model.blueScore);
        assertTrue(model.getPlayerOneTurn());

        for (int[] row : model.values) {
            for (int v : row) {
                assertEquals(0, v);
            }
        }
    }

    @Test
    public void testIncrementCenterAffectsNeighbors() {
        GameModel model = new GameModel(3);

        model.increment(1,1);

        // center + 4 neighbors should be 1
        assertEquals(1, model.values[1][1]); // center
        assertEquals(1, model.values[0][1]); // up
        assertEquals(1, model.values[2][1]); // down
        assertEquals(1, model.values[1][0]); // left
        assertEquals(1, model.values[1][2]); // right
    }

    @Test
    public void testIncrementEdgeAffectsValidCellsOnly() {
        GameModel model = new GameModel(3);

        model.increment(0,0); // top-left corner

        assertEquals(1, model.values[0][0]);
        assertEquals(1, model.values[1][0]);
        assertEquals(1, model.values[0][1]);

        // ensure no invalid access increment
        assertEquals(0, model.values[2][2]);
    }

    @Test
    public void testScoreIncreasesWhenValueReaches4ForRed() {
        GameModel model = new GameModel(3);

        // player one turn (red)
        for(int i=0;i<4;i++) {
            model.increment(1,1);
        }

        assertEquals(4, model.values[1][1]);
        assertEquals(1, model.redScore);
        assertEquals(0, model.blueScore);
        assertEquals(1, model.owner[1][1]);
    }

    @Test
    public void testScoreIncreasesWhenValueReaches4ForBlue() {
        GameModel model = new GameModel(3);

        model.setPlayerOneTurn(false);

        for(int i=0;i<4;i++) {
            model.increment(1,1);
        }

        assertEquals(4, model.values[1][1]);
        assertEquals(0, model.redScore);
        assertEquals(1, model.blueScore);
        assertEquals(2, model.owner[1][1]);
    }

    @Test
    public void testNextPlayerSwitchesTurn() {
        GameModel model = new GameModel(3);
        assertTrue(model.getPlayerOneTurn());
        model.nextPlayer();
        assertFalse(model.getPlayerOneTurn());
        model.nextPlayer();
        assertTrue(model.getPlayerOneTurn());
    }

    @Test
    public void testGameNotOverInitially() {
        GameModel model = new GameModel(3);
        assertFalse(model.isGameOver());
    }

    @Test
    public void testGameOverWhenAllAre4() {
        GameModel model = new GameModel(2);

        // fill 2x2 grid to 4
        for (int r = 0; r < 2; r++) {
            for (int c = 0; c < 2; c++) {
                for(int i = 0; i < 4; i++) {
                    model.increment(r, c);
                }
            }
        }

        assertTrue(model.isGameOver());
    }

    @Test
    public void testIncrementDoesNotIncreaseBeyond4() {
        GameModel model = new GameModel(3);

        for(int i=0; i<10; i++) {
            model.increment(1,1);
        }

        assertEquals(4, model.values[1][1]);
        assertEquals(1, model.redScore);
    }
    
    @Test
    public void testGameEndsTriggersRestart() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            TestGameFrame frame = new TestGameFrame(3);

            // Make board full
            for (int r = 0; r < frame.getModel().size; r++) {
                for (int c = 0; c < frame.getModel().size; c++) {
                    frame.getModel().values[r][c] = 4;
                }
            }

            JButton someButton = (JButton) frame.getContentPane().getComponent(0);
            someButton.doClick();

            assertTrue(frame.restartCalled);
        });
    }

}
