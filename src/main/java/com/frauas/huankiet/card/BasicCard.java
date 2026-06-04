package com.frauas.huankiet.card;

public class BasicCard extends Card {

	public BasicCard() throws Exception{
		super();
		createCard();
	}

	@Override
	public void createCard() throws Exception{
		String frontText = "Max Mustermann";
		String backText = "Erica Mustermann";
		this.frontSide = frontText;
		this.backSide = backText;
	}

	// Getters and Setters
	@Override
	public String getFrontSide(){return this.frontSide;}
	@Override
	public String getBackSide(){return this.backSide;}
	@Override
	public void setFrontSide(String newFrontSide){this.frontSide = newFrontSide;}
	@Override
	public void setBackSide(String newBackSide){this.backSide = newBackSide;} 
}
