/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shapes;

/**
 *
 * @author User
 */
public class Circle extends Shape {
    private double radius;
    
    public Circle(double x, double y, double radius) throws InvalidShapeDataException {
        super(x, y);
        if (radius <= 0) throw new InvalidShapeDataException("Radius must be positive.");
        this.radius = radius;
    }
    
    @Override
    public double getBoundingBoxArea() {
        double side = 2 * radius;
        return side * side;
    }
    
    @Override
    public String toString() {
        return "Circle at (" + centerX + "," + centerY + "), r=" + radius;
    }
}
