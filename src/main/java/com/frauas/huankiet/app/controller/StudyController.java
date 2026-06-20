package com.frauas.huankiet.app.controller;
import com.frauas.huankiet.app.util.UIManager;
import com.frauas.huankiet.app.cards.Card;
import com.frauas.huankiet.app.cards.ImageCard;
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

    @FXML private Label frontTextLabel;
    @FXML private HBox feedbackFlow;
    @FXML private TextField answerField;
    @FXML private Button backButton;
    @FXML private javafx.scene.layout.HBox ratingBox;

    private List<Card> cards = new ArrayList<>();
    private String correctBackText = "";

    private boolean isRevealed = false;
    private final Random random = new Random();

    private Card currentCard;

    @FXML
    public void initialize() {

        if (MainController.currentStudyDeck != null) {
            this.cards = MainController.currentStudyDeck.getCards();
        }

        answerField.setOnAction(event -> handleEnterPress());

        loadRandomCard();
    }

    private void handleEnterPress() {
        if (!isRevealed) {
            revealAnswer();
        }
    }

    private void loadRandomCard() {

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
        if (currentCard instanceof flashcard.app.card.ImageCard) {
            frontTextLabel.setText("");
            try {
                String path = currentCard.getFrontSide();
                if (!path.startsWith("file:") && !path.startsWith("http")) {
                    path = new java.io.File(path).toURI().toString();
                }
                javafx.scene.image.Image img = new javafx.scene.image.Image(path);
                javafx.scene.image.ImageView imgView = new javafx.scene.image.ImageView(img);
                imgView.setFitWidth(350);
                imgView.setPreserveRatio(true);
                frontTextLabel.setGraphic(imgView);
            } catch (Exception e) {
                frontTextLabel.setGraphic(null);
                frontTextLabel.setText("[Error loading image]");
            }
        } else {
            frontTextLabel.setGraphic(null);
            frontTextLabel.setText(currentCard.getFrontSide());
        }

        correctBackText = currentCard.getBackSide();
        feedbackFlow.getChildren().clear();
        answerField.setEditable(true);
        answerField.clear();
        answerField.requestFocus();
        isRevealed = false;

        if (ratingBox != null) {
            ratingBox.setVisible(false);
        }
    }

    private void revealAnswer() {
        if (cards == null || cards.isEmpty()) {
            return;
        }

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

        isRevealed = true;

        answerField.clear();
        answerField.setEditable(false);

        // --- NEW: Reveal the Easy/Hard buttons at the bottom ---
        if (ratingBox != null) {
            ratingBox.setVisible(true);
        }
    }

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

        // Apply SRS Logic
        if (isEasy) {
            currentCard.setHard(false);
            currentCard.setDelayOffset(25); // Push back 25 cards
        } else {
            currentCard.setHard(true);
            currentCard.setDelayOffset(5); // Show again soon
        }

        loadRandomCard(); // load next
    }

    public void setCardData(String front, String back) {
        this.frontTextLabel.setText(front);
        this.correctBackText = back;
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            UIManager.switchScene("/fxml/main.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}