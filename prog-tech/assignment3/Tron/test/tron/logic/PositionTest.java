package tron.logic;

import org.junit.Test;
import static org.junit.Assert.*;

public class PositionTest {

    @Test
    public void testPositionValues() {
        Position pos = new Position(10, 20);
        assertEquals(10, pos.x());
        assertEquals(20, pos.y());
    }

    @Test
    public void testEqualsAndHashCode() {
        Position p1 = new Position(5, 5);
        Position p2 = new Position(5, 5);
        Position p3 = new Position(10, 5);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}
