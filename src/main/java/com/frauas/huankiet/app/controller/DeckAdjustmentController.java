package flashcard.app.controller;

import flashcard.app.card.Card;
import flashcard.app.deck.Deck;
import flashcard.app.util.UIManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class DeckAdjustmentController {

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

        // Direct removal from the backing array reference list
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
        dialog.setHeaderText("Enter details. Note: Image filename parameter must end with .jpg, .jpeg, or .png");

        ButtonType confirmButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane layoutGrid = new GridPane();
        layoutGrid.setHgap(10);
        layoutGrid.setVgap(10);
        layoutGrid.setPadding(new Insets(20, 10, 10, 10));

        TextField imagePathInput = new TextField();
        imagePathInput.setPromptText("e.g., chart.png");
        TextField backDescriptionInput = new TextField();
        backDescriptionInput.setPromptText("Back answer/description...");

        layoutGrid.add(new Label("Image Filename (Front):"), 0, 0);
        layoutGrid.add(imagePathInput, 1, 0);
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

            // Passes Image as Front (arg 1) and Text as Back (arg 2)
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