package com.frauas.huankiet.app;

import com.frauas.huankiet.app.db.DBMaster;
import com.frauas.huankiet.app.util.UIManager;
import javafx.application.Application;

import javafx.stage.Stage;
public class MainApp extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		DBMaster.startFlashCardSession();
		UIManager.setStage(primaryStage);
		primaryStage.setTitle("Flashcards");
		UIManager.switchScene("/fxml/main.fxml");
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception{
		DBMaster.endFlashCardSession();
	}

	public static void main(String[] args) {
		launch(args);
	}
}

