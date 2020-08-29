package ui;

import model.*;
import java.util.ArrayList;

// Stores information for AppController as AppController is reinitialized on each redraw
public class FieldApp {
    private static String currentH;
    private static String currentV;
    private static float hscale = 9;
    private static float vscale = 9;
    public static final String FIELD_SYSTEM_FILE = "./data/fieldSystem.txt";
    private FieldSystem sys;

    // EFFECTS: initialises axes, scale, and field system
    public FieldApp() {
        currentH = "X";
        currentV = "Y";
        sys = new FieldSystem(new ArrayList<>());
    }

    public static void setHscale(float hscale) {
        FieldApp.hscale = hscale;
    }

    public static float getHscale() {
        return hscale;
    }

    public static void setVscale(float vscale) {
        FieldApp.vscale = vscale;
    }

    public static float getVscale() {
        return vscale;
    }

    public static void setCurrentV(String currentV) {
        FieldApp.currentV = currentV;
    }

    public static String getCurrentV() {
        return currentV;
    }

    public static void setCurrentH(String currentH) {
        FieldApp.currentH = currentH;
    }

    public static String getCurrentH() {
        return currentH;
    }

    public FieldSystem getSys() {
        return sys;
    }

    public void setSys(FieldSystem sys) {
        this.sys = sys;
    }
}
