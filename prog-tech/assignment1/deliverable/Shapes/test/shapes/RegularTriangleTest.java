/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package shapes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author User
 */
public class RegularTriangleTest {
    
    public RegularTriangleTest() {
    }

    @Test
    public void testSomeMethod() {
    }
    
    @Test
    public void testValidTriangleBoundingBoxArea() throws InvalidShapeDataException {
        RegularTriangle t = new RegularTriangle(0, 0, 4);
        double expected = 4 * ((Math.sqrt(3) / 2) * 4); // base * height
        assertEquals(expected, t.getBoundingBoxArea(), 0.0001);
    }

    @Test
    public void testTriangleToStringFormat() throws InvalidShapeDataException {
        RegularTriangle t = new RegularTriangle(1, 2, 3);
        assertEquals("RegularTriangle at (1.0,2.0), side=3.0", t.toString());
    }

    @Test
    public void testInvalidTriangleSideThrowsException() {
        assertThrows(InvalidShapeDataException.class, () -> new RegularTriangle(0, 0, -5));
    }

    @Test
    public void testTriangleConstructorStoresValuesCorrectly() throws InvalidShapeDataException {
        RegularTriangle t = new RegularTriangle(1.5, -2.5, 6.0);
        assertEquals(6.0, t.getSide(), 0.0001); // Add getSide() method if not available
        assertEquals(1.5, t.getCenterX(), 0.0001);
        assertEquals(-2.5, t.getCenterY(), 0.0001);
    }

    @Test
    public void testBoundingBoxAreaPrecision() throws InvalidShapeDataException {
        RegularTriangle t = new RegularTriangle(0, 0, 1);
        double expected = 1 * (Math.sqrt(3) / 2);
        assertEquals(expected, t.getBoundingBoxArea(), 0.0001);
    }

    @Test
    public void testZeroSideBoundaryCase() {
        assertThrows(InvalidShapeDataException.class, () -> new RegularTriangle(0, 0, 0));
    }

}
