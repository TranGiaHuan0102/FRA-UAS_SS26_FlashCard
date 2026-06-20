package com.frauas.huankiet.app;

import javafx.application.Application;
import javafx.stage.Stage;
import com.frauas.huankiet.app.util.UIManager;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        UIManager.setStage(primaryStage);
        primaryStage.setTitle("Flashcards");
        UIManager.switchScene("/fxml/main.fxml");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

