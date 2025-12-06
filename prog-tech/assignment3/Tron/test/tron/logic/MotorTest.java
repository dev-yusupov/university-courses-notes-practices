package tron.logic;

import java.awt.Color;
import org.junit.Test;
import static org.junit.Assert.*;

public class MotorTest {

    @Test
    public void testInitialization() {
        Position startPos = new Position(10, 10);
        Motor motor = new Motor("TestPlayer", Color.RED, startPos, Direction.RIGHT);

        assertEquals("TestPlayer", motor.getName());
        assertEquals(Color.RED, motor.getColor());
        assertEquals(startPos, motor.getPosition());
        assertEquals(1, motor.getTrail().size());
        assertFalse(motor.isCrashed());
    }

    @Test
    public void testMove() {
        Motor motor = new Motor("P1", Color.BLUE, new Position(0, 0), Direction.RIGHT);
        motor.move(); // Should move to (1, 0)

        assertEquals(new Position(1, 0), motor.getPosition());
        assertEquals(2, motor.getTrail().size());
    }

    @Test
    public void testDirectionChange() {
        Motor motor = new Motor("P1", Color.BLUE, new Position(0, 0), Direction.RIGHT);

        motor.setDirection(Direction.UP); // Valid 90 deg
        motor.move(); // (0, -1)

        assertEquals(new Position(0, -1), motor.getPosition());
    }

    @Test
    public void testInvalidDirectionChange() {
        Motor motor = new Motor("P1", Color.BLUE, new Position(0, 0), Direction.RIGHT);

        motor.setDirection(Direction.LEFT); // Invalid 180 deg
        motor.move(); // Should still be RIGHT -> (1, 0)

        assertEquals(new Position(1, 0), motor.getPosition());
    }
}
