package model;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import persistence.Reader;
import ui.FieldApp;

import java.io.PrintWriter;
import java.util.ArrayList;

import static ui.AppController.*;

// Represents a charged conducting sphere in space which inherits ChargedObject fields and methods
// Electric field calculated exactly by symmetry of a sphere
// Electric field of a point outside the radius is the same as a point charge at the center of the sphere
// and electric field inside the sphere is 0
// Assuming the sphere, as other objects, has a infinitely thin surface, we assume if distance to center <= radius then
// electric field at origin is 0
public class ConductingSphere extends ChargedObject {
    private double radius;

    public ConductingSphere(double density, ArrayList<Double> c1, double r) {
        super(density, c1, 0);
        radius = r;
    }

    @Override
    // EFFECTS: returns new circle object (to be drawn) from data of conducting sphere that is given
    public Node makeShape(int haxis, int vaxis) {
        Stop[] stops = new Stop[] { new Stop(0, Color.web("#FF5F6D")), new Stop(1,Color.web("#FFC371"))};
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true,
                CycleMethod.NO_CYCLE, stops);
        return new Circle(
                translateH(coordinate.get(haxis)),
                translateV(coordinate.get(vaxis)),
                radius * ((H_END / 100.0) / FieldApp.getHscale()) * (H_END / 100.0),
                lg1);
    }

    @Override
    // EFFECTS: returns electric field at origin due to this rod in vector form
    protected ArrayList<Double> exyzField() {
        ArrayList<Double> totalExyzField = new ArrayList<>();
        double distanceToCenter = Math.pow(Math.pow(coordinate.get(0),2)
                        + Math.pow(coordinate.get(1),2)
                        + Math.pow(coordinate.get(2),2),
                0.5);

        totalExyzField = setupTotalField(totalExyzField);

        if (distanceToCenter >= radius) {
            double totalCharge = chargeDensity * 4 * Math.PI * Math.pow(radius, 2);
            Point centerPoint = new Point(totalCharge, coordinate);
            ArrayList<Double> currentExyzField = centerPoint.exyzField();

            totalExyzField.set(0, totalExyzField.get(0) + currentExyzField.get(0));
            totalExyzField.set(1, totalExyzField.get(1) + currentExyzField.get(1));
            totalExyzField.set(2, totalExyzField.get(2) + currentExyzField.get(2));

        }
        return totalExyzField;
    }

    @Override
    public void save(PrintWriter printWriter) {
        printWriter.print("Conducting Sphere");
        printWriter.print(Reader.OBJECT_DELIMITER);
        printWriter.print(chargeDensity);
        printWriter.print(Reader.OBJECT_DELIMITER);
        printWriter.print(numPoints);
        printWriter.print(Reader.OBJECT_DELIMITER);
        printWriter.print(coordinate);
        printWriter.print(Reader.OBJECT_DELIMITER);
        printWriter.println(radius);
    }

    @Override
    public double getCharge() {
        return chargeDensity;
    }

    @Override
    public ArrayList<Double> getCoordinate1() {
        return coordinate;
    }

    public double getRadius() {
        return radius;
    }
}
