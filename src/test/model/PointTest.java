package model;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.FieldApp;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static ui.AppController.*;
import static ui.AppController.V_END;

class PointTest {
    private Point point1;
    private ArrayList<Double> coordinate1;

    @BeforeEach
    void runBefore() {
        coordinate1 = new ArrayList<>();
        coordinate1.add(3.0);
        coordinate1.add(4.0);
        coordinate1.add(6.0);

        point1 = new Point(4,coordinate1);
    }

    @Test
    void testConstructor() {
        assertEquals(4,point1.getCharge());
        assertEquals(coordinate1,point1.getCoordinate1());
        double distance = Math.pow(Math.pow(coordinate1.get(0),2)
                                   + Math.pow(coordinate1.get(1),2)
                                   + Math.pow(coordinate1.get(2),2),
                                   0.5);
        assertEquals(distance,point1.getDistance());
        assertEquals(5.89348e8,point1.getEmagnitude(),0.00001e8);

        assertEquals(new ArrayList<Double>(),point1.getCoordinate2());
        assertEquals(new ArrayList<Double>(),point1.getCoordinate3());
        assertEquals(0.0,point1.getRadius());
    }

    @Test
    void exyzFieldTest() {
        assertEquals(-2.26375e8,point1.exyzField().get(0),0.00001e8);
        assertEquals(-3.01833e8,point1.exyzField().get(1),0.00001e8);
        assertEquals(-4.52749e8,point1.exyzField().get(2),0.00001e8);
    }

    @Test
    void translateHTest() {
        assertEquals(5 * ((H_END / 100.0) / FieldApp.getHscale()) * (H_END / 100.0) + CENTRE_H,
                point1.translateH(5));
    }

    @Test
    void translateVTest() {
        assertEquals(CENTRE_V - -10 * ((V_END / 100.0) / FieldApp.getVscale()) * (V_END / 100.0),
                point1.translateV(-10));
    }

    @Test
    void makeShapeTest() {
        Circle circle = new Circle(
                point1.translateH(coordinate1.get(1)),
                point1.translateV(coordinate1.get(2)),
                3,
                Color.web("#6DD5FA"));
        assertEquals(circle.getCenterX(),((Circle) point1.makeShape(1,2)).getCenterX());
        assertEquals(circle.getCenterY(),((Circle) point1.makeShape(1,2)).getCenterY());
        assertEquals(circle.getRadius(),((Circle) point1.makeShape(1,2)).getRadius());
        assertEquals(circle.getFill(),((Circle) point1.makeShape(1,2)).getFill());
    }
}
