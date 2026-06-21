module com.frauas.huankiet.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;

    opens com.frauas.huankiet.app.controller to javafx.fxml;
    opens com.frauas.huankiet.app to javafx.graphics;

    exports com.frauas.huankiet.app;
    exports com.frauas.huankiet.app.deck;
    exports com.frauas.huankiet.app.classes;
}
