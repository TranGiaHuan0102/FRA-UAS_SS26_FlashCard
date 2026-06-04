package com.frauas.huankiet.card;
import com.frauas.huankiet.miscellaneous.IDGenerator;

abstract public class Card {
	private final String id;
	
	protected String frontSide, backSide;

	protected Card(){
		this.id = IDGenerator.generate_ID();
	}

	// Factory for creating cards
	abstract void createCard() throws Exception;

	abstract String getFrontSide();
	abstract String getBackSide();
	abstract void setFrontSide(String newFrontSide);
	abstract void setBackSide(String newBackSide);
}
