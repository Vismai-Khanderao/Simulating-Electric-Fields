package persistence;

import model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// A reader that obtains information of objects in field system from a file
public class Reader {
    public static final String OBJECT_DELIMITER = ";";
    public static final String COORDINATE_DELIMITER = ",";

    // Dummy Constructor
    public Reader() {
        return;
    }

    // EFFECTS: returns a list of charged objects parsed from file; throws
    // IOException if an exception is raised when opening / reading from file
    public static ArrayList<ChargedObject> readChargedObjects(File file) throws IOException {
        List<String> fileContent = readFile(file);
        return parseContent(fileContent);
    }

    // EFFECTS: returns content of file as a list of strings, each string
    // containing the content of one row of the file
    private static List<String> readFile(File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }

    // EFFECTS: returns a list of charged objects parsed from list of strings
    // where each string contains data for one account
    private static ArrayList<ChargedObject> parseContent(List<String> fileContent) {
        ArrayList<ChargedObject> chargedObjects = new ArrayList<>();

        for (String line : fileContent) {
            ArrayList<String> lineComponents = splitLineString(line);
            chargedObjects.add(parseChargedObject(lineComponents));
        }

        return chargedObjects;
    }

    // EFFECTS: returns a list of strings obtained by splitting line on OBJECT_DELIMITER
    private static ArrayList<String> splitLineString(String line) {
        String[] splits = line.split(OBJECT_DELIMITER);
        return new ArrayList<>(Arrays.asList(splits));
    }

    // EFFECTS: returns a list of doubles obtained by splitting component on COORDINATE_DELIMITER
    private static ArrayList<Double> splitCoordinateString(String line) {
        String[] splits = line.split(COORDINATE_DELIMITER);
        splits[0] = splits[0].substring(1);
        splits[2] = splits[2].substring(0,splits[2].length() - 1);
        ArrayList<Double> doubleCoordinates = new ArrayList<>();
        for (int n = 0; n < 3; n++) {
            doubleCoordinates.add(Double.parseDouble(splits[n]));
        }
        return doubleCoordinates;
    }

    // REQUIRES: components have variable size where
    // element 0 always represents the charged object as a string
    // element 1 always represents the charge density/charge of object
    // element 2 always represents the number of calculation points
    // element 3 always represents the 1st coordinate of object
    // element 4 is the 2nd coordinate or radius in case of conducting sphere
    // element 5 is the 3rd coordinate
    // EFFECTS: returns an account constructed from components
    private static ChargedObject parseChargedObject(List<String> components) {
        System.out.println(components.get(0));
        double chargeDensity = Double.parseDouble(components.get(1));
        int numPoints = Integer.parseInt(components.get(2));
        ArrayList<Double> c1 = splitCoordinateString(components.get(3));

        if (components.get(0).equals("Point")) {
            return new Point(chargeDensity, c1);
        } else if (components.get(0).equals("Rod")) {
            ArrayList<Double> c2 = splitCoordinateString(components.get(4));
            return new Rod(chargeDensity, c1, c2, numPoints);
        } else if (components.get(0).equals("Plane")) {
            ArrayList<Double> c2 = splitCoordinateString(components.get(4));
            ArrayList<Double> c3 = splitCoordinateString(components.get(5));
            return new Plane(chargeDensity, c1, c2, c3, numPoints);
        } else {                                                                    //conducting sphere
            double radius = Double.parseDouble(components.get(4));
            return new ConductingSphere(chargeDensity, c1, radius);
        }
    }
}
