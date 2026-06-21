package com.frauas.huankiet.app.classes.cards;

public class ImageCard extends Card{
	private String imgURL;	
	
	protected ImageCard(String frontText, String imgURL){
		this.frontText = frontText;
		this.imgURL = imgURL;   
	}	
	
	// Getters and Setters
	public String getbackText(){return this.imgURL;}
	public void setbackText(String newURL){this.imgURL = newURL;} 
}
