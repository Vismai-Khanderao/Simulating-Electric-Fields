package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.*;
import persistence.Reader;
import persistence.Writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;

// Link between field system and GUI, all commands are taken in through this class which uses fxml file as base
public class AppController implements Initializable {
    public static final int H_START = 0;
    public static final int H_END = 900;
    public static final int V_START = 0;
    public static final int V_END = 900;
    public static final int CENTRE_H = (H_START + H_END) / 2;
    public static final int CENTRE_V = (V_START + V_END) / 2;
    private static final float HDIV_SCALE = FieldApp.getHscale() * 10;
    private static final float VDIV_SCALE = FieldApp.getVscale() * 10;
    private static final int HNUM_PER_DIV = 10;
    private static final int VNUM_PER_DIV = 10;

    //TODO: Implement sort to avoid twisted plane and seeing an infinitesimally thin plane
    //TODO: display shapes over each other
    //TODO: add bounds to shapes

    @FXML
    private ChoiceBox shapeChoice;

    @FXML
    private TextField charge;

    @FXML
    private TextField coord1x;
    @FXML
    private TextField coord1y;
    @FXML
    private TextField coord1z;
    @FXML
    private TextField coord2x;
    @FXML
    private TextField coord2y;
    @FXML
    private TextField coord2z;
    @FXML
    private TextField coord3x;
    @FXML
    private TextField coord3y;
    @FXML
    private TextField coord3z;

    @FXML
    private TextField numPoints;

    @FXML
    private TextField radius;

    @FXML
    private TextField removeIndex;

    @FXML
    private Text em;
    @FXML
    private Text ex;
    @FXML
    private Text ey;
    @FXML
    private Text ez;

    @Override
    // EFFECTS: runs each time scene is redrawn, makes call to display last calculated electric field value
    public void initialize(URL url, ResourceBundle rb) {
        displayEfieldValues();
    }

    @FXML
    // EFFECTS: uses user input to determine which shape is to be added and
    //          then call is made to add specified shape
    private void addShape(ActionEvent actionEvent) throws IOException {
        if (shapeChoice.getValue().equals("Point")) {
            addPoint();
        } else if (shapeChoice.getValue().equals("Rod")) {
            addRod();
        } else if (shapeChoice.getValue().equals("Plane")) {
            addPlane();
        } else if (shapeChoice.getValue().equals("CSphere")) {
            addCSphere();
        }
        redraw(actionEvent);
    }

    @FXML
    // EFFECTS: takes in user input and makes call to remove object in system
    private void removeShape(ActionEvent actionEvent) throws IOException {
        int i = Integer.parseInt(removeIndex.getText());
        Main.fieldApp.getSys().removeObject(i);
        redraw(actionEvent);
    }

