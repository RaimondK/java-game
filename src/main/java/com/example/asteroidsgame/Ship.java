package com.example.asteroidsgame;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Ship extends Character {

    public Ship(int x, int y) {
        super(new Polygon(-5, -5, 10, 0, -5, 5), x, y);
        this.getCharacter().setFill(Color.web("#00ff11",1.0));
    }

    public void accelerate() {
        double speed = 3;
        double angleInRadians = Math.toRadians(this.getCharacter().getRotate());
        double changeX = speed * Math.cos(angleInRadians);
        double changeY = speed * Math.sin(angleInRadians);

        this.setMovement(new Point2D(changeX, changeY));
    }

    public void stopMoving() {
        this.setMovement(new Point2D(0, 0));
    }

    public void moveBackwards()  {
        double speed = -1;
        double angleInRadians = Math.toRadians(this.getCharacter().getRotate());
        double changeX = speed * Math.cos(angleInRadians);
        double changeY = speed * Math.sin(angleInRadians);

        this.setMovement(new Point2D(changeX, changeY));
    }
}
