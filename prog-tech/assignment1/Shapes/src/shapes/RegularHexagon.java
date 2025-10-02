/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shapes;

/**
 *
 * @author User
 */
public class RegularHexagon extends Shape {
    private double side;

    public RegularHexagon(double x, double y, double side) throws InvalidShapeDataException {
        super(x, y);
        if (side <= 0) throw new InvalidShapeDataException("Side length should be higher than 0.");
        this.side = side;
    }

    @Override
    public double getBoundingBoxArea() {
        double width = 2 * side;
        double height = Math.sqrt(3) * side;
        return width * height;
    }

    @Override
    public String toString() {
        return "RegularHexagon at (" + centerX + "," + centerY + "), side=" + side;
    }
}
