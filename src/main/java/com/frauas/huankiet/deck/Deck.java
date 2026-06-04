package com.frauas.huankiet.deck;
import com.frauas.huankiet.miscellaneous.IDGenerator;
import com.frauas.huankiet.card.*;
import java.time.LocalDateTime;

public class Deck {
	private final String deckID;
	private final LocalDateTime creationDate;
	private String deckName;
	private int deckSize;

	Deck(String deckName){
		this.deckName = deckName;
		this.deckID = IDGenerator.generate_ID();
		this.creationDate = LocalDateTime.now();
		this.deckSize = 0;
	}

	public void addCard(String CardType){
		if (CardType.equals("IMAGE")){
			try{Card c = new ImageCard();}
			catch (Exception e){System.out.println(e.getMessage());}
		}
		else{
			try{Card c = new BasicCard();}
			catch (Exception e){System.out.println(e.getMessage());}
		}
	}
}
