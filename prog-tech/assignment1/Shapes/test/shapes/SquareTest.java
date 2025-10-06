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
public class SquareTest {
    
    public SquareTest() {
    }

    @Test
    public void testSomeMethod() {
    }
    
    @Test
    public void testValidSquareBoundingBoxArea() throws InvalidShapeDataException {
        Square s = new Square(0, 0, 5);
        assertEquals(25.0, s.getBoundingBoxArea(), 0.0001); // 5 * 5
    }

    @Test
    public void testSquareToStringFormat() throws InvalidShapeDataException {
        Square s = new Square(2, 3, 4);
        assertEquals("Square at (2.0,3.0), side=4.0", s.toString());
    }

    @Test
    public void testInvalidSquareSideThrowsException() {
        assertThrows(InvalidShapeDataException.class, () -> new Square(0, 0, -1));
    }
    
    @Test
    public void testSquareConstructorStoresValuesCorrectly() throws InvalidShapeDataException {
        Square s = new Square(1.5, -2.5, 6.0);
        assertEquals(6.0, s.getSide(), 0.0001); // Uses public getter
        assertEquals(1.5, s.getCenterX(), 0.0001); // Requires getCenterX() in Shape
        assertEquals(-2.5, s.getCenterY(), 0.0001); // Requires getCenterY() in Shape
    }

    @Test
    public void testZeroSideBoundaryCase() {
        assertThrows(InvalidShapeDataException.class, () -> new Square(0, 0, 0));
    }
}
