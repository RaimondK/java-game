package com.example.asteroidsgame.Enemies;

import com.example.asteroidsgame.Character;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.Random;

public class Circle extends Character {

    private double rotationalMovement;

    public Circle(int x, int y) {
        super(new Polygon(-10, -10, 20, 0, -10, 10), x, y);
        this.getCharacter().setFill(Color.web("#A600FF", 1.0));

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
        super.getCharacter().setRotate(super.getCharacter().getRotate() + rotationalMovement);
    }
}
