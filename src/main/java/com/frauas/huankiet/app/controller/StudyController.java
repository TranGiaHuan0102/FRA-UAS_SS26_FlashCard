package com.frauas.huankiet.app.controller;
import com.frauas.huankiet.app.util.StatsTracker;
import com.frauas.huankiet.app.util.UIManager;
import com.frauas.huankiet.app.classes.cards.*;
import com.frauas.huankiet.app.classes.decks.*;
import com.frauas.huankiet.app.db.DBMaster;
import com.frauas.huankiet.app.db.operations.CardRepository;
import com.frauas.huankiet.app.db.operations.CardRepositoryException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StudyController {

    @FXML private HBox feedbackFlow;
    @FXML private TextField answerField;
    @FXML private Button backButton;
    @FXML private javafx.scene.layout.HBox ratingBox;

    // Get an instance of CardRepository
    private final CardRepository cr = DBMaster.getCardRepository();
	
    // Study deck and its cards
    private Deck targetDeck;
    private List<Card> cards = new ArrayList<>();
    
    
    private Card currentCard;	// Card that is currently used in the test 
    @FXML private Label frontTextLabel; // The side shown to user 
    private String correctBackText = "";	// The correct answer, extracted from the hidden side

    private boolean isRevealed = false;		// By default, the answer is hidden, reveal answer with Enter
    private final Random random = new Random();

    // Load a random card from the current deck to test the user
    @FXML
    public void initialize() {
        if (MainController.currentStudyDeck != null) {
		this.targetDeck = MainController.currentStudyDeck;
		try{this.cards = cr.findByDeck(targetDeck);}
		catch (CardRepositoryException e){System.err.println(e.getMessage());}
        }
        answerField.setOnAction(event -> handleEnterPress());
        loadRandomCard();
    }

    // Implementation of the random function, as well as how cards should be rendered
    private void loadRandomCard() {
	// Setup parameters	
        feedbackFlow.getChildren().clear();
        answerField.setEditable(true);
        answerField.clear();
        answerField.requestFocus();
        isRevealed = false;
        if (ratingBox != null) {ratingBox.setVisible(false);}

        if (cards == null || cards.isEmpty()) {
            frontTextLabel.setGraphic(null);
            frontTextLabel.setText("No cards found in this deck.");
            //correctBackText = "";
            answerField.setEditable(false);
            answerField.setPromptText("Go back and select another deck...");
            return;
        }

        // 1. Decrement the delay offset for all cards in the deck
        for (Card c : cards) {
            if (c.getDelayOffset() > 0) {
                c.setDelayOffset(c.getDelayOffset() - 1);
		try{cr.updateCardStats(c);}
		catch (CardRepositoryException e){System.err.println(e.getMessage());}
            }
        }

        // 2. Filter which cards are actually allowed to be shown right now
        java.util.List<Card> availableCards = new java.util.ArrayList<>();
        java.util.List<Card> hardCards = new java.util.ArrayList<>();

        for (Card c : cards) {
            if (c.getDelayOffset() <= 0) {
                availableCards.add(c);
                if (c.isHard()) hardCards.add(c);
            }
        }

        // 3. Selection Algorithm
        if (!hardCards.isEmpty()) {
            // Priority 1: Hard cards that are due
            currentCard = hardCards.get(random.nextInt(hardCards.size()));
        } else if (!availableCards.isEmpty()) {
            // Priority 2: Any other card that is due
            currentCard = availableCards.get(random.nextInt(availableCards.size()));
        } else {
            // Priority 3: If all cards are delayed, force the one with the lowest delay
            // so the session doesn't hang.
            currentCard = cards.stream()
                    .min(java.util.Comparator.comparingInt(Card::getDelayOffset))
                    .orElse(cards.get(0));
        }

        // Render logic
	// If currentCard is an ImageCard
        if (currentCard instanceof ImageCard image) {
            try {
                String path = image.getImgURL();	// Get image URL
                if (!path.startsWith("file:") && !path.startsWith("http")) {
                    path = new java.io.File(path).toURI().toString();
                }
                javafx.scene.image.Image img = new javafx.scene.image.Image(path);
                javafx.scene.image.ImageView imgView = new javafx.scene.image.ImageView(img);
                imgView.setFitWidth(350);
                imgView.setPreserveRatio(true);
                frontTextLabel.setGraphic(imgView);	// Show image as prompt, ask for text answer
            	frontTextLabel.setText("");
            }
	    catch (Exception e) {
                frontTextLabel.setGraphic(null);
                frontTextLabel.setText("[Error loading image]");
            }
	    correctBackText = image.getAnswerText();	// Text answer at the back of ImageCard (messed up earlier, oops)
        }
	// If instead a BasicCard
	else if (currentCard instanceof BasicCard basic) {
		// Show text as prompt
		frontTextLabel.setGraphic(null);
		frontTextLabel.setText(basic.getfrontText());
		correctBackText = basic.getbackText();
        }
    }
    
    // If the user press Enter, reveal the answer if not already
    private void handleEnterPress() {
        if (!isRevealed) {
            revealAnswer();
        }
    }
   
    private void revealAnswer() {
        if (cards == null || cards.isEmpty()) {
            return;
        }
	
	// Get user's answer and compare to card's answer
        String typedAnswer = answerField.getText();
        feedbackFlow.getChildren().clear();
        boolean isCompletelyCorrect = typedAnswer.trim().equalsIgnoreCase(correctBackText.trim());

        Text resultText = new Text(correctBackText);
        resultText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-font-family: 'Monospaced';");

        if (isCompletelyCorrect) {
            resultText.setFill(javafx.scene.paint.Color.web("#27ae60")); // Green
        } else {
            resultText.setFill(javafx.scene.paint.Color.web("#ff0000")); // Red
        }

        feedbackFlow.getChildren().add(resultText);

	// Mark card as revealed
        isRevealed = true;

        answerField.clear();
        answerField.setEditable(false);

        if (ratingBox != null) {
            ratingBox.setVisible(true);
        }
    }

    // Easy and Hard Buttons to modify testing behaviour
    @FXML
    public void handleEasy(javafx.event.ActionEvent event) {
        handleRating(true);
    }

    @FXML
    public void handleHard(javafx.event.ActionEvent event) {
        handleRating(false);
    }

    private void handleRating(boolean isEasy) {
        // Track Statistics
        if (currentCard.isNewCard()) {
            StatsTracker.newCardsStudiedToday++;
            currentCard.setNewCard(false);
        } else {
            StatsTracker.oldCardsRevisedToday++;
        }

        if (isEasy) {
            currentCard.setHard(false);
            currentCard.setDelayOffset(25); // if ez, review card after 25 other cards
        } else {
            currentCard.setHard(true);
            currentCard.setDelayOffset(5); // if hard, review sooner after 5 cards
        }

	// Update card status in database
	try{cr.updateCardStats(currentCard);}
	catch (CardRepositoryException e){System.err.println(e.getMessage());}

        loadRandomCard(); // load next
    }

    public void setCardData(String front, String back) {
        this.frontTextLabel.setText(front);
        this.correctBackText = back;
    }

    // Return to main.xml via back button
    @FXML
    public void handleBack(ActionEvent event) {
        try {
            UIManager.switchScene("/fxml/main.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}