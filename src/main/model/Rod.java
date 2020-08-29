package model;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import persistence.Reader;

import java.io.PrintWriter;
import java.util.ArrayList;

// Represents a charged rod in space which inherits ChargedObject fields and methods
// Electric field calculated by dividing plane into a number of points
public class Rod extends ChargedObject {
    private ArrayList<Double> coordinate2;  // coordinate of 2nd endpoint
    private ArrayList<Double> deltaXYZrod;  // a small change in coordinates for calculation of next point

    public Rod(double density,
               ArrayList<Double> c1,      // [x1,y1,z1]  2
               ArrayList<Double> c2,      // [x2,y2,z2]  |
               int n) {                   //             1
        super(density, c1, n);
        coordinate2 = c2;

        deltaXYZrod = new ArrayList<>();
        deltaXYZrod.add((coordinate2.get(0) - coordinate.get(0)) / numPoints);
        deltaXYZrod.add((coordinate2.get(1) - coordinate.get(1)) / numPoints);
        deltaXYZrod.add((coordinate2.get(2) - coordinate.get(2)) / numPoints);
    }

    @Override
    // EFFECTS: returns new line object (to be drawn) from data of rod that is given
    public Node makeShape(int haxis, int vaxis) {
        Line line = new Line(
                translateH(coordinate.get(haxis)),
                translateV(coordinate.get(vaxis)),
                translateH(coordinate2.get(haxis)),
                translateV(coordinate2.get(vaxis)));
        Stop[] stops = new Stop[] { new Stop(0, Color.web("#3494E6")), new Stop(1,Color.web("#EC6EAD"))};
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true,
                CycleMethod.NO_CYCLE, stops);
        line.setStroke(lg1);
        return line;
    }

    @Override
    // EFFECTS: returns electric field at origin due to this rod in vector form
    protected ArrayList<Double> exyzField() {
        ArrayList<Double> totalExyzField = new ArrayList<>();

        double delL = Math.pow(Math.pow(deltaXYZrod.get(0),2)
                        + Math.pow(deltaXYZrod.get(1),2)
                        + Math.pow(deltaXYZrod.get(2),2),
                0.5);

        totalExyzField = setupTotalField(totalExyzField);
        totalExyzField = endPointsField(totalExyzField);
        totalExyzField = midPointsField(totalExyzField);
        totalExyzField = deltaXYZmultiplication(totalExyzField,delL);

        return totalExyzField;
    }

    // EFFECTS: calculates and adds eField of endpoints (Trapezoidal rule) into the total field and returns it
    public ArrayList<Double> endPointsField(ArrayList<Double> totalExyzField) {
        Point point1 = new Point(chargeDensity,coordinate);

        ArrayList<Double> currentExyzField1 = point1.exyzField();

        totalExyzField.set(0, totalExyzField.get(0) + (currentExyzField1.get(0) / 2));
        totalExyzField.set(1, totalExyzField.get(1) + (currentExyzField1.get(1) / 2));
        totalExyzField.set(2, totalExyzField.get(2) + (currentExyzField1.get(2) / 2));

        Point point2 = new Point(chargeDensity,coordinate2);

        ArrayList<Double> currentExyzField2 = point2.exyzField();

        totalExyzField.set(0, totalExyzField.get(0) + (currentExyzField2.get(0) / 2));
        totalExyzField.set(1, totalExyzField.get(1) + (currentExyzField2.get(1) / 2));
        totalExyzField.set(2, totalExyzField.get(2) + (currentExyzField2.get(2) / 2));

        return totalExyzField;
    }

    // EFFECTS: calculates and adds all eFields of points between endpoints into total field and returns it
    public ArrayList<Double> midPointsField(ArrayList<Double> totalExyzField) {
        for (int n = 1; n < numPoints; n++) {
            ArrayList<Double> newC1 = new ArrayList<>();

            newC1.add(coordinate.get(0) + n * deltaXYZrod.get(0));
            newC1.add(coordinate.get(1) + n * deltaXYZrod.get(1));
            newC1.add(coordinate.get(2) + n * deltaXYZrod.get(2));

            Point currentPoint = new Point(chargeDensity,newC1);

            ArrayList<Double> currentExyzField = currentPoint.exyzField();

            totalExyzField.set(0, totalExyzField.get(0) + currentExyzField.get(0));
            totalExyzField.set(1, totalExyzField.get(1) + currentExyzField.get(1));
            totalExyzField.set(2, totalExyzField.get(2) + currentExyzField.get(2));
        }
        return totalExyzField;
    }

    @Override
    public void save(PrintWriter printWriter) {
        printWriter.print("Rod");
        printWriter.print(Reader.OBJECT_DELIMITER);
        printWriter.print(chargeDensity);
        printWriter.print(Reader.OBJECT_DELIMITER);
        printWriter.print(numPoints);
        printWriter.print(Reader.OBJECT_DELIMITER);
        printWriter.print(coordinate);
        printWriter.print(Reader.OBJECT_DELIMITER);
        printWriter.println(coordinate2);
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

    public int getNumPoints() {
        return numPoints;
    }

    public ArrayList<Double> getDeltaXYZrod() {
        return deltaXYZrod;
    }
}
