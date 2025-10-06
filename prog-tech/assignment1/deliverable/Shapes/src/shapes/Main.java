package shapes;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        List<Shape> shapes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String firstLine = br.readLine();
            if (firstLine == null) {
                throw new IOException("File is empty.");
            }

            int count;
            try {
                count = Integer.parseInt(firstLine.trim());
            } catch (NumberFormatException e) {
                throw new IOException("First line must be a valid integer.", e);
            }

            for (int i = 0; i < count; i++) {
                try {
                    String line = br.readLine();
                    if (line == null) {
                        throw new IllegalArgumentException("Not enough lines in file for the declared number of shapes.");
                    }

                    String[] parts = line.trim().split("\\s+");
                    if (parts.length != 4) {
                        throw new IllegalArgumentException("Invalid number of elements on line " + (i + 2));
                    }

                    String type = parts[0];
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    double size = Double.parseDouble(parts[3]);

                    if (size <= 0) {
                        throw new IllegalArgumentException("Size must be positive on line " + (i + 2));
                    }
                    
                    switch (type) {
                        case "C":
                            try {
                                shapes.add(new Circle(x, y, size));
                            } catch (InvalidShapeDataException e) {
                                System.err.println("Invalid data for Circle on line " + (i + 2) + ": " + e.getMessage());
                            }
                            break;

                        case "S":
                            try {
                                shapes.add(new Square(x, y, size));
                            } catch (InvalidShapeDataException e) {
                                System.err.println("Invalid data for Square on line " + (i + 2) + ": " + e.getMessage());
                            }
                            break;

                        case "T":
                            try {
                                shapes.add(new RegularTriangle(x, y, size));
                            } catch (InvalidShapeDataException e) {
                                System.err.println("Invalid data for Triangle on line " + (i + 2) + ": " + e.getMessage());
                            }
                            break;

                        case "H":
                            try {
                                shapes.add(new RegularHexagon(x, y, size));
                            } catch (InvalidShapeDataException e) {
                                System.err.println("Invalid data for Hexagon on line " + (i + 2) + ": " + e.getMessage());
                            }
                            break;

                        default:
                            System.err.println("Unknown shape type: " + type + " on line " + (i + 2));
                    }


                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping invalid line " + (i + 2) + ": " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return;
        }

        if (shapes.isEmpty()) {
            System.out.println("No valid shapes were loaded.");
            return;
        }

        Shape maxShape = null;
        double maxArea = -1;

        for (Shape s : shapes) {
            double area = s.getBoundingBoxArea();
            if (area > maxArea) {
                maxArea = area;
                maxShape = s;
            }
        }

        System.out.println("Shape with the largest bounding box:");
        System.out.println(maxShape + " with area = " + maxArea);
    }
}
