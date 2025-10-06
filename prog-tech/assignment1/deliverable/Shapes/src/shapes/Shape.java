/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shapes;

/**
 *
 * @author User
 */
abstract public class Shape {
    protected double centerX;
    protected double centerY;
    
    public Shape(double centerX, double centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }
    
    public abstract double getBoundingBoxArea();
    
    public double getCenterX() {
        return this.centerX;
    }
    
    public double getCenterY() {
        return this.centerY;
    }
}
