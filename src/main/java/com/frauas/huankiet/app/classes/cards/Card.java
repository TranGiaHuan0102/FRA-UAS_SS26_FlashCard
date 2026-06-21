package com.frauas.huankiet.app.classes.cards;

public abstract class Card {
	protected Long cardID;
	protected String frontText;

	public Long getCardID() {return this.cardID;}
	public String getfrontText(){return this.frontText;}
	public void setfrontText(String newfrontText){this.frontText = newfrontText;}
	public void setCardID(Long cardID) {
		if (this.cardID != null) {
			throw new IllegalStateException("cardID already set, cannot be changed!");
		}
		this.cardID = cardID;
	}	
}
