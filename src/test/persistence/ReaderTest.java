package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ReaderTest {
    // Dummy constructor test
    @Test
    void testConstructor() {
        new Reader();
    }

    @Test
    void testParseChargedObjectsFile() {
        try {
            List<ChargedObject> chargedObjects = Reader.readChargedObjects(new File("./data/testReadFieldSystemFile.txt"));

            Point point1 = (Point) chargedObjects.get(0);
            Rod rod1 = (Rod) chargedObjects.get(1);
            Plane plane1 = (Plane) chargedObjects.get(2);
            ConductingSphere cs1 = (ConductingSphere) chargedObjects.get(3);

            ArrayList<Double> coordinate1 = new ArrayList<>();
            coordinate1.add(3.0);
            coordinate1.add(4.0);
            coordinate1.add(5.0);

            ArrayList<Double> coordinate2 = new ArrayList<>();
            coordinate2.add(-10.0);
            coordinate2.add(0.0);
            coordinate2.add(0.0);

            ArrayList<Double> coordinate3 = new ArrayList<>();
            coordinate3.add(5.0);
            coordinate3.add(-5.0);
            coordinate3.add(-5.0);

            ArrayList<Double> coordinate4 = new ArrayList<>();
            coordinate4.add(-1.0);
            coordinate4.add(3.0);
            coordinate4.add(4.0);

            assertEquals(5.0, point1.getCharge());
            assertEquals(coordinate1, point1.getCoordinate1());

            assertEquals(-8.0,rod1.getCharge());
            assertEquals(coordinate1,rod1.getCoordinate1());
            assertEquals(coordinate2,rod1.getCoordinate2());
            assertEquals(1000,rod1.getNumPoints());

            assertEquals(2.0,plane1.getCharge());
            assertEquals(coordinate1,plane1.getCoordinate1());
            assertEquals(coordinate2,plane1.getCoordinate2());
            assertEquals(coordinate3,plane1.getCoordinate3());
            assertEquals(20000,plane1.getNumPoints());

            assertEquals(0.5, cs1.getCharge());
            assertEquals(coordinate4, cs1.getCoordinate1());
            assertEquals(4.0,cs1.getRadius());

        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    void testIOException() {
        try {
            Reader.readChargedObjects(new File("./path/does/not/exist/testFieldSystem.txt"));
        } catch (IOException e) {
            // expected
        }
    }
}