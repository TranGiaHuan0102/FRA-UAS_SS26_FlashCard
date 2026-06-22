package com.frauas.huankiet.app.classes.decks;
import java.time.LocalDateTime;

public class Deck {
	// Fixed variables	
	private String deckName;

	// These fields are updated when deck are added to DB
	private Long deckID;
	private LocalDateTime creationDate;

	// Constructor
	public Deck(String deckName) {
		this.deckName = deckName;
	}

	// Getters
	public String getDeckName() {return this.deckName;}
	public Long getDeckID(){return this.deckID;}
	public LocalDateTime getCreationDate(){return this.creationDate;}	

	// Setters
	public void setDeckID(Long deckID) {
		if (this.deckID != null){
			throw new IllegalStateException("deckID already set, cannot be modified!");}
		this.deckID = deckID;
	}
	
	public void setDeckName(String deckName){this.deckName = deckName;}

	public void setCreationDate(LocalDateTime creationDate) {
		if (this.creationDate != null) {
			throw new IllegalStateException("creationDate is already set and cannot be changed.");}
		this.creationDate = creationDate;
	}
}