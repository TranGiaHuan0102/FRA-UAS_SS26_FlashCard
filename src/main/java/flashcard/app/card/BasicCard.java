package flashcard.app.card;

public class BasicCard extends Card {

    // Constructor that accepts custom values
    public BasicCard(String frontText, String backText) {
        super();
        this.frontSide = frontText;
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
    public void setBackSide(String newBackSide) { this.backSide = newBackSide; }
}