package com.frauas.huankiet.app.db.operations;
import com.frauas.huankiet.app.classes.cards.*;
import com.frauas.huankiet.app.classes.decks.Deck;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class CardRepository {
	private final Connection conn;
	public CardRepository(Connection conn) {this.conn = conn;}

	public void insert(Card c, Deck d) throws CardRepositoryException{
		// Cannot insert loaded cards
		if (c.getCardID() != null) {
			throw new CardRepositoryException("Card has already been inserted!", null);
		}
		if (d.getDeckID() == null) {
		    throw new CardRepositoryException("Deck must be inserted before cards can be added to it!", null);
		}

		String cardType = (c instanceof BasicCard) ? "BASIC" : "IMAGE";

		try{
			conn.setAutoCommit(false);

			// Step 1: Insert into general CARDS database, get back generated ID
			String insertCard = "INSERT INTO CARDS (cardType) VALUES (?) RETURNING cardID";
			long cardID;

			// Wrap in try block so stmt is closed after processing automatically	
			try (PreparedStatement stmt = conn.prepareStatement(insertCard)){
				stmt.setString(1, cardType);
				try (ResultSet rs = stmt.executeQuery()){
					if (!rs.next()){
						throw new SQLException("Insert into CARDs succeeded but no returned ID!");
					}
					cardID = rs.getLong("cardID");
				}
			}

			// Step 2: Insert into subtype tables
			if (c instanceof BasicCard basic){
				String insertBasic = "INSERT INTO BASIC_CARDS (cardID, frontSide, backSide) VALUES (?, ?, ?)";
				try (PreparedStatement stmt = conn.prepareStatement(insertBasic)){
					stmt.setLong(1, cardID);
					stmt.setString(2, basic.getfrontText());
					stmt.setString(3, basic.getbackText());
					stmt.executeUpdate();
				}	
			}
			else if (c instanceof ImageCard image){
				String insertImage = "INSERT INTO IMAGE_CARDS (cardID, frontSide, imagePath) VALUES (?, ?, ?)";
				try (PreparedStatement stmt = conn.prepareStatement(insertImage)){
					stmt.setLong(1, cardID);
					stmt.setString(2, image.getfrontText());
					stmt.setString(3, image.getbackText());
					stmt.executeUpdate();
				}	
			}

			// Step 3: Link cards to decks
			String insertLink = "INSERT INTO DECKS_TO_CARDS (deckID, cardID) VALUES (?, ?)";
			try (PreparedStatement stmt = conn.prepareStatement(insertLink)){
				stmt.setLong(1, d.getDeckID());
				stmt.setLong(2, cardID);
				stmt.executeUpdate();
			}

			// Commit changes and update ID of Card object only if all 3 steps succeeded
			conn.commit();
			c.setCardID(cardID);
		}
		catch (SQLException e){
			// If anything goes wrong, try to rollback and throw exception
			try{conn.rollback();}
			catch(SQLException rollbackEx){e.addSuppressed(rollbackEx);} 
			throw new CardRepositoryException("Failed to insert card!", null);
		}
		finally{
			try{conn.setAutoCommit(true);}
			catch(SQLException ignored){}
		}
	}   

	public void delete(Card c) throws CardRepositoryException{
		if (c.getCardID() == null){
			throw new CardRepositoryException("Cannot delete cards that hasn't been inserted!", null);
		}
		String sql = "DELETE FROM CARDS WHERE cardID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setLong(1, c.getCardID());
			stmt.executeUpdate();
		}
		catch (SQLException e){throw new CardRepositoryException("Failed to delete card!", e);}
	}

	public List<Card> findByDeck(Deck d) throws CardRepositoryException{
		if (d.getDeckID() == null){
			throw new CardRepositoryException("Cannot find cards for a deck that has not been inserted!", null);
		}

		String sql = "SELECT c.cardID, c.cardType, "
			+ "b.frontSide AS BASIC_FRONT, b.backSide AS BASIC_BACK"
			+ "i.frontSide AS IMAGE_FRONT, i.imagePath AS IMAGE_PATH"
			+ "FROM CARDS c "
			+ "JOIN DECKS_TO_CARDS dtc ON c.cardID = dtc.cardID "
			+ "LEFT JOIN BASIC_CARDS b ON c.cardID = b.cardID "
			+ "LEFT JOIN IMAGE_CARDS i ON c.cardID = i.cardID "
			+ "WHERE dtc.deckID = ?";
				
		List<Card> cards = new ArrayList<>();
		try (PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setLong(1, d.getDeckID());
			try (ResultSet rs = stmt.executeQuery()){
				while (rs.next()){
					String cardType = rs.getString("cardType");
					
					// Depending on cardType, create corresponding card object
					Card card = switch(cardType){
						case "BASIC" -> CardFactory.createBasic(
							rs.getString("BASIC_FRONT"), rs.getString("BASIC_BACK"));
						case "IMAGE" -> {
							try{
								yield CardFactory.createImage(
									rs.getString("IMAGE_FRONT"), rs.getString("IMAGE_PATH"));
							}
							catch(CardFactoryException e){
								throw new CardRepositoryException(
									"Failed to reconstruct ImageCard from database row", e);
							}
						}
						default -> throw new CardRepositoryException("Unknown cardType in database!", null);
					};

					card.setCardID(rs.getLong("cardID"));
					cards.add(card);
				}
			}
		}
		catch (SQLException e){throw new CardRepositoryException("Failed to load cards for deck!", e);}
		return cards;
	}	
}
