package com.frauas.huankiet.app.controller;

import com.frauas.huankiet.app.classes.cards.Card;
import com.frauas.huankiet.app.classes.decks.Deck;
import com.frauas.huankiet.app.util.UIManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.application.Platform;

import java.io.File;
import java.util.Optional;

public class DeckEditController {

    @FXML private Label deckNameLabel;
    @FXML private ListView<Card> cardListView;

    private Deck targetDeck;
    private ObservableList<Card> cardObservableList;

    @FXML
    public void initialize() {
        targetDeck = MainController.currentAdjustmentDeck;
        if (targetDeck == null) {
            deckNameLabel.setText("No Deck Loaded");
            return;
        }

        deckNameLabel.setText("Adjusting: " + targetDeck.getDeckName());
        refreshCardLayoutList();

        cardListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !cardListView.getSelectionModel().isEmpty()) {
                Card selectedCard = cardListView.getSelectionModel().getSelectedItem();
                openEditCardDialog(selectedCard);
            }
        });
    }

    private void openEditCardDialog(Card card) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit card");
        dialog.setHeaderText("Modify card details:");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextField frontField = new TextField(card.getFrontSide());
        frontField.setPrefWidth(300);

        TextField backField = new TextField(card.getBackSide());
        backField.setPrefWidth(300);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        grid.add(new Label("Front:"), 0, 0);
        grid.add(frontField, 1, 0);
        grid.add(new Label("Back:"), 0, 1);
        grid.add(backField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(frontField::requestFocus); // cursor on front text field

        dialog.showAndWait().ifPresent(result -> {
            if (result == saveButtonType) {
                card.setFrontSide(frontField.getText().trim());
                card.setBackSide(backField.getText().trim());

                cardListView.refresh();
            }
        });
    }
    private void refreshCardLayoutList() {
        cardObservableList = FXCollections.observableArrayList(targetDeck.getCards());
        cardListView.setItems(cardObservableList);

        cardListView.setCellFactory(param -> new ListCell<Card>() {
            @Override
            protected void updateItem(Card card, boolean empty) {
                super.updateItem(card, empty);
                if (empty || card == null) {
                    setText(null);
                } else {
                    String cardType = card.getClass().getSimpleName();
                    setText("[" + cardType + "] Front: " + card.getFrontSide() + " | Back: " + card.getBackSide());
                }
            }
        });
    }

    @FXML
    public void handleDeleteCard(ActionEvent event) {
        Card selected = cardListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showNotification(Alert.AlertType.WARNING, "Selection Required", "Please choose a card from the list to delete.");
            return;
        }

        targetDeck.getCards().remove(selected);
        refreshCardLayoutList();
    }

    @FXML
    public void handleAddRegularCard(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Regular Card");
        dialog.setHeaderText("Enter text for a standard text flashcard");

        ButtonType confirmButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane layoutGrid = new GridPane();
        layoutGrid.setHgap(10);
        layoutGrid.setVgap(10);
        layoutGrid.setPadding(new Insets(20, 10, 10, 10));

        TextField frontInput = new TextField();
        frontInput.setPromptText("Front question text...");
        TextField backInput = new TextField();
        backInput.setPromptText("Back answer text...");

        layoutGrid.add(new Label("Question:"), 0, 0);
        layoutGrid.add(frontInput, 1, 0);
        layoutGrid.add(new Label("Answer:"), 0, 1);
        layoutGrid.add(backInput, 1, 1);

        dialog.getDialogPane().setContent(layoutGrid);

        Optional<ButtonType> contextAction = dialog.showAndWait();
        if (contextAction.isPresent() && contextAction.get() == confirmButtonType) {
            String frontText = frontInput.getText().trim();
            String backText = backInput.getText().trim();

            if (frontText.isEmpty() || backText.isEmpty()) {
                showNotification(Alert.AlertType.ERROR, "Input Error", "Both text fields are required!");
                return;
            }

            targetDeck.addCard(frontText, backText);
            refreshCardLayoutList();
        }
    }

    @FXML
    public void handleAddImageCard(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Image Card");
        dialog.setHeaderText("Provide image and answer");

        ButtonType confirmButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane layoutGrid = new GridPane();
        layoutGrid.setHgap(10);
        layoutGrid.setVgap(10);
        layoutGrid.setPadding(new Insets(20, 10, 10, 10));

        TextField imagePathInput = new TextField();
        imagePathInput.setPromptText("Choose an image...");
        imagePathInput.setEditable(false); // Prevent manual typos, force them to use browse
        imagePathInput.setPrefWidth(200);

        Button browseButton = new Button("Browse...");
        browseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            // Open the file dialog
            File selectedFile = fileChooser.showOpenDialog(dialog.getDialogPane().getScene().getWindow());
            if (selectedFile != null) {
                // Save the absolute path of the selected image
                imagePathInput.setText(selectedFile.getAbsolutePath());
            }
        });

        // Group the field and button together in an HBox
        HBox imageInputBox = new HBox(10, imagePathInput, browseButton);

        TextField backDescriptionInput = new TextField();
        backDescriptionInput.setPromptText("Back answer/description...");

        layoutGrid.add(new Label("Image File (Front):"), 0, 0);
        layoutGrid.add(imageInputBox, 1, 0);
        layoutGrid.add(new Label("Back Description Text:"), 0, 1);
        layoutGrid.add(backDescriptionInput, 1, 1);

        dialog.getDialogPane().setContent(layoutGrid);

        Optional<ButtonType> contextAction = dialog.showAndWait();
        if (contextAction.isPresent() && contextAction.get() == confirmButtonType) {
            String imgPath = imagePathInput.getText().trim();
            String backDesc = backDescriptionInput.getText().trim();

            if (imgPath.isEmpty() || backDesc.isEmpty()) {
                showNotification(Alert.AlertType.ERROR, "Input Error", "All configuration fields must be provided.");
                return;
            }

            targetDeck.addCard(imgPath, backDesc, true);
            refreshCardLayoutList();
        }
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            UIManager.switchScene("/fxml/main.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNotification(Alert.AlertType type, String title, String contents) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contents);
        alert.showAndWait();
    }
}