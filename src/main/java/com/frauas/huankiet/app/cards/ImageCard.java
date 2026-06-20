package com.frauas.huankiet.app.cards;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageCard extends Card {

    public static class InvalidImagePathException extends Exception {
        @Override
        public String getMessage() { return "Invalid Image Filepath!"; }
    }

    public ImageCard(String imagePath, String backText) throws Exception {
        super();
        this.frontSide = validateImagePath(imagePath);
        this.backSide = backText;
    }

    @Override
    public void createCard() throws Exception {
    }
    @Override
    public String getFrontSide() { return this.frontSide; }
    @Override
    public String getBackSide() { return this.backSide; }
    @Override
    public void setFrontSide(String newFrontSide) { this.frontSide = newFrontSide; }
    @Override
    public void setBackSide(String newImage) { this.backSide = newImage; }

    private String validateImagePath(String imagePath) throws InvalidImagePathException {
        Pattern imagePathPattern = Pattern.compile("^(.*)\\.(jpe?g|png)$");
        Matcher matcher = imagePathPattern.matcher(imagePath);
        if (!matcher.matches()) {
            throw new InvalidImagePathException();
        }
        return imagePath;
    }
}
