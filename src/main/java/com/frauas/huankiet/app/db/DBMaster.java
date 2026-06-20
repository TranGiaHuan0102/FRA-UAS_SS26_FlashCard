package com.frauas.huankiet.app.db;
import com.frauas.huankiet.app.db.operations.CardRepository;
import com.frauas.huankiet.app.db.operations.DeckRepository;
import java.sql.Connection;

public class DBMaster {
	private static Connection conn;
	private static DeckRepository deckRepository;
	private static CardRepository cardRepository;

	// Start new flashcard session
	public static void startFlashCardSession() {
		conn = DBSetup.setup();
		deckRepository = new DeckRepository(conn);
		cardRepository = new CardRepository(conn);
	}

	// End current flashcard session
	public static void endFlashCardSession() throws DBConnectionException{
		DBConnectionController.CloseConnection(conn);
	}

	// Access repositories
	public static DeckRepository getDeckRepository() { return deckRepository; }
	public static CardRepository getCardRepository() { return cardRepository; }
}