package com.frauas.huankiet.app.controller;

import com.frauas.huankiet.app.classes.decks.Deck;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddDeckController {

    @FXML private TextField nameField;
    private Deck tempDeck;
    private boolean saved = false;

    @FXML
    public void initialize() {
        tempDeck = new Deck("New Deck");
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