package tron.logic;

import org.junit.Test;
import static org.junit.Assert.*;

public class DirectionTest {

    @Test
    public void testEnumValues() {
        assertEquals(4, Direction.values().length);
        assertEquals(Direction.UP, Direction.valueOf("UP"));
        assertEquals(Direction.DOWN, Direction.valueOf("DOWN"));
        assertEquals(Direction.LEFT, Direction.valueOf("LEFT"));
        assertEquals(Direction.RIGHT, Direction.valueOf("RIGHT"));
    }
}
