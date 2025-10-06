package shapes;

/**
 * Abstract base class representing a geometric shape with a center point.
 * All shapes have a center position defined by x and y coordinates.
 * Subclasses must implement the calculation of their bounding box area.
 *
 * @author Yusupov Boburjon
 */
abstract public class Shape {
    /** X-coordinate of the shape's center point */
    protected double centerX;
    /** Y-coordinate of the shape's center point */
    protected double centerY;
    
    /**
     * Constructs a new Shape with the specified center coordinates.
     *
     * @param centerX the x-coordinate of the shape's center
     * @param centerY the y-coordinate of the shape's center
     */
    public Shape(double centerX, double centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }
    
    /**
     * Calculates and returns the area of the smallest rectangle that can contain this shape.
     * This method must be implemented by all concrete subclasses.
     *
     * @return the area of the bounding box that encompasses this shape
     */
    public abstract double getBoundingBoxArea();
    
    /**
     * Gets the x-coordinate of the shape's center.
     *
     * @return the x-coordinate of the center point
     */
    public double getCenterX() {
        return this.centerX;
    }
    
    /**
     * Gets the y-coordinate of the shape's center.
     *
     * @return the y-coordinate of the center point
     */
    public double getCenterY() {
        return this.centerY;
    }
}
