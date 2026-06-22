package com.frauas.huankiet.app.controller;

import com.frauas.huankiet.app.classes.cards.*;
import com.frauas.huankiet.app.db.DBMaster;
import com.frauas.huankiet.app.db.operations.CardRepository;
import com.frauas.huankiet.app.db.operations.CardRepositoryException;
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
	private final ObservableList<Card> problemCards = FXCollections.observableArrayList(); // check for hard cards
	private final CardRepository cr = DBMaster.getCardRepository();

	@FXML
	public void initialize() {
		newCardsLabel.setText(String.valueOf(StatsTracker.newCardsStudiedToday)); // basic stats
		revisedCardsLabel.setText(String.valueOf(StatsTracker.oldCardsRevisedToday));

		// Add all hard cards to list
		try{
			for (Card c : cr.findbyDifficulity(true)){problemCards.add(c);}
		}
		catch (CardRepositoryException e){System.err.println(e.getMessage());}

		hardCardsList.setItems(problemCards);

		// list hard cards in a table
		hardCardsList.setCellFactory(param -> new ListCell<Card>() {
			@Override
			protected void updateItem(Card card, boolean empty) {
				super.updateItem(card, empty);
				if (empty || card == null) {
				    setText(null);
				} 
				else {
					if (card instanceof BasicCard basic){
						setText("⚠️ " + basic.getfrontText() + " (Answer: " + basic.getbackText() + ")");
						setStyle("-fx-text-fill: #c0392b; -fx-font-weight: bold;");
					}
					else if (card instanceof ImageCard image){
						setText("⚠️ " + image.getImgURL() + " (Answer: " + image.getAnswerText() + ")");
						setStyle("-fx-text-fill: #c0392b; -fx-font-weight: bold;");
					}
					else{
						setText("⚠️ UNKNOWN CARD TYPE");
					}
				}
			}
		});
	}
}