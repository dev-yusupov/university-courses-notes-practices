package shapes;

/**
 * Represents a regular hexagon with a specified center point and side length.
 * The hexagon's bounding box is calculated based on its width and height dimensions.
 *
 * @author Yusupov Boburjon
 */
public class RegularHexagon extends Shape {
    /** The side length of the regular hexagon */
    private double side;

    /**
     * Constructs a new RegularHexagon with the specified center coordinates and side length.
     *
     * @param x the x-coordinate of the hexagon's center
     * @param y the y-coordinate of the hexagon's center
     * @param side the length of each side of the regular hexagon
     * @throws InvalidShapeDataException if the side length is not positive
     */
    public RegularHexagon(double x, double y, double side) throws InvalidShapeDataException {
        super(x, y);
        if (side <= 0) throw new InvalidShapeDataException("Side length should be higher than 0.");
        this.side = side;
    }
    
    /**
     * Gets the side length of this regular hexagon.
     *
     * @return the length of each side of the hexagon
     */
    public double getSide() {
        return this.side;
    }

    /**
     * Calculates and returns the area of the bounding box that encompasses this regular hexagon.
     * The bounding box is a rectangle with width = 2 * side and height = sqrt(3) * side.
     *
     * @return the area of the rectangular bounding box (width * height)
     */
    @Override
    public double getBoundingBoxArea() {
        double width = 2 * side;
        double height = Math.sqrt(3) * side;
        return width * height;
    }

    /**
     * Returns a string representation of this regular hexagon including its center coordinates and side length.
     *
     * @return a formatted string describing the hexagon's position and dimensions
     */
    @Override
    public String toString() {
        return "RegularHexagon at (" + centerX + "," + centerY + "), side=" + side;
    }
    
}
