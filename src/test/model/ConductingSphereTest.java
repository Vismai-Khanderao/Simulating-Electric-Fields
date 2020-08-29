package model;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.FieldApp;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static ui.AppController.H_END;

class ConductingSphereTest {
    private ConductingSphere cs1;
    private ConductingSphere cs2;
    private ConductingSphere cs3;
    private ArrayList<Double> coordinate1;

    @BeforeEach
    void runBefore() {
        coordinate1 = new ArrayList<>();
        coordinate1.add(0.0);
        coordinate1.add(3.0);
        coordinate1.add(4.0);
        
        cs1 = new ConductingSphere(4,coordinate1,4);    // r > R
        cs2 = new ConductingSphere(4,coordinate1,5);    // r = R
        cs3 = new ConductingSphere(4,coordinate1,6);    // r < R
    }

    @Test
    void testConstructor() {
        assertEquals(4,cs1.getCharge());
        assertEquals(coordinate1,cs1.getCoordinate1());
        assertEquals(4,cs1.getRadius());
    }

    @Test
    void exyzFieldTest() {
        assertEquals(0,cs1.exyzField().get(0),0.0000001e11);
        assertEquals(-1.7347723e11,cs1.exyzField().get(1),0.0000001e11);
        assertEquals(-2.3130298e11,cs1.exyzField().get(2),0.0000001e11);

        assertEquals(0,cs2.exyzField().get(0),0.0000001e11);
        assertEquals(-2.7105817e11,cs2.exyzField().get(1),0.0000001e11);
        assertEquals(-3.6141090e11,cs2.exyzField().get(2),0.0000001e11);

        assertEquals(0,cs3.exyzField().get(0),0.0000001e11);
        assertEquals(0,cs3.exyzField().get(1),0.0000001e11);
        assertEquals(0,cs3.exyzField().get(2),0.0000001e11);
    }

    @Test
    void makeShapeTest() {
        Stop[] stops = new Stop[] { new Stop(0, Color.web("#FF5F6D")), new Stop(1,Color.web("#FFC371"))};
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true,
                CycleMethod.NO_CYCLE, stops);
        Circle circle = new Circle(
                cs1.translateH(coordinate1.get(1)),
                cs1.translateV(coordinate1.get(2)),
                4 * ((H_END / 100.0) / FieldApp.getHscale()) * (H_END / 100.0),
                lg1);
        assertEquals(circle.getCenterX(),((Circle) cs1.makeShape(1,2)).getCenterX());
        assertEquals(circle.getCenterY(),((Circle) cs1.makeShape(1,2)).getCenterY());
        assertEquals(circle.getRadius(),((Circle) cs1.makeShape(1,2)).getRadius());
        assertEquals(circle.getFill(),((Circle) cs1.makeShape(1,2)).getFill());
    }
}
