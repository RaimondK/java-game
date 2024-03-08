module com.example.asteroidsgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.asteroidsgame to javafx.fxml;
    exports com.example.asteroidsgame;
}