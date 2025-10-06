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
public class RegularHexagonTest {
    
    public RegularHexagonTest() {
    }

    @Test
    public void testSomeMethod() {
    }
    
    @Test
    public void testValidHexagonBoundingBoxArea() throws InvalidShapeDataException {
        RegularHexagon h = new RegularHexagon(0, 0, 4);
        double expected = (2 * 4) * (Math.sqrt(3) * 4); // width * height
        assertEquals(expected, h.getBoundingBoxArea(), 0.0001);
    }

    @Test
    public void testHexagonToStringFormat() throws InvalidShapeDataException {
        RegularHexagon h = new RegularHexagon(1, 2, 3);
        assertEquals("RegularHexagon at (1.0,2.0), side=3.0", h.toString());
    }

    @Test
    public void testInvalidHexagonSideThrowsException() {
        assertThrows(InvalidShapeDataException.class, () -> new RegularHexagon(0, 0, -5));
    }

    @Test
    public void testHexagonConstructorStoresValuesCorrectly() throws InvalidShapeDataException {
        RegularHexagon h = new RegularHexagon(2.5, -1.5, 6.0);
        assertEquals(6.0, h.getSide(), 0.0001); // Requires getSide() method
        assertEquals(2.5, h.getCenterX(), 0.0001);
        assertEquals(-1.5, h.getCenterY(), 0.0001);
    }

    @Test
    public void testBoundingBoxAreaPrecision() throws InvalidShapeDataException {
        RegularHexagon h = new RegularHexagon(0, 0, 1);
        double expected = (2 * 1) * (Math.sqrt(3) * 1);
        assertEquals(expected, h.getBoundingBoxArea(), 0.0001);
    }

    @Test
    public void testZeroSideBoundaryCase() {
        assertThrows(InvalidShapeDataException.class, () -> new RegularHexagon(0, 0, 0));
    }

}
