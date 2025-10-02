/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shapes;

/**
 *
 * @author User
 */
public class Square extends Shape {
    private double side;

    public Square(double x, double y, double side) throws InvalidShapeDataException {
        super(x, y);
        if (side <= 0) throw new InvalidShapeDataException("Side length of square must be higher than 0.");
        this.side = side;
    }

    @Override
    public double getBoundingBoxArea() {
        return side * side;
    }

    @Override
    public String toString() {
        return "Square at (" + centerX + "," + centerY + "), side=" + side;
    }
}
