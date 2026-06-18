module flashcards.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens flashcard.app.controller to javafx.fxml;
    opens flashcard.app to javafx.graphics;

    exports flashcard.app;
    exports flashcard.app.deck;
    exports flashcard.app.card;
}