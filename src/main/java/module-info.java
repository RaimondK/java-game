module com.example.asteroidsgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.asteroidsgame to javafx.fxml;
    exports com.example.asteroidsgame;
    exports com.example.asteroidsgame.Enemies;
    opens com.example.asteroidsgame.Enemies to javafx.fxml;
}