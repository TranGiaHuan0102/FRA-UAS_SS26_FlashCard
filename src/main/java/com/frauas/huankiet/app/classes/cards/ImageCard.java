package com.frauas.huankiet.app.classes.cards;

public class ImageCard extends Card{
	private final String imgURL;
	private final String answerText;	
	
	protected ImageCard(String answerText, String imgURL){
		this.answerText = answerText;
		this.imgURL = imgURL;   
	}	
	
	// Getters and Setters
	public String getAnswerText(){return this.answerText;}
	public String getImgURL(){return this.imgURL;}
}
