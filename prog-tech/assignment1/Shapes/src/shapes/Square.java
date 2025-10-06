package shapes;

/**
 * Represents a square shape with a specified center point and side length.
 * The square's bounding box area is equal to the square of its side length.
 *
 * @author Yusupov Boburjon
 */
public class Square extends Shape {
    /** The side length of the square */
    private double side;

    /**
     * Constructs a new Square with the specified center coordinates and side length.
     *
     * @param x the x-coordinate of the square's center
     * @param y the y-coordinate of the square's center
     * @param side the length of each side of the square
     * @throws InvalidShapeDataException if the side length is not positive
     */
    public Square(double x, double y, double side) throws InvalidShapeDataException {
        super(x, y);
        if (side <= 0) throw new InvalidShapeDataException("Side length of square must be higher than 0.");
        this.side = side;
    }
    
    /**
     * Gets the side length of this square.
     *
     * @return the length of each side of the square
     */
    public double getSide() {
        return this.side;
    }

    /**
     * Calculates and returns the area of the bounding box that encompasses this square.
     * For a square, the bounding box area is simply the square of its side length.
     *
     * @return the area of the square (side * side)
     */
    @Override
    public double getBoundingBoxArea() {
        return side * side;
    }

    /**
     * Returns a string representation of this square including its center coordinates and side length.
     *
     * @return a formatted string describing the square's position and dimensions
     */
    @Override
    public String toString() {
        return "Square at (" + centerX + "," + centerY + "), side=" + side;
    }
}
