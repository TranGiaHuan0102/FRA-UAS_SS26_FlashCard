package com.frauas.huankiet.app.classes.cards;
public class BasicCard extends Card{
	private String backText;
	
	protected BasicCard(String frontText, String backText) {
		this.frontText = frontText;
		this.backText = backText;
	}

	// Getters and Setters
	public String getbackText(){return this.backText;}
	public void setbackText(String newBackSide){this.backText = newBackSide;} 
}
