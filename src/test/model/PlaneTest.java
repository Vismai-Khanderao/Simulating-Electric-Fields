package model;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Polygon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;


class PlaneTest {
    private Plane plane1;
    private ArrayList<Double> coordinate1;
    private ArrayList<Double> coordinate2;
    private ArrayList<Double> coordinate3;

    @BeforeEach
    void runBefore() {
        coordinate1 = new ArrayList<>();
        coordinate1.add(-5.0);
        coordinate1.add(-5.0);
        coordinate1.add(-5.0);

        coordinate2 = new ArrayList<>();
        coordinate2.add(-5.0);
        coordinate2.add(5.0);
        coordinate2.add(-5.0);

        coordinate3 = new ArrayList<>();
        coordinate3.add(5.0);
        coordinate3.add(-5.0);
        coordinate3.add(-5.0);

        plane1 = new Plane(0.005,coordinate1,coordinate2,coordinate3,10000);
    }

    @Test
    void testConstructor() {
        double length1To2 = Math.pow(Math.pow(coordinate2.get(0) - coordinate1.get(0),2)
                                     + Math.pow(coordinate2.get(1) - coordinate1.get(1),2)
                                     + Math.pow(coordinate2.get(2) - coordinate1.get(2),2),
                                     0.5);
        double length1To3 = Math.pow(Math.pow(coordinate3.get(0) - coordinate1.get(0),2)
                                     + Math.pow(coordinate3.get(1) - coordinate1.get(1),2)
                                     + Math.pow(coordinate3.get(2) - coordinate1.get(2),2),
                                     0.5);

        ArrayList<Double> deltaXYZplane = new ArrayList<>();
        deltaXYZplane.add((coordinate3.get(0) - coordinate1.get(0))
                / Math.pow(10000 * (length1To3 / length1To2),0.5));
        deltaXYZplane.add((coordinate3.get(1) - coordinate1.get(1))
                / Math.pow(10000 * (length1To3 / length1To2),0.5));
        deltaXYZplane.add((coordinate3.get(2) - coordinate1.get(2))
                / Math.pow(10000 * (length1To3 / length1To2),0.5));

        assertEquals(0.005,plane1.getCharge());
        assertEquals(coordinate1,plane1.getCoordinate1());
        assertEquals(coordinate2,plane1.getCoordinate2());
        assertEquals(coordinate3,plane1.getCoordinate3());
        assertEquals(deltaXYZplane,plane1.getDeltaXYZplane());
        assertEquals(length1To2,plane1.getLength1To2());
        assertEquals(length1To3,plane1.getLength1To3());
    }

    @Test
    void exyzFieldTest() {
        assertEquals(0,plane1.exyzField().get(0),0.1e7);
        assertEquals(0,plane1.exyzField().get(1),0.1e7);
        assertEquals(9.4e7,plane1.exyzField().get(2),0.1e7);
    }

    @Test
    void makeShapeTest() {
        ArrayList<Double> coord4 = new ArrayList<>();
        coord4.add(coordinate2.get(1)
                + (coordinate3.get(1) - coordinate1.get(1)));
        coord4.add(coordinate2.get(2)
                + (coordinate3.get(2) - coordinate1.get(2)));
        Polygon plane = new Polygon();
        plane.getPoints().addAll(plane1.translateH(coordinate1.get(1)), plane1.translateV(coordinate1.get(2)),
                plane1.translateH(coordinate2.get(1)), plane1.translateV(coordinate2.get(2)),
                plane1.translateH(coord4.get(0)), plane1.translateV(coord4.get(1)),
                plane1.translateH(coordinate3.get(1)), plane1.translateV(coordinate3.get(2)));
        Stop[] stops = new Stop[] { new Stop(0, Color.web("#02AAB0")), new Stop(1,Color.web("#00CDAC"))};
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true,
                CycleMethod.NO_CYCLE, stops);
        plane.setFill(lg1);
        Polygon actualPlane = (Polygon) plane1.makeShape(1, 2);
        assertEquals(plane.getPoints(),actualPlane.getPoints());
        assertEquals(plane.getFill(),actualPlane.getFill());
    }
}
