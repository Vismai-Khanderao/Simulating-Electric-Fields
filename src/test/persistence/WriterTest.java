package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class WriterTest {
    private static final String TEST_FILE = "./data/testWriteFieldSystemFile.txt";
    private Writer testWriter;
    private Point point1;
    private Rod rod1;
    private Plane plane1;
    private ConductingSphere cs1;
    private ArrayList<Double> coordinate1;
    private ArrayList<Double> coordinate2;
    private ArrayList<Double> coordinate3;
    private ArrayList<Double> coordinate4;

    @BeforeEach
    void runBefore() throws FileNotFoundException, UnsupportedEncodingException {
        testWriter = new Writer(new File(TEST_FILE));

        coordinate1 = new ArrayList<>();
        coordinate1.add(3.0);
        coordinate1.add(7.0);
        coordinate1.add(8.0);

        coordinate2 = new ArrayList<>();
        coordinate2.add(-16.0);
        coordinate2.add(0.0);
        coordinate2.add(2.0);

        coordinate3 = new ArrayList<>();
        coordinate3.add(5.0);
        coordinate3.add(4.0);
        coordinate3.add(-5.0);

        coordinate4 = new ArrayList<>();
        coordinate4.add(1.0);
        coordinate4.add(-3.0);
        coordinate4.add(5.0);

        point1 = new Point(3.5,coordinate1);
        plane1 = new Plane(8.0,coordinate1,coordinate2,coordinate3,700);
        rod1 = new Rod(-4.0,coordinate1,coordinate2,50);
        cs1 = new ConductingSphere(-3.0,coordinate4,6.5);
    }

    @Test
    void testWriteChargedObjects() {
        // save charged objects to file
        testWriter.write(point1);
        testWriter.write(plane1);
        testWriter.write(rod1);
        testWriter.write(cs1);
        testWriter.close();

        // now read them back in and verify that the accounts have the expected values
        try {
            List<ChargedObject> chargedObjects = Reader.readChargedObjects(new File(TEST_FILE));
            
            Point point1 = (Point) chargedObjects.get(0);
            Plane plane1 = (Plane) chargedObjects.get(1);
            Rod rod1 = (Rod) chargedObjects.get(2);
            ConductingSphere cs1 = (ConductingSphere) chargedObjects.get(3);

            assertEquals(3.5, point1.getCharge());
            assertEquals(coordinate1, point1.getCoordinate1());

            assertEquals(8.0,plane1.getCharge());
            assertEquals(coordinate1,plane1.getCoordinate1());
            assertEquals(coordinate2,plane1.getCoordinate2());
            assertEquals(coordinate3,plane1.getCoordinate3());
            assertEquals(700,plane1.getNumPoints());

            assertEquals(-4.0,rod1.getCharge());
            assertEquals(coordinate1,rod1.getCoordinate1());
            assertEquals(coordinate2,rod1.getCoordinate2());
            assertEquals(50,rod1.getNumPoints());

            assertEquals(-3.0, cs1.getCharge());
            assertEquals(coordinate4, cs1.getCoordinate1());
            assertEquals(6.5,cs1.getRadius());
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }
}