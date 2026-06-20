package com.frauas.huankiet.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.paint.Color;
import com.frauas.huankiet.app.util.UIManager;
import com.frauas.huankiet.app.cards.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StudySessionController {

    @FXML private Label frontTextLabel;
    @FXML private TextFlow feedbackFlow;
    @FXML private TextField answerField;
    @FXML private Button backButton;


    private List<Card> cards = new ArrayList<>();
    private String correctBackText = "";

    private boolean isRevealed = false;
    private final Random random = new Random();

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
        } else {
            loadRandomCard();
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
            resultText.setFill(Color.web("#27ae60"));
        } else {
            resultText.setFill(Color.web("#ff0000"));
        }

        feedbackFlow.getChildren().add(resultText);

        isRevealed = true;

        answerField.clear();
        answerField.setEditable(false);
    }

    private void loadRandomCard() {

        if (cards == null || cards.isEmpty()) {
            frontTextLabel.setText("No cards found in this deck.");
            correctBackText = "";
            answerField.setEditable(false);
            answerField.setPromptText("Go back and select another deck...");
            return;
        }

        int randomIndex = random.nextInt(cards.size());
        Card card = cards.get(randomIndex);

        frontTextLabel.setText(card.getFrontSide());
        correctBackText = card.getBackSide();

        feedbackFlow.getChildren().clear();
        answerField.setEditable(true);
        answerField.setPromptText("Type your answer and press Enter...");
        answerField.clear();
        answerField.requestFocus();

        isRevealed = false;
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