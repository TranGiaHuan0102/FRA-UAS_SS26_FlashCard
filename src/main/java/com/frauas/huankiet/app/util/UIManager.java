package com.frauas.huankiet.app.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UIManager {
    private static Stage stage;

    public static void setStage(Stage nextStage) {
        stage = nextStage;
    }

    public static void switchScene(String fxml) throws Exception {
        if (stage == null) {
            throw new IllegalStateException("Stage reference missing in UIManager.");
        }
        Scene scene = new Scene(
                FXMLLoader.load(
                        UIManager.class.getResource(fxml)
                )
        );
        stage.setScene(scene);
    }
}