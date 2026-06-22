package com.frauas.huankiet.app.controller;

import com.frauas.huankiet.app.classes.decks.Deck;
import com.frauas.huankiet.app.db.DBMaster;
import com.frauas.huankiet.app.db.operations.DeckRepository;
import com.frauas.huankiet.app.db.operations.DeckRepositoryException;
import com.frauas.huankiet.app.util.UIManager;
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

public class MainController {

    public static Deck currentStudyDeck;
    protected static Deck currentAdjustmentDeck;

    @FXML
    private StackPane contentArea;
    @FXML
    private TextField searchField;
    @FXML
    private VBox deckViewContainer;

    // List of all decks in database
    private ObservableList<Deck> masterDeckData;

    // A display of the deck list, doesn't actually contain any deck objects
    @FXML private ListView<Deck> deckList;

    // Get the DeckRepository instance of this session
    private final DeckRepository dr = DBMaster.getDeckRepository();

    @FXML
    public void initialize() {
        masterDeckData = FXCollections.observableArrayList();

	// Load all currently existing decks in database
	try{
		masterDeckData.addAll(dr.findAll());
	} catch (DeckRepositoryException e) {System.err.println(e.getMessage());}

        FilteredList<Deck> filteredData = new FilteredList<>(masterDeckData, p -> true);

	// Search field for displayed decks
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(deck -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return deck.getDeckName().toLowerCase().contains(lowerCaseFilter);
            });
        });
        deckList.setItems(filteredData);

	// Put a cog icon next to every deck entry in search field
	// Click on it to target that deck for modifications
        deckList.setCellFactory(lv -> new ListCell<Deck>() {
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
			Deck deck = getItem();
			if (deck != null) {
				currentAdjustmentDeck = deck;
				try {UIManager.switchScene("/fxml/deck_edit.fxml");} 
				catch (Exception e) {
				    System.err.println("Failed to load Deck Adjustment screen.");
				    e.printStackTrace();
				}
                        }
                });
            }

            @Override
            protected void updateItem(Deck item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item.getDeckName());
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
	Deck selectedDeck = deckList.getSelectionModel().getSelectedItem();

        if (selectedDeck == null) {
            System.out.println("Please select a deck from the list first!");
            return;
        }
	currentStudyDeck = selectedDeck;
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

	    // Add the deck to current session and to database
            AddDeckController controller = loader.getController();
            if (controller.isSaved()) {
                Deck newDeck = controller.getCreatedDeck();
		try{
			dr.insert(newDeck);
                	masterDeckData.add(newDeck);
		}
		catch (DeckRepositoryException e){System.err.println(e.getMessage());}
            }

        } catch (Exception e) {
            System.err.println("Failed to load Add Deck Dialog layout.");
            e.printStackTrace();
        }
    }
}