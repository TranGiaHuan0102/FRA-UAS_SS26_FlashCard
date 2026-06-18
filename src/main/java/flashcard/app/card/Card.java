package flashcard.app.card;

import flashcard.app.util.IDGenerator;
public abstract class Card {
    private final String id;
    protected String frontSide, backSide;

    protected Card() {
        this.id = IDGenerator.generate_ID();
    }

    public abstract void createCard() throws Exception;

    public abstract String getFrontSide();
    public abstract String getBackSide();
    public abstract void setFrontSide(String newFrontSide);
    public abstract void setBackSide(String newBackSide);

    public String getId() {
        return id;
    }
}
