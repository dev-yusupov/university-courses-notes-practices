package shapes;

/**
 * Represents a circle shape with a specified center point and radius.
 * The circle's bounding box is calculated as a square with side length equal to the diameter.
 *
 * @author Yusupov Boburjon
 */
public class Circle extends Shape {
    /** The radius of the circle */
    private double radius;
    
    /**
     * Constructs a new Circle with the specified center coordinates and radius.
     *
     * @param x the x-coordinate of the circle's center
     * @param y the y-coordinate of the circle's center
     * @param radius the radius of the circle
     * @throws InvalidShapeDataException if the radius is not positive
     */
    public Circle(double x, double y, double radius) throws InvalidShapeDataException {
        super(x, y);
        if (radius <= 0) throw new InvalidShapeDataException("Radius must be positive.");
        this.radius = radius;
    }
    
    /**
     * Gets the radius of this circle.
     *
     * @return the radius of the circle
     */
    public double getRadius() {
        return this.radius;
    }
    
    /**
     * Calculates and returns the area of the bounding box that encompasses this circle.
     * The bounding box is a square with side length equal to the circle's diameter (2 * radius).
     *
     * @return the area of the square bounding box
     */
    @Override
    public double getBoundingBoxArea() {
        double side = 2 * radius;
        return side * side;
    }
    
    /**
     * Returns a string representation of this circle including its center coordinates and radius.
     *
     * @return a formatted string describing the circle's position and size
     */
    @Override
    public String toString() {
        return "Circle at (" + centerX + "," + centerY + "), r=" + radius;
    }
}
