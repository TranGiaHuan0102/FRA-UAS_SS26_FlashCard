package flashcard.app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import flashcard.app.util.UIManager;
import flashcard.app.service.MockService;
import flashcard.app.deck.Deck;

public class MainController {


    public static Deck currentStudyDeck;

    @FXML
    private StackPane contentArea;
    @FXML
    private Button decksNavButton;
    @FXML
    private Button statsNavButton;
    @FXML
    private TextField searchField;
    @FXML
    private VBox deckViewContainer;
    @FXML
    private ListView<String> deckList;


    private MockService mockService;
    private ObservableList<String> masterDeckData;

    @FXML
    public void initialize() {

        mockService = new MockService();
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


        deckList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && deckList.getSelectionModel().getSelectedItem() != null) {

                handleStartStudy(null);
            }
        });


        showDecksView();
    }

    public void openDecks(ActionEvent e) throws Exception {
        UIManager.switchScene("/fxml/DeckView.fxml");
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

            UIManager.switchScene("/fxml/StudySession.fxml");
        } catch (Exception e) {
            System.err.println("Failed routing application layout state redirect profiles.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCreateDeck(ActionEvent actionEvent) {
    }
}