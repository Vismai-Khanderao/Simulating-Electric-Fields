package model;

import javafx.scene.Node;
import persistence.Saveable;
import ui.FieldApp;

import java.util.ArrayList;

import static ui.AppController.*;

// Represents a charged object
// Abstract as all charged objects represent an item that can be modelled/no generic charged object can exist
public abstract class ChargedObject implements Saveable {
    protected double chargeDensity;                                // density of charge
    protected ArrayList<Double> coordinate;                        // object origin coordinate
    protected int numPoints;                                       // number of points to be calculated

    protected ChargedObject(double density, ArrayList<Double> coord, int n) {
        chargeDensity = density;
        coordinate = coord;
        numPoints = n;
    }

    public abstract Node makeShape(int haxis, int vaxis);

    // EFFECTS: translates center origin coordinate to top-left origin coordinate for H axis
    protected double translateH(double h) {
        return h * ((H_END / 100.0) / FieldApp.getHscale()) * (H_END / 100.0) + CENTRE_H;
    }

    // EFFECTS: translates center origin coordinate to top-left origin coordinate for V axis
    protected double translateV(double v) {
        return CENTRE_V - v * ((V_END / 100.0) / FieldApp.getVscale()) * (V_END / 100.0);
    }

    protected abstract ArrayList<Double> exyzField();

    // EFFECTS: Initialises list to store total field in 3D vector form and returns it
    protected ArrayList<Double> setupTotalField(ArrayList<Double> totalExyzField) {
        for (int i = 0; i < 3; i++) {
            totalExyzField.add(0.0);
        }
        return totalExyzField;
    }

    // EFFECTS: multiplies each element of total field by delta distance and returns it (last step of Trapezoidal rule)
    protected ArrayList<Double> deltaXYZmultiplication(ArrayList<Double> totalExyzField,double delL) {
        totalExyzField.set(0,totalExyzField.get(0) * delL);
        totalExyzField.set(1,totalExyzField.get(1) * delL);
        totalExyzField.set(2,totalExyzField.get(2) * delL);
        return totalExyzField;
    }

    public abstract double getCharge();

    public abstract ArrayList<Double> getCoordinate1();

    public ArrayList<Double> getCoordinate2() {
        return new ArrayList<>();
    }

    public ArrayList<Double> getCoordinate3() {
        return new ArrayList<>();
    }

    public double getRadius() {
        return 0.0;
    }
}
