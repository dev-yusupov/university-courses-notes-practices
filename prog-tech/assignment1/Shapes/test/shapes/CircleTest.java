package shapes;

import org.junit.Test;
import static org.junit.Assert.*;

public class CircleTest {
    
    public CircleTest() {
    }

    @Test
    public void testValidCircleBoundingBoxArea() throws InvalidShapeDataException {
        Circle c = new Circle(0, 0, 5);
        assertEquals(100.0, c.getBoundingBoxArea(), 0.0001);  // (2*5)^2 = 100
    }

    @Test
    public void testCircleToStringContainsValues() throws InvalidShapeDataException {
        Circle c = new Circle(2, 3, 4);
        String output = c.toString();
        assertTrue(output.contains("2.0"));
        assertTrue(output.contains("3.0"));
        assertTrue(output.contains("4.0"));
    }


    @Test
    public void testInvalidRadiusThrowsException() {
        assertThrows(InvalidShapeDataException.class, () -> new Circle(0, 0, -1));
    }
    
    @Test
    public void testConstructorStoresValuesCorrectly() throws InvalidShapeDataException {
        Circle c = new Circle(1.5, -2.5, 3.0);
        assertEquals(3.0, c.getRadius(), 0.0001); // You may need to add getRadius() for testing
        assertEquals(1.5, c.getCenterX(), 0.0001);
        assertEquals(-2.5, c.getCenterY(), 0.0001);
    }

    @Test
    public void testBoundingBoxAreaUsesCorrectFormula() throws InvalidShapeDataException {
        Circle c = new Circle(0, 0, 7);
        double expected = (2 * 7) * (2 * 7); // Explicit formula check
        assertEquals(expected, c.getBoundingBoxArea(), 0.0001);
    }

    @Test
    public void testZeroRadiusBoundaryCase() {
        assertThrows(InvalidShapeDataException.class, () -> new Circle(0, 0, 0)); // Edge condition branch
    }
}
