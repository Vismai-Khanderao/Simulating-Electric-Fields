package model;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class RodTest {
    private Rod rod1;
    private Rod rod2;
    private ArrayList<Double> coordinate1;
    private ArrayList<Double> coordinate2;
    private ArrayList<Double> coordinate3;
    private ArrayList<Double> coordinate4;

    @BeforeEach
    void runBefore() {
        coordinate1 = new ArrayList<>();
        coordinate1.add(-5.0);
        coordinate1.add(0.0);
        coordinate1.add(0.0);

        coordinate2 = new ArrayList<>();
        coordinate2.add(-10.0);
        coordinate2.add(0.0);
        coordinate2.add(0.0);

        coordinate3 = new ArrayList<>();
        coordinate3.add(-5.0);
        coordinate3.add(-5.0);
        coordinate3.add(0.0);

        coordinate4 = new ArrayList<>();
        coordinate4.add(-5.0);
        coordinate4.add(5.0);
        coordinate4.add(0.0);

        rod1 = new Rod(4,coordinate1,coordinate2,1000);
        rod2 = new Rod(-3,coordinate3,coordinate4,1000);
    }

    @Test
    void testConstructor() {
        ArrayList<Double> deltaXYZrod = new ArrayList<>();
        deltaXYZrod.add((coordinate2.get(0) - coordinate1.get(0)) / 1000);
        deltaXYZrod.add((coordinate2.get(1) - coordinate1.get(1)) / 1000);
        deltaXYZrod.add((coordinate2.get(2) - coordinate1.get(2)) / 1000);

        assertEquals(4,rod1.getCharge());
        assertEquals(coordinate1,rod1.getCoordinate1());
        assertEquals(coordinate2,rod1.getCoordinate2());
        assertEquals(deltaXYZrod,rod1.getDeltaXYZrod());
    }

    @Test
    void exyzFieldTest() {
        assertEquals(3.5950207e9,rod1.exyzField().get(0),0.00001e9);
        assertEquals(0,rod1.exyzField().get(1),0.00001e9);
        assertEquals(0,rod1.exyzField().get(2),0.00001e9);

        assertEquals(-7.62619e9,rod2.exyzField().get(0),0.00001e9);
        assertEquals(0,rod2.exyzField().get(1),0.00001e9);
        assertEquals(0,rod2.exyzField().get(2),0.00001e9);
    }

    @Test
    void makeShapeTest() {
        Line line = new Line(
                rod1.translateH(coordinate1.get(1)),
                rod1.translateV(coordinate1.get(2)),
                rod1.translateH(coordinate2.get(1)),
                rod1.translateV(coordinate2.get(2)));
        Stop[] stops = new Stop[] { new Stop(0, Color.web("#3494E6")), new Stop(1,Color.web("#EC6EAD"))};
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true,
                CycleMethod.NO_CYCLE, stops);
        line.setStroke(lg1);
        Line actualLine = (Line) rod1.makeShape(1,2);
        assertEquals(line.getStartX(),actualLine.getStartX());
        assertEquals(line.getStartY(),actualLine.getStartY());
        assertEquals(line.getEndX(),actualLine.getEndX());
        assertEquals(line.getEndY(),actualLine.getEndY());
        assertEquals(line.getStroke(),actualLine.getStroke());
    }
}
