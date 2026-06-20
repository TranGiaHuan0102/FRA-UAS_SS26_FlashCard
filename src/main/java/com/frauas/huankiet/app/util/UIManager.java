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

        // load the new layout from the FXML
        javafx.scene.Parent root = FXMLLoader.load(UIManager.class.getResource(fxml));

        // swap only the contents of a scene.
        // E.g. main UI -> deck edit -> main UI with added content (instead of load from scratch)
        if (stage.getScene() != null) {
            stage.getScene().setRoot(root);
        } else {
            // create the scene from scratch if first time running
            Scene scene = new Scene(root);
            stage.setScene(scene);
        }
    }
}