package com.plant;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;



public class Main extends Application {
	
	// ù ����
	@Override
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("firstLogin/View/login.fxml"));
		primaryStage.setScene(new Scene(root));
		primaryStage.getIcons().add(new Image("file:src/application/main/View/css/menu_plant_icon.png"));
		primaryStage.show();
		primaryStage.setResizable(false);
		primaryStage.setTitle("Plants Growing �α���");
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
