package model;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Polygon;
import persistence.Reader;

import java.io.PrintWriter;
import java.util.ArrayList;

// Represents a charged plane in space which inherits ChargedObject fields and methods
// Electric field calculated by dividing plane into a number of rods
public class Plane extends ChargedObject {
    private ArrayList<Double> coordinate2;   // coordinates of 2nd end point of plane
    private ArrayList<Double> coordinate3;   // coordinates of 3rd end point of plane
    private ArrayList<Double> deltaXYZplane; // small change in coordinates in direction 1 to 3 / 2 to 4
    private double length1To2;
    private double length1To3;

    //Each Rod will be of coordinates 1 to 2 along 1 to 3
    public Plane(double density,
                 ArrayList<Double> c1,   // [x1,y1,z1]  2-----4
                 ArrayList<Double> c2,   // [x2,y2,z2]  |     |
                 ArrayList<Double> c3,   // [x3,y3,z3]  1-----3
                 int n) {
        super(density, c1, n);

        coordinate2 = c2;
        coordinate3 = c3;

        length1To2 = Math.pow(Math.pow(coordinate2.get(0) - coordinate.get(0),2)
                        + Math.pow(coordinate2.get(1) - coordinate.get(1),2)
                        + Math.pow(coordinate2.get(2) - coordinate.get(2),2),
                0.5);
        length1To3 = Math.pow(Math.pow(coordinate3.get(0) - coordinate.get(0),2)
                        + Math.pow(coordinate3.get(1) - coordinate.get(1),2)
                        + Math.pow(coordinate3.get(2) - coordinate.get(2),2),
                0.5);

        deltaXYZplane = new ArrayList<>();
        deltaXYZplane.add((coordinate3.get(0) - coordinate.get(0))
                / Math.pow(numPoints * (length1To3 / length1To2),0.5));
        deltaXYZplane.add((coordinate3.get(1) - coordinate.get(1))
                / Math.pow(numPoints * (length1To3 / length1To2),0.5));
        deltaXYZplane.add((coordinate3.get(2) - coordinate.get(2))
                / Math.pow(numPoints * (length1To3 / length1To2),0.5));

    }

