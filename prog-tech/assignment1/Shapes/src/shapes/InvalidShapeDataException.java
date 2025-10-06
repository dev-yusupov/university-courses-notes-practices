package shapes;

/**
 * Custom runtime exception thrown when invalid data is provided during shape construction.
 * This exception is typically thrown when shape parameters (such as radius, side length, etc.)
 * are negative or zero, which would result in invalid geometric shapes.
 *
 * @author Yusupov Boburjon
 */
public class InvalidShapeDataException extends RuntimeException {
    /**
     * Constructs a new InvalidShapeDataException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public InvalidShapeDataException(String message) {
        super(message);
    }
}
