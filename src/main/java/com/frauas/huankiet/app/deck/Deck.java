package com.frauas.huankiet.app.deck;

import com.frauas.huankiet.app.cards.Card;
import com.frauas.huankiet.app.cards.ImageCard;
import com.frauas.huankiet.app.cards.BasicCard;
import com.frauas.huankiet.app.util.IDGenerator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Deck {
    private final String deckID;
    private final LocalDateTime creationDate;
    private String deckName;
    private int deckSize;
    private List<Card> cards;

    public Deck(String deckName) {
        this.deckName = deckName;
        this.deckID = IDGenerator.generate_ID();
        this.creationDate = LocalDateTime.now();
        this.deckSize = 0;
        this.cards = new ArrayList<>();
    }

    public void addCard(String front, String back) {
        try {
            this.cards.add(new BasicCard(front, back));
            this.deckSize++;
        } catch (Exception e) {
            System.out.println("Failed to add BasicCard: " + e.getMessage());
        }
    }

    public void addCard(String imagePath, String back, boolean isImageCard) {
        if (isImageCard) {
            try {
                this.cards.add(new ImageCard(imagePath, back));
                this.deckSize++;
            } catch (Exception e) {
                System.out.println("Failed to add ImageCard: " + e.getMessage());
            }
        } else {
            addCard(imagePath, back); // parse path as string if not available
        }
    }

    // old addCard method code (using placeholder), use if needed
    public void addCard(String cardType) {
        if ("IMAGE".equalsIgnoreCase(cardType)) {
            try {
                // Feeds placeholder text to the new constructor
                this.cards.add(new ImageCard("Placeholder.jpg", "Placeholder Description"));
                this.deckSize++;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            try {
                // Feeds placeholder text to the new constructor
                this.cards.add(new BasicCard("Placeholder Front", "Placeholder Back"));
                this.deckSize++;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }


    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public String getDeckID() {
        return deckID;
    }

    public int getDeckSize() {
        return deckSize;
    }

    public List<Card> getCards() {
        return cards;
    }
}