package fourgame;

import org.junit.*;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;

public class GameFrameTest {

    private GameFrame testFrame;

    /** Test subclass to stop restart loop */
    

    @Before
    public void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            testFrame = new TestGameFrame(3);
        });
    }

    @After
    public void tearDown() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            testFrame.dispose();
        });
    }

    @Test
    public void testFrameCreated() {
        assertNotNull(testFrame);
        assertEquals("Reach 4 Game", testFrame.getTitle());
        assertTrue(testFrame.isDisplayable());
    }

    @Test
    public void testGridButtonsExist() {
        int componentCount = testFrame.getContentPane().getComponentCount();
        assertEquals(9, componentCount); // 3x3 grid
    }

    @Test
    public void testButtonClickUpdatesValue() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JButton btn = (JButton) testFrame.getContentPane().getComponent(4); // center cell
            assertEquals("0", btn.getText());

            btn.doClick();  // simulate click

            assertEquals("1", btn.getText());
        });
    }

    @Test
    public void testMultipleClicksReach4() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JButton btn = (JButton) testFrame.getContentPane().getComponent(4);

            for (int i = 0; i < 4; i++) btn.doClick();

            assertEquals("4", btn.getText());
        });
    }

    @Test
    public void testGameEndsTriggersRestart() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            TestGameFrame frame = (TestGameFrame) testFrame;

            // force model full to end immediately
            for (int r = 0; r < frame.getModel().size; r++) {
                for (int c = 0; c < frame.getModel().size; c++) {
                    frame.getModel().values[r][c] = 4;
                }
            }

            JButton someButton = (JButton) frame.getContentPane().getComponent(0);
            someButton.doClick(); // trigger end logic

            assertTrue(frame.restartCalled);
        });
    }
}
