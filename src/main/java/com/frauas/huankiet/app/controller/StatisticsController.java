package com.frauas.huankiet.app.controller;

import com.frauas.huankiet.app.classes.cards.Card;
import com.frauas.huankiet.app.classes.decks.Deck;
import com.frauas.huankiet.app.util.StatsTracker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class StatisticsController {

    @FXML private Label newCardsLabel;
    @FXML private Label revisedCardsLabel;
    @FXML private ListView<Card> hardCardsList;

    @FXML
    public void initialize() {
        newCardsLabel.setText(String.valueOf(StatsTracker.newCardsStudiedToday)); // basic stats
        revisedCardsLabel.setText(String.valueOf(StatsTracker.oldCardsRevisedToday));

        ObservableList<Card> problemCards = FXCollections.observableArrayList(); // check for hard cards

        if (MainController.getMockService() != null) {
            for (Deck deck : MainController.getMockService().getDecks()) {
                for (Card card : deck.getCards()) {
                    if (card.isHard()) {
                        problemCards.add(card);
                    }
                }
            }
        }
        hardCardsList.setItems(problemCards);

        // list hard cards in a table
        hardCardsList.setCellFactory(param -> new ListCell<Card>() {
            @Override
            protected void updateItem(Card card, boolean empty) {
                super.updateItem(card, empty);
                if (empty || card == null) {
                    setText(null);
                } else {
                    setText("⚠️ " + card.getFrontSide() + " (Answer: " + card.getBackSide() + ")");
                    setStyle("-fx-text-fill: #c0392b; -fx-font-weight: bold;");
                }
            }
        });
    }
}