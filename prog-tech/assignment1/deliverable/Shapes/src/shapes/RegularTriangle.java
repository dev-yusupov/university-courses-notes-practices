/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shapes;

/**
 *
 * @author User
 */
public class RegularTriangle extends Shape {
    private double side;

    public RegularTriangle(double x, double y, double side) throws InvalidShapeDataException {
        super(x, y);
        if (side <= 0) throw new InvalidShapeDataException("Side length of regular triangle should be higher than 0.");
        this.side = side;
    }
    
    public double getSide() {
        return this.side;
    }

    @Override
    public double getBoundingBoxArea() {
        double height = (Math.sqrt(3) / 2) * side;
        return side * height;
    }

    @Override
    public String toString() {
        return "RegularTriangle at (" + centerX + "," + centerY + "), side=" + side;
    }
}
