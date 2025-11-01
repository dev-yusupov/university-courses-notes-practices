package fourgame;

/**
 * A test version of GameFrame that prevents infinite restart loops during testing.
 * This class overrides the restartGame() method to simply set a flag and dispose
 * the frame without creating a new game instance.
 */

public class TestGameFrame extends GameFrame {
    boolean restartCalled = false;

    /**
     * Constructs a new TestGameFrame with the specified board size.
     * Automatically enables test mode to disable popups.
     * 
     * @param size the dimension of the square board
     */
    public TestGameFrame(int size) {
        super(size);
        this.testMode = true;
    }

    /**
     * Overrides restartGame to prevent creating new game instances during testing.
     * Sets the restartCalled flag to true and disposes the frame.
     */
    @Override
    protected void restartGame() {
        restartCalled = true;
        dispose(); // safe in test mode
    }
}
