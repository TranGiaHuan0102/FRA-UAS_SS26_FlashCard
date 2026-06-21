package com.frauas.huankiet.app.classes.cards;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardFactory{
	public static BasicCard createBasic(String frontText, String backText){
		return new BasicCard(frontText, backText);
	}

	public static ImageCard createImage(String frontText, String imgURL) throws CardFactoryException{
		if (isValidFilePath(imgURL)){return new ImageCard(frontText, imgURL);}
		else{throw new CardFactoryException("Invalid image file path!", null);}
	}

	public static boolean isValidFilePath(String imgURL){
		// Match all file names that end with .png, .jpeg, .jpg
		Pattern imagePathPattern = Pattern.compile("^(.*)\\.(jpe?g|png)$");
		Matcher matcher = imagePathPattern.matcher(imgURL);
		return matcher.find();
	}
}
