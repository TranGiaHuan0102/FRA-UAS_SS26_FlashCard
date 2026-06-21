package com.frauas.huankiet.app.controller;

import com.frauas.huankiet.app.classes.decks.Deck;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AddDeckController {

    @FXML private TextField nameField;
    @FXML private TextField frontField;
    @FXML private TextField backField;
    @FXML private Label countLabel;

    private Deck tempDeck;
    private boolean saved = false;

    @FXML
    public void initialize() {
        tempDeck = new Deck("New Deck");
    }

    @FXML
    public void handleBrowse(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(nameField.getScene().getWindow());
        if (file != null) {
            frontField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    public void handleAdd(ActionEvent event) {
        String front = frontField.getText().trim();
        String back = backField.getText().trim();

        if (!front.isEmpty() && !back.isEmpty()) {
            boolean isImage = front.toLowerCase().matches(".*\\.(png|jpe?g)$");
            tempDeck.addCard(front, back, isImage);

            countLabel.setText("Item count: " + tempDeck.getDeckSize());

            frontField.clear();
            backField.clear();
            frontField.requestFocus();
        }
    }

    @FXML
    public void handleSave(ActionEvent event) {
        String finalDeckName = nameField.getText().trim();
        if (finalDeckName.isEmpty()) {
            finalDeckName = "Untitled Deck";
        }
        tempDeck.setDeckName(finalDeckName);
        saved = true;

        Stage stage = (Stage) nameField.getScene().getWindow();  // close window after save
        stage.close();
    }

    // --- Getters so MainController can retrieve the data ---
    public Deck getCreatedDeck() {
        return tempDeck;
    }
    public boolean isSaved() {
        return saved;
    }
}