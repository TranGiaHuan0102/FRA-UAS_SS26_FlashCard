package com.frauas.huankiet.app.classes.cards;
public class BasicCard extends Card{
	private final String frontText;
	private final String backText;
	
	protected BasicCard(String frontText, String backText) {
		this.frontText = frontText;
		this.backText = backText;
	}

	// Getters
	public String getfrontText(){return this.frontText;}
	public String getbackText(){return this.backText;}
}
