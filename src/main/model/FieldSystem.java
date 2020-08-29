package model;

import java.util.ArrayList;

// Represents a 3D system of objects
public class FieldSystem {
    public static final double K = 8987551788.7;              // Coulomb's Constant

    private double em;
    private double ex;
    private double ey;
    private double ez;
    private ArrayList<ChargedObject> chargedObjects;    // stores charged objects added to system

    // EFFECTS: sets up new system with charged objects
    public FieldSystem(ArrayList<ChargedObject> chargedObjects) {
        em = 0.0;
        ex = 0.0;
        ey = 0.0;
        ez = 0.0;
        this.chargedObjects = chargedObjects;
    }

    public double getEm() {
        return em;
    }

    public double getEx() {
        return ex;
    }

    public double getEy() {
        return ey;
    }

    public double getEz() {
        return ez;
    }

    public ArrayList<ChargedObject> getChargedObjects() {
        return chargedObjects;
    }

    // MODIFIES: this
    // EFFECTS: adds a point into the system
    public void addPoint(Point point) {
        chargedObjects.add(point);
        System.out.println("Object added successfully");
    }

    // MODIFIES: this
    // EFFECTS: adds a rod into the system
    public void addRod(Rod rod) {
        chargedObjects.add(rod);
        System.out.println("Object added successfully");
    }

    // REQUIRES: n>=1
    // MODIFIES: this
    // EFFECTS: adds a plane into the system
    public void addPlane(Plane plane) {
        chargedObjects.add(plane);
        System.out.println("Object added successfully");
    }

    public void addConductingSphere(ConductingSphere conductingSphere) {
        chargedObjects.add(conductingSphere);
        System.out.println("Object added successfully");
    }

    // REQUIRES: i>=0 and is at most 1 less than number of objects in system
    // MODIFIES: this
    // EFFECTS: removes object from system which is represented with index i
    public void removeObject(int i) {
        if (i <= chargedObjects.size() - 1) {
            chargedObjects.remove(i);
            System.out.println("Object removed successfully");
        } else {
            System.out.println("Object not found");
        }
    }

    // EFFECTS: Calculates electric field at origin due to all charged objects in system
    public void electricField() {
        ArrayList<Double> totalField = new ArrayList<>();

        totalField.add(0.0);  // Magnitude
        totalField.add(0.0);  // Ex
        totalField.add(0.0);  // Ey
        totalField.add(0.0);  // Ez

        for (ChargedObject chargedObject : chargedObjects) {
            ArrayList<Double> currentExyzField = chargedObject.exyzField();
            totalField.set(1, totalField.get(1) + currentExyzField.get(0));
            totalField.set(2, totalField.get(2) + currentExyzField.get(1));
            totalField.set(3, totalField.get(3) + currentExyzField.get(2));
        }

        double totalEMagnitude = Math.pow(Math.pow(totalField.get(1),2)
                        + Math.pow(totalField.get(2),2)
                        + Math.pow(totalField.get(3),2),
                0.5);
        totalField.set(0, totalEMagnitude);

        em = totalField.get(0);
        ex = totalField.get(1);
        ey = totalField.get(2);
        ez = totalField.get(3);
    }
}
