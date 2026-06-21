package com.frauas.huankiet.app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;


import com.frauas.huankiet.app.util.UIManager;
import com.frauas.huankiet.app.service.MockService;
import com.frauas.huankiet.app.deck.Deck;

public class MainController {

    public static Deck currentStudyDeck;
    public static Deck currentAdjustmentDeck;

    @FXML
    private StackPane contentArea;
    @FXML
    private TextField searchField;
    @FXML
    private VBox deckViewContainer;
    @FXML
    private ListView<String> deckList;

    private static MockService mockService; // make it static so that the session registers newly added card
    private ObservableList<String> masterDeckData;
    public static com.frauas.huankiet.app.service.MockService getMockService() {
        return mockService;
    }

    @FXML
    public void initialize() {
        if (mockService == null) {
            mockService = new MockService();
        }

        masterDeckData = FXCollections.observableArrayList();

        for (Deck deck : mockService.getDecks()) {
            masterDeckData.add(deck.getDeckName());
        }

        FilteredList<String> filteredData = new FilteredList<>(masterDeckData, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(deckName -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return deckName.toLowerCase().contains(lowerCaseFilter);
            });
        });

        deckList.setItems(filteredData);

        // cell factory to modify setting icon (cog) capability
        deckList.setCellFactory(lv -> new ListCell<String>() {
            private final HBox hbox = new HBox();
            private final Label label = new Label();
            private final Button cogButton = new Button("⚙");
            {
                hbox.setAlignment(Pos.CENTER_LEFT);
                HBox.setHgrow(label, Priority.ALWAYS);
                label.setMaxWidth(Double.MAX_VALUE);

                cogButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 16px; -fx-padding: 0 5 0 5;");
                hbox.getChildren().addAll(label, cogButton);

                cogButton.setOnAction(event -> {
                    String deckName = getItem();
                    if (deckName != null) {
                        for (Deck deck : mockService.getDecks()) {
                            if (deck.getDeckName().equalsIgnoreCase(deckName)) {
                                currentAdjustmentDeck = deck;
                                break;
                            }
                        }
                        try {
                            UIManager.switchScene("/fxml/deck_edit.fxml");
                        } catch (Exception e) {
                            System.err.println("Failed to load Deck Adjustment screen.");
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item);
                    setGraphic(hbox);
                }
            }
        });

        deckList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && deckList.getSelectionModel().getSelectedItem() != null) {
                handleStartStudy(null);
            }
        });

        showDecksView();
    }

    @FXML
    public void showDecksView(ActionEvent actionEvent) {
        if (deckViewContainer != null && !contentArea.getChildren().contains(deckViewContainer)) {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(deckViewContainer);
        }
    }

    public void showDecksView() {
        showDecksView(null);
    }

    @FXML
    public void showStatsView(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/stat.fxml"));
            javafx.scene.Parent root = loader.load();

            // Swap out the center content area with the dashboard
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);
        } catch (Exception e) {
            System.err.println("Failed to load Statistics Dashboard.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleStartStudy(ActionEvent actionEvent) {
        String selectedDeckName = deckList.getSelectionModel().getSelectedItem();

        if (selectedDeckName == null) {
            System.out.println("Please select a deck from the list first!");
            return;
        }

        for (Deck deck : mockService.getDecks()) {
            if (deck.getDeckName().equalsIgnoreCase(selectedDeckName)) {
                currentStudyDeck = deck;
                break;
            }
        }
        try {
            UIManager.switchScene("/fxml/study.fxml");
        } catch (Exception e) {
            System.err.println("Failed routing application layout state redirect profiles.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCreateDeck(ActionEvent actionEvent) {
        try {
            // Load the clean layout from FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_deck.fxml"));
            javafx.scene.Parent root = loader.load();

            // Set up the window
            Stage popupStage = new Stage();
            popupStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            popupStage.setTitle("Add New Deck");
            popupStage.setScene(new javafx.scene.Scene(root));

            popupStage.showAndWait();

            AddDeckController controller = loader.getController();
            if (controller.isSaved()) {
                Deck newDeck = controller.getCreatedDeck();

                mockService.getDecks().add(newDeck);
                masterDeckData.add(newDeck.getDeckName());
            }

        } catch (Exception e) {
            System.err.println("Failed to load Add Deck Dialog layout.");
            e.printStackTrace();
        }
    }
}