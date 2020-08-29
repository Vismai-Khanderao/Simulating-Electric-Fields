package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static FieldApp fieldApp = new FieldApp();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group axes = AppController.makeAxes("X", "Y");
        Parent root = FXMLLoader.load(getClass().getResource("AppBase.fxml"));
        Group app = new Group(root,axes);
        primaryStage.setTitle("Electric Field Calculator");
        primaryStage.setScene(new Scene(app, 1600, 900));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
