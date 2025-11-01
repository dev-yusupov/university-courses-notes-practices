package fourgame;

import java.awt.Frame;
import org.junit.*;
import javax.swing.*;

public class MainTest {

    @Before
    public void setup() {
        Main.testMode = true;           // no dialogs
    }

    @After
    public void cleanup() {
        Main.testBoardSize = null;
        Main.testMode = false;
    }

    @Test
    public void testStartGameWith3x3() throws Exception {
        Main.testBoardSize = 3;

        SwingUtilities.invokeAndWait(Main::startGame);

        JFrame active = getActiveFrame();
        Assert.assertNotNull(active);
        Assert.assertEquals(3, ((GameFrame) active).getModel().size);

        active.dispose();
    }

    @Test
    public void testStartGameWith7x7() throws Exception {
        Main.testBoardSize = 7;

        SwingUtilities.invokeAndWait(Main::startGame);

        JFrame active = getActiveFrame();
        Assert.assertNotNull(active);
        Assert.assertEquals(7, ((GameFrame) active).getModel().size);

        active.dispose();
    }

    /** Helper to get currently open GameFrame */
    private JFrame getActiveFrame() {
        for (Frame f : JFrame.getFrames()) {
            if (f.isVisible() && f instanceof GameFrame) return (JFrame) f;
        }
        return null;
    }
}
