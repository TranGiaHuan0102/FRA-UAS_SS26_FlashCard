package com.frauas.huankiet.app.controller;

import com.frauas.huankiet.app.cards.Card;
import com.frauas.huankiet.app.deck.Deck;
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
        // 1. Load basic stats
        newCardsLabel.setText(String.valueOf(StatsTracker.newCardsStudiedToday));
        revisedCardsLabel.setText(String.valueOf(StatsTracker.oldCardsRevisedToday));

        // 2. Scan global MockService database for hard cards
        ObservableList<Card> problemCards = FXCollections.observableArrayList();

        // Grab the static database instance from MainController
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

        // Format how they look in the list
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