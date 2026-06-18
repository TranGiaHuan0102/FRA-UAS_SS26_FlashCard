package flashcard.app.service;

import flashcard.app.deck.Deck;
import java.util.*;

public class MockService {
    private Set<Deck> decks = new HashSet<>();

    public MockService() {
        // 1. Create and populate the PCNA Deck (Contains 1 Basic, 1 Image)
        Deck pcna = new Deck("PCNA");
        pcna.addCard("What does OSI stand for?", "Open Systems Interconnection");
        pcna.addCard("star_topology.png", "Network Topology Diagram", true);

        // 2. Create and populate the Morse Deck (Contains 2 Basic Cards)
        Deck morse = new Deck("Morse");
        morse.addCard("What the fuck is A", ".-");
        morse.addCard("What the fuck is B", "-...");

        // Add both decks to our data set
        decks.add(pcna);
        decks.add(morse);
    }

    public Set<Deck> getDecks() {
        return decks;
    }

    public void addDeck(String name) {
        decks.add(new Deck(name));
    }
}