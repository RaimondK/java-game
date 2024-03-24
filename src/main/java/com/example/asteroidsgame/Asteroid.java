package com.example.asteroidsgame;

import java.util.Random;

public class Asteroid extends Character {

    private double rotationalMovement;
    private Ship ship;

    public Asteroid(int x, int y, Ship playerShip) {
        super(new PolygonFactory().createPolygon(), x, y);
        this.ship = playerShip;

        Random rnd = new Random();

        super.getCharacter().setRotate(rnd.nextInt(360));

        int accelerationAmount = 1 + rnd.nextInt(10);
        for (int i = 0; i < accelerationAmount; i++) {
            accelerate();
        }

        this.rotationalMovement = 0.5 - rnd.nextDouble();
    }

    public Asteroid(double sizeOfAsteroid, int x, int y, Ship playerShip) {
        super(new PolygonFactory().createPolygon(sizeOfAsteroid), x, y);
        this.ship = playerShip;

        Random rnd = new Random();

        super.getCharacter().setRotate(rnd.nextInt(360));

        int accelerationAmount = 1 + rnd.nextInt(10);
        for (int i = 0; i < accelerationAmount; i++) {
            accelerate();
        }

        this.rotationalMovement = 0.5 - rnd.nextDouble();
    }

    @Override
    public void move() {
        super.move();
        followPlayer(ship.getCharacter().getTranslateX(), ship.getCharacter().getTranslateY(), 1);
        super.getCharacter().setRotate(super.getCharacter().getRotate() + rotationalMovement);
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
    }
}