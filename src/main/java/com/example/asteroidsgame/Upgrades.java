package com.example.asteroidsgame;

import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;

public class Upgrades extends Character {
    public Upgrades(int x, int y) {
        super(new Polygon(8, 0, 8, 8, 0, 8, 0, 0), x, y);
        this.getCharacter().setFill(Color.RED);
    }
}
