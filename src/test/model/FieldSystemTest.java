package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class FieldSystemTest {
    private FieldSystem s1;
    private Point point1;
    private Point point2;
    private Rod rod;
    private Plane plane;
    private ConductingSphere conductingSphere;

    @BeforeEach
    void runBefore() {
        s1 = new FieldSystem(new ArrayList<>());

        ArrayList<Double> coordinate1 = new ArrayList<>();
        coordinate1.add(3.0);
        coordinate1.add(4.0);
        coordinate1.add(6.0);
        ArrayList<Double> coordinate2 = new ArrayList<>();
        coordinate2.add(-10.0);
        coordinate2.add(-5.0);
        coordinate2.add(6.5);
        ArrayList<Double> coordinate3 = new ArrayList<>();
        coordinate3.add(-12.0);
        coordinate3.add(0.0);
        coordinate3.add(0.5);

        point1 = new Point(5,coordinate1);
        point2 = new Point(-3,coordinate2);
        plane = new Plane(5,coordinate1,coordinate2,coordinate3,50);
        rod = new Rod(5,coordinate1,coordinate2,50);
        conductingSphere = new ConductingSphere(-2,coordinate1,3);
    }

    @Test
    void testConstructor() {
        assertEquals(0,s1.getChargedObjects().size());
    }

    @Test
    void addPointTest() {
        s1.addPoint(point1);

        assertEquals(1,s1.getChargedObjects().size());
        assertEquals(point1, s1.getChargedObjects().get(0));
    }

    @Test
    void addRodTest() {
        s1.addRod(rod);

        assertEquals(1,s1.getChargedObjects().size());
        assertEquals(rod, s1.getChargedObjects().get(0));
    }

    @Test
    void addPlaneTest() {
        s1.addPlane(plane);

        assertEquals(1,s1.getChargedObjects().size());
        assertEquals(plane, s1.getChargedObjects().get(0));
    }

    @Test
    void addConductingSphereTest() {
        s1.addConductingSphere(conductingSphere);

        assertEquals(1,s1.getChargedObjects().size());
        assertEquals(conductingSphere, s1.getChargedObjects().get(0));
    }

    @Test
    void addMultipleTest() {
        s1.addRod(rod);
        s1.addPlane(plane);
        s1.addPoint(point1);

        assertEquals(3,s1.getChargedObjects().size());
        assertEquals(rod,s1.getChargedObjects().get(0));
        assertEquals(plane,s1.getChargedObjects().get(1));
        assertEquals(point1,s1.getChargedObjects().get(2));
    }

    @Test
    void removeObjectTest() {
        s1.addPoint(point1);
        assertEquals(1,s1.getChargedObjects().size());
        s1.removeObject(0);
        assertEquals(0,s1.getChargedObjects().size());
    }

    @Test
    void removeObjectNotPresentTest() {
        s1.addPoint(point1);
        assertEquals(1,s1.getChargedObjects().size());
        s1.removeObject(2);
        assertEquals(1,s1.getChargedObjects().size());
    }

    @Test
    void removeMultipleObjectTest() {
        s1.addRod(rod);
        s1.addPlane(plane);
        assertEquals(2,s1.getChargedObjects().size());

        s1.removeObject(0);
        assertEquals(1,s1.getChargedObjects().size());
        assertEquals(plane,s1.getChargedObjects().get(0));

        s1.removeObject(0);
        assertEquals(0,s1.getChargedObjects().size());
    }

    @Test
    void electricFieldTest() {
        s1.addPoint(point1);
        s1.addPoint(point2);
        s1.electricField();

        assertEquals(7.7107767e8,s1.getEm(),0.0000001e8);
        assertEquals(-4.0762446e8,s1.getEx(),0.0000001e8);
        assertEquals(-4.3961922e8,s1.getEy(),0.0000001e8);
        assertEquals(-4.8491031e8,s1.getEz(),0.0000001e8);
    }
}