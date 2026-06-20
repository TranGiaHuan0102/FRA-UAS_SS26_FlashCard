package com.frauas.huankiet.app.cards;

import com.frauas.huankiet.app.util.IDGenerator;
public abstract class Card {
    private final String id;
    protected String frontSide, backSide;

    private boolean isNewCard = true;
    private boolean isHard = false;
    private int delayOffset = 0; // how many cards until the tagged cards is reviewed

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
    public boolean isNewCard() { return isNewCard; }
    public void setNewCard(boolean newCard) { isNewCard = newCard; }

    public boolean isHard() { return isHard; }
    public void setHard(boolean hard) { isHard = hard; }

    public int getDelayOffset() { return delayOffset; }
    public void setDelayOffset(int delayOffset) { this.delayOffset = delayOffset; }
}
