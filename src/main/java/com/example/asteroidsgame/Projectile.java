package com.example.asteroidsgame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Projectile extends Character {

    public Projectile(int x, int y) {
        super(new Polygon(3, -3, 3, 3, -3, 3, -3, -3), x, y);
        this.getCharacter().setFill(Color.WHITESMOKE);
    }

    public void move() {
        this.getCharacter().setTranslateX(this.getCharacter().getTranslateX() + this.getMovement().getX());
        this.getCharacter().setTranslateY(this.getCharacter().getTranslateY() + this.getMovement().getY());

        if (this.getCharacter().getTranslateX() < 0) {
            setAlive(false);
        }

        if (this.getCharacter().getTranslateX() > AsteroidsGame.WIDTH) {
            setAlive(false);
        }

        if (this.getCharacter().getTranslateY() < 0) {
            setAlive(false);
        }

        if (this.getCharacter().getTranslateY() > AsteroidsGame.HEIGHT) {
            setAlive(false);
        }
    }
}