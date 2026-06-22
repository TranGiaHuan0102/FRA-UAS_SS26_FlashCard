package com.frauas.huankiet.app.classes.cards;

public abstract class Card {
	protected Long cardID;

	private boolean isNewCard = true;
	private boolean isHard = false;
	private int delayOffset = 0;	// how many cards until the tagged card is reviewed

	// getters and setters for shared params
	public Long getCardID() {return this.cardID;}
	public void setCardID(Long cardID) {
		if (this.cardID != null) {
			throw new IllegalStateException("cardID already set, cannot be changed!");
		}
		this.cardID = cardID;
	}	

	// getters and setters for study session
	public boolean isNewCard(){return this.isNewCard;}
	public void setNewCard(boolean newCard){this.isNewCard = newCard;}	

	public boolean isHard() { return isHard; }
	public void setHard(boolean hard) { isHard = hard; }

	public int getDelayOffset() { return delayOffset; }
	public void setDelayOffset(int delayOffset) { this.delayOffset = delayOffset; }
}
