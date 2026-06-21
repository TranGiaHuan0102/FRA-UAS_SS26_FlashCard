package com.frauas.huankiet.app.service;

import com.frauas.huankiet.app.deck.Deck;
import java.util.*;

public class MockService {
    private Set<Deck> decks = new HashSet<>();

    public MockService() {
        Deck pcna = new Deck("PCNA");
        pcna.addCard("What does OSI stand for?", "Open Systems Interconnection");
        pcna.addCard("star_topology.png", "Network Topology Diagram", true);

        Deck morse = new Deck("Morse");
        morse.addCard("What the fuck is A", ".-");
        morse.addCard("What the fuck is B", "-...");

        decks.add(pcna);
        decks.add(morse);
    }

    public Set<Deck> getDecks() {
        return decks;
    }
}