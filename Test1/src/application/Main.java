package application;

import viewcontroller.HomeViewController;
import viewcontroller.DefectViewController;
import viewcontroller.DefinitionsViewController;
import viewcontroller.EffortViewController;
import viewcontroller.LogViewController;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override public void start(Stage primaryStage){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/viewcontroller/home.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("File Selection");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
	public static void main(String[] args) {
		System.out.println("hello world");
		launch(args);
	}
}