    @Override
    // EFFECTS: returns new polygon object (to be drawn) from data of plane that is given
    public Node makeShape(int haxis, int vaxis) {
        ArrayList<Double> coord4 = new ArrayList<>();
        coord4.add(coordinate2.get(haxis)
                + (coordinate3.get(haxis) - coordinate.get(haxis)));
        coord4.add(coordinate2.get(vaxis)
                + (coordinate3.get(vaxis) - coordinate.get(vaxis)));
        Polygon plane = new Polygon();
        plane.getPoints().addAll(translateH(coordinate.get(haxis)), translateV(coordinate.get(vaxis)),
                translateH(coordinate2.get(haxis)), translateV(coordinate2.get(vaxis)),
                translateH(coord4.get(0)), translateV(coord4.get(1)),
                translateH(coordinate3.get(haxis)), translateV(coordinate3.get(vaxis)));
        Stop[] stops = new Stop[] { new Stop(0, Color.web("#02AAB0")), new Stop(1,Color.web("#00CDAC"))};
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true,
                CycleMethod.NO_CYCLE, stops);
        plane.setFill(lg1);
        return plane;
    }

    @Override
    // EFFECTS: returns electric field at origin due to this plane in vector form
    protected ArrayList<Double> exyzField() {
        ArrayList<Double> totalExyzField = new ArrayList<>();

        double delL = Math.pow(Math.pow(deltaXYZplane.get(0),2)
                + Math.pow(deltaXYZplane.get(1),2)
                + Math.pow(deltaXYZplane.get(2),2),0.5);

        totalExyzField = setupTotalField(totalExyzField);
        totalExyzField = endRodsField(totalExyzField);
        totalExyzField = midRodsField(totalExyzField);
        totalExyzField = deltaXYZmultiplication(totalExyzField,delL);

        return totalExyzField;
    }

    // EFFECTS: calculates and adds eField of endpoint rods (Trapezoidal rule) into the total field and returns it
    public ArrayList<Double> endRodsField(ArrayList<Double> totalExyzField) {
        Rod rod1 = new Rod(chargeDensity,coordinate,coordinate2,
                (int) Math.pow(numPoints * (length1To2 / length1To3),0.5));

        ArrayList<Double> currentExyzField1 = rod1.exyzField();

        totalExyzField.set(0, totalExyzField.get(0) + (currentExyzField1.get(0) / 2));
        totalExyzField.set(1, totalExyzField.get(1) + (currentExyzField1.get(1) / 2));
        totalExyzField.set(2, totalExyzField.get(2) + (currentExyzField1.get(2) / 2));

        ArrayList<Double> coordinate4 = new ArrayList<>();
        coordinate4.add(coordinate3.get(0) + (coordinate2.get(0) - coordinate.get(0)));
        coordinate4.add(coordinate3.get(1) + (coordinate2.get(1) - coordinate.get(1)));
        coordinate4.add(coordinate3.get(2) + (coordinate2.get(2) - coordinate.get(2)));

        Rod rod2 = new Rod(chargeDensity,coordinate3,coordinate4,
                (int) Math.pow(numPoints * (length1To2 / length1To3),0.5));

        ArrayList<Double> currentExyzField2 = rod2.exyzField();

        totalExyzField.set(0, totalExyzField.get(0) + (currentExyzField2.get(0) / 2));
        totalExyzField.set(1, totalExyzField.get(1) + (currentExyzField2.get(1) / 2));
        totalExyzField.set(2, totalExyzField.get(2) + (currentExyzField2.get(2) / 2));

        return totalExyzField;
    }

    // EFFECTS: calculates and adds all eFields of rods between endpoint rods into total field and returns it
    public ArrayList<Double> midRodsField(ArrayList<Double> totalExyzField) {
        for (int n = 1; n < ((int) Math.pow(numPoints * (length1To3 / length1To2),0.5)); n++) {
            ArrayList<Double> newC1 = new ArrayList<>();

            newC1.add(coordinate.get(0) + n * deltaXYZplane.get(0));
            newC1.add(coordinate.get(1) + n * deltaXYZplane.get(1));
            newC1.add(coordinate.get(2) + n * deltaXYZplane.get(2));

            ArrayList<Double> newC2 = new ArrayList<>();

            newC2.add(coordinate2.get(0) + n * deltaXYZplane.get(0));
            newC2.add(coordinate2.get(1) + n * deltaXYZplane.get(1));
            newC2.add(coordinate2.get(2) + n * deltaXYZplane.get(2));

            Rod currentRod = new Rod(chargeDensity,newC1,newC2,
                    (int) Math.pow(numPoints * (length1To2 / length1To3),0.5));

            ArrayList<Double> currentExyzField = currentRod.exyzField();

            totalExyzField.set(0, totalExyzField.get(0) + currentExyzField.get(0));
            totalExyzField.set(1, totalExyzField.get(1) + currentExyzField.get(1));
            totalExyzField.set(2, totalExyzField.get(2) + currentExyzField.get(2));
        }

        return totalExyzField;
    }

    @Override
    public void save(PrintWriter printWriter) {
        printWriter.print("Plane");
        printWriter.print(Reader.OBJECT_DELIMITER);
        printWriter.print(chargeDensity);
        printWriter.print(Reader.OBJECT_DELIMITER);
        printWriter.print(numPoints);
        printWriter.print(Reader.OBJECT_DELIMITER);
        printWriter.print(coordinate);
        printWriter.print(Reader.OBJECT_DELIMITER);
        printWriter.print(coordinate2);
        printWriter.print(Reader.OBJECT_DELIMITER);
        printWriter.println(coordinate3);
    }

    @Override
    public double getCharge() {
        return chargeDensity;
    }

    @Override
    public ArrayList<Double> getCoordinate1() {
        return coordinate;
    }

    public ArrayList<Double> getCoordinate2() {
        return coordinate2;
    }

    public ArrayList<Double> getCoordinate3() {
        return coordinate3;
    }

    public int getNumPoints() {
        return numPoints;
    }

    public ArrayList<Double> getDeltaXYZplane() {
        return deltaXYZplane;
    }

    public double getLength1To2() {
        return length1To2;
    }

    public double getLength1To3() {
        return length1To3;
    }
}
