package model;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import persistence.Reader;

import java.io.PrintWriter;
import java.util.ArrayList;

// Represents a singular point charge in space which inherits ChargedObject fields and methods
public class Point extends ChargedObject {
    private double distance;                 // distance from origin (calculation point)
    private double emagnitude;               // magnitude of electric field at calculation point

    public Point(double charge,
                 ArrayList<Double> c1) {    // [x,y,z]
        super(charge, c1, 0);
        distance = Math.pow(Math.pow(coordinate.get(0),2)
                        + Math.pow(coordinate.get(1),2)
                        + Math.pow(coordinate.get(2),2),
                0.5);
        emagnitude = (FieldSystem.K * chargeDensity) / (Math.pow(distance, 2));
    }

    @Override
    // EFFECTS: returns new circle object (to be drawn) from data of point that is given
    public Node makeShape(int haxis, int vaxis) {
        return new Circle(
                translateH(coordinate.get(haxis)),
                translateV(coordinate.get(vaxis)),
                3,
                Color.web("#6DD5FA"));
    }

    @Override
    // EFFECTS: returns electric field at origin due to this point in vector form
    protected ArrayList<Double> exyzField() {
        double ex = (-coordinate.get(0) / distance) * emagnitude;
        double ey = (-coordinate.get(1) / distance) * emagnitude;
        double ez = (-coordinate.get(2) / distance) * emagnitude;

        ArrayList<Double> exyzFieldVectors = new ArrayList<>();
        exyzFieldVectors.add(ex);
        exyzFieldVectors.add(ey);
        exyzFieldVectors.add(ez);

        return exyzFieldVectors;
    }

    @Override
    public void save(PrintWriter printWriter) {
        printWriter.print("Point");
        printWriter.print(Reader.OBJECT_DELIMITER);
        printWriter.print(chargeDensity);
        printWriter.print(Reader.OBJECT_DELIMITER);
        printWriter.print(numPoints);
        printWriter.print(Reader.OBJECT_DELIMITER);
        printWriter.println(coordinate);
    }

    @Override
    public double getCharge() {
        return chargeDensity;
    }

    @Override
    public ArrayList<Double> getCoordinate1() {
        return coordinate;
    }

    public double getDistance() {
        return distance;
    }

    public double getEmagnitude() {
        return emagnitude;
    }
}
