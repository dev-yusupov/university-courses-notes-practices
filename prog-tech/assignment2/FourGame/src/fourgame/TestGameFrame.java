/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fourgame;

public class TestGameFrame extends GameFrame {
    boolean restartCalled = false;

    public TestGameFrame(int size) {
        super(size);
        this.testMode = true;
    }

    @Override
    protected void restartGame() {
        restartCalled = true;
        dispose(); // safe in test mode
    }
}