    @FXML
    // EFFECTS: saves state of field system to FIELD_SYSTEM_FILE
    private void saveSystem(ActionEvent actionEvent) throws IOException {
        try {
            Writer writer = new Writer(new File(ui.FieldApp.FIELD_SYSTEM_FILE));
            for (ChargedObject c : Main.fieldApp.getSys().getChargedObjects()) {
                writer.write(c);
            }
            writer.close();
            System.out.println("System saved to file " + ui.FieldApp.FIELD_SYSTEM_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to save system to " + ui.FieldApp.FIELD_SYSTEM_FILE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        redraw(actionEvent);
    }

    @FXML
    // EFFECTS: loads system of charged objects from FIELD_SYSTEM_FILE, if that file exists;
    //          otherwise returns back to state before loadSystem was called
    private void loadSystem(ActionEvent actionEvent) throws IOException {
        try {
            ArrayList<ChargedObject> chargedObjects = Reader.readChargedObjects(new File(
                    ui.FieldApp.FIELD_SYSTEM_FILE));
            Main.fieldApp.setSys(new FieldSystem(chargedObjects));
        } catch (IOException e) {
            return;
        }
        redraw(actionEvent);
    }

    @FXML
    // EFFECTS: redraws scene but twice as closer
    private void zoomIn(ActionEvent actionEvent) throws IOException {
        FieldApp.setHscale(FieldApp.getHscale() / 2);
        FieldApp.setVscale(FieldApp.getVscale() / 2);
        redraw(actionEvent);
    }

    @FXML
    // EFFECTS: redraws scene but twice as further
    private void zoomOut(ActionEvent actionEvent) throws IOException {
        FieldApp.setHscale(FieldApp.getHscale() * 2);
        FieldApp.setVscale(FieldApp.getVscale() * 2);
        redraw(actionEvent);
    }

    @FXML
    // EFFECTS: changes axes on scene to X (on H axis) and Y (on V axis)
    private void changeToXY(ActionEvent actionEvent) throws IOException {
        FieldApp.setCurrentH("X");
        FieldApp.setCurrentV("Y");
        redraw(actionEvent);
    }

    @FXML
    // EFFECTS: changes axes on scene to Y (on H axis) and Z (on V axis)
    private void changeToYZ(ActionEvent actionEvent) throws IOException {
        FieldApp.setCurrentH("Y");
        FieldApp.setCurrentV("Z");
        redraw(actionEvent);
    }

    @FXML
    // EFFECTS: changes axes on scene to X (on H axis) and Z (on V axis)
    private void changeToXZ(ActionEvent actionEvent) throws IOException {
        FieldApp.setCurrentH("X");
        FieldApp.setCurrentV("Z");
        redraw(actionEvent);
    }

    @FXML
    // EFFECTS: calls to calculate electric field of system
    private void calculateEfield(ActionEvent actionEvent) throws IOException {
        Main.fieldApp.getSys().electricField();
        redraw(actionEvent);
    }

    // EFFECTS: outputs calculated electric field of system
    private void displayEfieldValues() {
        em.setText("Magnitude = " + Main.fieldApp.getSys().getEm() + " N / C");
        ex.setText("Ex = " + Main.fieldApp.getSys().getEx() + " N / C");
        ey.setText("Ey = " + Main.fieldApp.getSys().getEy() + " N / C");
        ez.setText("Ez = " + Main.fieldApp.getSys().getEz() + " N / C");
    }

    // MODIFIES: this
    // EFFECTS: takes in user inputs and makes call to add point to system
    private void addPoint() {
        ArrayList<Double> coord1 = new ArrayList<>();
        coord1.add(Double.parseDouble(coord1x.getText()));
        coord1.add(Double.parseDouble(coord1y.getText()));
        coord1.add(Double.parseDouble(coord1z.getText()));
        Main.fieldApp.getSys().addPoint(new Point(Double.parseDouble(charge.getText()),coord1));
    }

    // REQUIRES: n>=1
    // MODIFIES: this
    // EFFECTS: takes in user inputs and makes call to add rod to system
    private void addRod() {
        ArrayList<Double> coord1 = new ArrayList<>();
        coord1.add(Double.parseDouble(coord1x.getText()));
        coord1.add(Double.parseDouble(coord1y.getText()));
        coord1.add(Double.parseDouble(coord1z.getText()));
        ArrayList<Double> coord2 = new ArrayList<>();
        coord2.add(Double.parseDouble(coord2x.getText()));
        coord2.add(Double.parseDouble(coord2y.getText()));
        coord2.add(Double.parseDouble(coord2z.getText()));
        Main.fieldApp.getSys().addRod(new Rod(Double.parseDouble(charge.getText()),
                coord1,
                coord2,
                Integer.parseInt(numPoints.getText())));
    }

    // MODIFIES: this
    // EFFECTS: takes in user inputs and makes call to add plane to system
    private void addPlane() {
        ArrayList<Double> coord1 = new ArrayList<>();
        coord1.add(Double.parseDouble(coord1x.getText()));
        coord1.add(Double.parseDouble(coord1y.getText()));
        coord1.add(Double.parseDouble(coord1z.getText()));
        ArrayList<Double> coord2 = new ArrayList<>();
        coord2.add(Double.parseDouble(coord2x.getText()));
        coord2.add(Double.parseDouble(coord2y.getText()));
        coord2.add(Double.parseDouble(coord2z.getText()));
        ArrayList<Double> coord3 = new ArrayList<>();
        coord3.add(Double.parseDouble(coord3x.getText()));
        coord3.add(Double.parseDouble(coord3y.getText()));
        coord3.add(Double.parseDouble(coord3z.getText()));
        Main.fieldApp.getSys().addPlane(new Plane(Double.parseDouble(charge.getText()),
                coord1,
                coord2,
                coord3,
                Integer.parseInt(numPoints.getText())));
    }

    // MODIFIES: this
    // EFFECTS: takes in user inputs and makes call to add conducting sphere to system
    private void addCSphere() {
        ArrayList<Double> coord1 = new ArrayList<>();
        coord1.add(Double.parseDouble(coord1x.getText()));
        coord1.add(Double.parseDouble(coord1y.getText()));
        coord1.add(Double.parseDouble(coord1z.getText()));
        Main.fieldApp.getSys().addConductingSphere(new ConductingSphere(Double.parseDouble(charge.getText()),
                coord1,
                Double.parseDouble(radius.getText())));
    }

    // EFFECTS: Scene is redrawn
    private void redraw(ActionEvent actionEvent) throws IOException {
        Group axes = makeAxes(FieldApp.getCurrentH(), FieldApp.getCurrentV());
        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("AppBase.fxml"));

        Group shapes = new Group();

        int haxis = checkHaxis(FieldApp.getCurrentH());
        int vaxis = checkVaxis(FieldApp.getCurrentV());

        for (ChargedObject chargedObject : Main.fieldApp.getSys().getChargedObjects()) {
            shapes.getChildren().add(chargedObject.makeShape(haxis,vaxis));
        }

        Group app = new Group(root,shapes,axes);
        Scene newScene = ((Node)actionEvent.getSource()).getScene();
        newScene.setRoot(app);
        window.setScene(newScene);
        window.show();
    }

    // EFFECTS: returns coordinate index for which value is on the H axis
    private int checkHaxis(String currentH) {
        if (currentH.equals("X")) {
            return 0;
        } else if (currentH.equals("Y")) {
            return  1;
        } else {
            return  2;
        }
    }

    // EFFECTS: returns coordinate index for which value is on the V axis
    private int checkVaxis(String currentV) {
        if (currentV.equals("X")) {
            return 0;
        } else if (currentV.equals("Y")) {
            return  1;
        } else {
            return  2;
        }
    }

    // EFFECTS: returns group of objects to be drawn that represent axes
    public static Group makeAxes(String h, String v) {
        Group lines = makeLines(h,v);
        Group divs = makeDivs();

        return new Group(lines,divs);
    }

    // EFFECTS: returns group of objects to be drawn that represent axis lines
    private static Group makeLines(String h, String v) {
        Line hline = new Line(H_START,CENTRE_V,H_END,CENTRE_V);
        hline.setStroke(Color.WHITE);

        Line vline = new Line(CENTRE_H,V_START,CENTRE_H,V_END);
        vline.setStroke(Color.WHITE);

        Group hvlines = new Group(hline,vline);

        Line harrow1 = new Line(H_END - 10, CENTRE_V - 10, H_END, CENTRE_V);
        harrow1.setStroke(Color.WHITE);

        Line harrow2 = new Line(H_END - 10, CENTRE_V + 10, H_END, CENTRE_V);
        harrow2.setStroke(Color.WHITE);

        Line varrow1 = new Line(CENTRE_H - 10, V_START + 10, CENTRE_H, V_START);
        varrow1.setStroke(Color.WHITE);

        Line varrow2 = new Line(CENTRE_H + 10, V_START + 10, CENTRE_H, V_START);
        varrow2.setStroke(Color.WHITE);

        Text haxis = new Text(H_END - 40, CENTRE_V + 40, h);
        haxis.setFill(Color.WHITE);
        haxis.setFont(Font.font(haxis.getFont().getFamily(),30));
        Text vaxis = new Text(CENTRE_H - 40, V_START + 40, v);
        vaxis.setFill(Color.WHITE);
        vaxis.setFont(Font.font(vaxis.getFont().getFamily(),30));

        Group arrows = new Group(harrow1,harrow2,varrow1,varrow2);

        return new Group(hvlines,arrows,haxis,vaxis);
    }

    // EFFECTS: returns group of objects to be drawn that represent axis divisions (small lines)
    private static Group makeDivs() {
        ArrayList<ObservableList<Node>> listOfHComponents = makeListOfObservables();
        ArrayList<ObservableList<Node>> listOfVComponents = makeListOfObservables();

        for (int i = 1; i < 10; i++) {
            int hnumPixelShift = getHnumPixelShift(i);
            int vnumWidthPixelShift = getVnumWidthPixelShift(i);
            ArrayList<ObservableList<Node>> hcomponents = makeHComponents(listOfHComponents,i,hnumPixelShift);
            listOfHComponents.set(0,hcomponents.get(0));
            listOfHComponents.set(1,hcomponents.get(1));
            listOfHComponents.set(2,hcomponents.get(2));
            ArrayList<ObservableList<Node>> vcomponents = makeVComponents(listOfVComponents,i,5,
                                                                          vnumWidthPixelShift);
            listOfVComponents.set(0,vcomponents.get(0));
            listOfVComponents.set(1,vcomponents.get(1));
            listOfVComponents.set(2,vcomponents.get(2));
        }
        return addAllDivs(listOfHComponents.get(0),listOfHComponents.get(1),listOfHComponents.get(2),
                          listOfVComponents.get(0),listOfVComponents.get(1),listOfVComponents.get(2));
    }

    // EFFECTS: returns a list of ObservableList
    private static ArrayList<ObservableList<Node>> makeListOfObservables() {
        ArrayList<ObservableList<Node>> listOfComponents = new ArrayList<>();
        listOfComponents.add(FXCollections.observableArrayList());
        listOfComponents.add(FXCollections.observableArrayList());
        listOfComponents.add(FXCollections.observableArrayList());
        return listOfComponents;
    }

    // EFFECTS: returns array of objects to be drawn that
    //          represent axis division lines and numbers for H axis
    private static ArrayList<ObservableList<Node>> makeHComponents(ArrayList<ObservableList<Node>> listOfHComponents,
                                                                   int i,
                                                                   int hnumPixelShift) {
        ObservableList<Node> listOfHdivsLines = listOfHComponents.get(0);
        ObservableList<Node> listOfHgridLines = listOfHComponents.get(1);
        ObservableList<Node> listOfHnums = listOfHComponents.get(2);

        Line hdivLine = new Line(H_START + i * HDIV_SCALE, CENTRE_V - 10,
                H_START + i * HDIV_SCALE, CENTRE_V + 10);
        hdivLine.setStroke(Color.WHITE);
        listOfHdivsLines.add(hdivLine);

        Line hgridLine = new Line(H_START + i * HDIV_SCALE, V_START,
                H_START + i * HDIV_SCALE, V_END);
        hgridLine.setStroke(Color.LIGHTGRAY);
        listOfHgridLines.add(hgridLine);

        int hnumInt = (i - 5) * HNUM_PER_DIV;
        Text hnum = new Text(H_START - hnumPixelShift + i * HDIV_SCALE, CENTRE_V + 25,
                Double.toString(hnumInt * FieldApp.getHscale() / 9));
        hnum.setFill(Color.WHITE);
        listOfHnums.add(hnum);

        ArrayList<ObservableList<Node>> hcomponents = new ArrayList<>();
        hcomponents.add(listOfHdivsLines);
        hcomponents.add(listOfHgridLines);
        hcomponents.add(listOfHnums);

        return hcomponents;
    }

    // EFFECTS: returns array of objects to be drawn that
    //          represent axis division lines and numbers for V axis
    private static ArrayList<ObservableList<Node>> makeVComponents(ArrayList<ObservableList<Node>> listOfVComponents,
                                                                   int i,
                                                                   int vnumHeightPixelShift,
                                                                   int vnumWidthPixelShift) {
        ObservableList<Node> listOfVdivsLines = listOfVComponents.get(0);
        ObservableList<Node> listOfVgridLines = listOfVComponents.get(1);
        ObservableList<Node> listOfVnums = listOfVComponents.get(2);
        Line vdivLine = new Line(CENTRE_H - 10, V_START + i * VDIV_SCALE,
                CENTRE_H + 10, V_START + i * VDIV_SCALE);
        vdivLine.setStroke(Color.WHITE);
        listOfVdivsLines.add(vdivLine);
        Line vgridLine = new Line(H_START, V_START + i * VDIV_SCALE,H_END, V_START + i * VDIV_SCALE);
        vgridLine.setStroke(Color.LIGHTGRAY);
        listOfVgridLines.add(vgridLine);
        if (i != 5) {
            int vnumInt = (i - 5) * VNUM_PER_DIV;
            Text vnum = new Text(CENTRE_H - vnumWidthPixelShift,
                                 V_START + vnumHeightPixelShift + i * VDIV_SCALE,
                                    Double.toString(-vnumInt * FieldApp.getVscale() / 9));

            vnum.setFill(Color.WHITE);
            listOfVnums.add(vnum);
        }
        ArrayList<ObservableList<Node>> vcomponents = new ArrayList<>();
        vcomponents.add(listOfVdivsLines);
        vcomponents.add(listOfVgridLines);
        vcomponents.add(listOfVnums);
        return vcomponents;
    }

    // EFFECTS: returns pixel shift value for H axis numbers
    private static int getHnumPixelShift(int i) {
        if (i < 5) {
            return 13;
        } else if (i == 5) {
            return 18;
        } else {
            return 8;
        }
    }

    // EFFECTS: returns pixel shift value for V axis numbers
    private static int getVnumWidthPixelShift(int i) {
        if (i <= 5) {
            return 30;
        } else {
            return 36;
        }
    }

    // EFFECTS: returns group of all axis divisions added together
    private static Group addAllDivs(ObservableList<Node> listOfHdivsLines, ObservableList<Node> listOfHgridLines,
                                    ObservableList<Node> listOfHnums, ObservableList<Node> listOfVdivsLines,
                                    ObservableList<Node> listOfVgridLines, ObservableList<Node> listOfVnums) {
        Group allHdivs = new Group(listOfHdivsLines);
        Group allHgrids = new Group(listOfHgridLines);
        Group allHnums = new Group(listOfHnums);

        Group allVdivs = new Group(listOfVdivsLines);
        Group allVgrids = new Group(listOfVgridLines);
        Group allVnums = new Group(listOfVnums);

        return new Group(allHdivs,allHgrids,allHnums,allVdivs,allVgrids,allVnums);
    }
}