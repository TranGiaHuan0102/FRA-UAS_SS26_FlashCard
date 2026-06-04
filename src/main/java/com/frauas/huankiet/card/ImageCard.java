package com.frauas.huankiet.card;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ImageCard extends Card{
	
	class InvalidImagePathException extends Exception{
		public String getMessage(){return "Invalid Image Filepath!";}
	}

	public ImageCard() throws Exception{
		super();
		createCard();
	}	
	

	@Override	
	public void createCard() throws Exception{
		String front = "TopText";
		String imagePath = "BottomText.jpg";

		this.frontSide = frontSide;
		this.backSide = validateImagePath(imagePath); // Exception if imagePath is invalid 
	}

	// Getters and Setters
	@Override
	public String getFrontSide(){return this.frontSide;}
	@Override
	public String getBackSide(){return this.backSide;}
	@Override
	public void setFrontSide(String newFrontSide){this.frontSide = newFrontSide;}
	@Override
	public void setBackSide(String newImage){this.backSide = newImage;} 
	
	// Check validity of image path
	private String validateImagePath(String imagePath) throws InvalidImagePathException{
		// Match all file names that end with .png, .jpeg, .jpg
		Pattern imagePathPattern = Pattern.compile("^(.*)\\.(jpe?g|png)$");
		Matcher matcher = imagePathPattern.matcher(imagePath);
		if (!matcher.find()){throw new InvalidImagePathException();}
		return imagePath;
	}
}
