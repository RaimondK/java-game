package com.example.asteroidsgame.Enemies;

import com.example.asteroidsgame.Character;
import com.example.asteroidsgame.PolygonFactory;
import com.example.asteroidsgame.Ship;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.Random;

public class Triangle extends Character {

    private Ship ship;

    public Triangle(int x, int y, Ship playerShip) {
        super(new Polygon(-10, -10, 20, 0, -10, 10), x, y);
        this.getCharacter().setFill(Color.web("#ffffff", 1.0));
        this.ship = playerShip;

        Random rnd = new Random();

        int accelerationAmount = 1 + rnd.nextInt(10);
        for (int i = 0; i < accelerationAmount; i++) {
            accelerate();
        }
    }

    @Override
    public void move() {
        super.move();
    }

    public void followPlayer() {
        super.move();
        followPlayer(ship.getCharacter().getTranslateX(), ship.getCharacter().getTranslateY(), 1);
    }

    public void followPlayer(double playerX, double playerY, double speed) {
        // Calculate the direction vector from the asteroid to the player
        double dx = playerX - this.getCharacter().getTranslateX();
        double dy = playerY - this.getCharacter().getTranslateY();

        // Calculate the magnitude of the direction vector
        double magnitude = Math.sqrt(dx * dx + dy * dy);

        // Normalize the direction vector to get a unit vector
        dx /= magnitude;
        dy /= magnitude;

        // Update the position of the asteroid based on the direction vector and speed
        double newX = this.getCharacter().getTranslateX() + dx * speed;
        double newY = this.getCharacter().getTranslateY() + dy * speed;
        this.getCharacter().setTranslateX(newX);
        this.getCharacter().setTranslateY(newY);

        // Update the triangle to point towards the ship
        double mouseX = ship.getCharacter().getTranslateX();
        double mouseY = ship.getCharacter().getTranslateY();
        double shipX = super.getCharacter().getTranslateX() + (super.getCharacter().getLayoutBounds().getWidth() / 2);
        double shipY = super.getCharacter().getTranslateY() + (super.getCharacter().getLayoutBounds().getHeight() / 2);
        double angle = Math.toDegrees(Math.atan2(mouseY - shipY, mouseX - shipX));
        super.getCharacter().setRotate(angle);
    }

}
