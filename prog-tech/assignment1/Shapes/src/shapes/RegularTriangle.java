package shapes;

/**
 * Represents an equilateral (regular) triangle with a specified center point and side length.
 * The triangle's bounding box is calculated based on its width (side length) and height.
 *
 * @author Yusupov Boburjon
 */
public class RegularTriangle extends Shape {
    /** The side length of the equilateral triangle */
    private double side;

    /**
     * Constructs a new RegularTriangle with the specified center coordinates and side length.
     *
     * @param x the x-coordinate of the triangle's center
     * @param y the y-coordinate of the triangle's center
     * @param side the length of each side of the equilateral triangle
     * @throws InvalidShapeDataException if the side length is not positive
     */
    public RegularTriangle(double x, double y, double side) throws InvalidShapeDataException {
        super(x, y);
        if (side <= 0) throw new InvalidShapeDataException("Side length of regular triangle should be higher than 0.");
        this.side = side;
    }
    
    /**
     * Gets the side length of this equilateral triangle.
     *
     * @return the length of each side of the triangle
     */
    public double getSide() {
        return this.side;
    }

    /**
     * Calculates and returns the area of the bounding box that encompasses this regular triangle.
     * The bounding box is a rectangle with width equal to the side length and height calculated
     * using the formula: height = (sqrt(3) / 2) * side.
     *
     * @return the area of the rectangular bounding box (side * height)
     */
    @Override
    public double getBoundingBoxArea() {
        double height = (Math.sqrt(3) / 2) * side;
        return side * height;
    }

    /**
     * Returns a string representation of this regular triangle including its center coordinates and side length.
     *
     * @return a formatted string describing the triangle's position and dimensions
     */
    @Override
    public String toString() {
        return "RegularTriangle at (" + centerX + "," + centerY + "), side=" + side;
    }
}
