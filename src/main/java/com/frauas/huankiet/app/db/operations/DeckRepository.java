package com.frauas.huankiet.app.db.operations;
import com.frauas.huankiet.app.classes.decks.Deck;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeckRepository {
	private final Connection conn;
	
	public DeckRepository(Connection conn) { this.conn = conn; }

	public void insert(Deck deck) throws DeckRepositoryException {
		// Insert deck to DB with deckName, then return ID and creationDate back to object
		// ID and creationDate are generated automatically by DB
		String sql = "INSERT INTO DECKS (deckName) VALUES (?) RETURNING deckID, creationDate";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, deck.getDeckName());

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
				    deck.setDeckID(rs.getLong("deckID"));
				    deck.setCreationDate(rs.getTimestamp("creationDate").toLocalDateTime());
				} 
				else {throw new DeckRepositoryException("Insert succeeded but no row was returned.", null);}
			}
		}
		catch (SQLException e){
			throw new DeckRepositoryException("Failed to insert deck " + deck.getDeckName() + "into database!", e);
		}
	}
	
	public void delete(Deck deck) throws DeckRepositoryException {
		if (deck.getDeckID() == null) {
			throw new DeckRepositoryException("Cannot delete a deck that hasn't been inserted.", null);
		}

		String sql = "DELETE FROM DECKS WHERE deckID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setLong(1, deck.getDeckID());
			stmt.executeUpdate();
		}
		catch (SQLException e) {
			throw new DeckRepositoryException("Failed to delete deck: " + deck.getDeckName(), e);
		}
	}

	public List<Deck> findAll() throws DeckRepositoryException {
		// Find all existing decks in DB  	
		String sql = "SELECT deckID, deckName, creationDate FROM DECKS ORDER BY deckName";
		List<Deck> decks = new ArrayList<>();

		try (PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery()) {
			// Add each found deck into list
			while (rs.next()) {
			    Deck deck = new Deck(rs.getString("deckName"));
			    deck.setDeckID(rs.getLong("deckID"));
			    deck.setCreationDate(rs.getTimestamp("creationDate").toLocalDateTime());
			    decks.add(deck);
			}
		}
		catch (SQLException e){throw new DeckRepositoryException("Failed to load decks from database!", e);}
		return decks;
	}
	
	public void update(Deck deck) throws DeckRepositoryException {
		if (deck.getDeckID() == null) {
			throw new DeckRepositoryException("Cannot update a deck that hasn't been inserted.", null);
		}

		String sql = "UPDATE DECKS SET deckName = ? WHERE deckID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, deck.getDeckName());
			stmt.setLong(2, deck.getDeckID());
			stmt.executeUpdate();
		}
		catch (SQLException e) {
			throw new DeckRepositoryException("Failed to update deck: " + deck.getDeckName(), e);
		}
	}	
}
