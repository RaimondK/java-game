package com.example.asteroidsgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;


public class AsteroidsGame extends Application {

    public static int WIDTH = 600;
    public static int HEIGHT = 480;
    public static boolean gameStopped = false;
    public static int BUTTON_WIDTH = 100;
    public static int BUTTON_HEIGHT = 20;
    public int projectileCount = 1;
    private boolean mousePressed = false;

    @Override
    public void start(Stage stage) {
        // Start pane
        Pane startPane = new Pane();
        startPane.setPrefSize(WIDTH, HEIGHT);
        ImageView mainImage = new ImageView("File:src/main/resources/mainImage.png");
        mainImage.setFitHeight(HEIGHT);
        mainImage.setFitWidth(WIDTH);
        startPane.getChildren().add(mainImage);

        // Game window pane
        Pane pane = new Pane();

        // New text for points and death screen
        Text text = new Text(10, 20, "Points: 0");
        // Lost text settings
        Text youLostText = new Text(200, 240, "YOU LOST :)");
        youLostText.setFill(Color.RED);
        youLostText.setFont(Font.font(40));
        // Won text settings
        Text youWonText = new Text(200, 240, "YOU WON! =)");
        youWonText.setFill(Color.YELLOW);
        youWonText.setFont(Font.font(40));


        // Point calculation
        AtomicInteger points = new AtomicInteger();

        // Using public static int values for width and height
        pane.setPrefSize(WIDTH, HEIGHT);

        // Background color
        Rectangle backgroundRect = new Rectangle(0, 0, WIDTH, HEIGHT);
        backgroundRect.setFill(Color.DARKSLATEBLUE);

        // Create a black overlay rectangle
        Rectangle lostBackground = new Rectangle(0, 0, WIDTH, HEIGHT);
        lostBackground.setFill(Color.BLACK);
        lostBackground.setOpacity(0); // Initially transparent

        // Create a green overlay rectangle
        Rectangle wonBackground = new Rectangle(0, 0, WIDTH, HEIGHT);
        wonBackground.setFill(Color.GREEN);
        wonBackground.setOpacity(0); // Initially transparent

        pane.getChildren().addAll(backgroundRect, lostBackground, wonBackground);

        // Create a fade transition to gradually fade the black overlay
        FadeTransition fadeLostTransition = new FadeTransition(Duration.seconds(0.5), lostBackground);
        fadeLostTransition.setFromValue(0.0); // Starting opacity
        fadeLostTransition.setToValue(1.0);   // Ending opacity

        // Create a fade transition to gradually fade the green overlay
        FadeTransition fadeWonTransition = new FadeTransition(Duration.seconds(0.5), wonBackground);
        fadeWonTransition.setFromValue(0.0); // Starting opacity
        fadeWonTransition.setToValue(1.0);   // Ending opacity

        fadeLostTransition.setOnFinished(event -> {
            // Code to display text after transition
            pane.getChildren().add(youLostText);
            Button testButton = createButton("Quit");
            // Position the button in the center of the start pane
            testButton.setLayoutX(WIDTH / 2 - (BUTTON_WIDTH / 2));
            testButton.setLayoutY((HEIGHT / 2) + 30);
            pane.getChildren().add(testButton);
            testButton.setOnAction(click -> stage.close());
        });

        fadeWonTransition.setOnFinished(event -> {
            // Code to display text after transition
            pane.getChildren().add(youWonText);
        });

        pane.getChildren().add(text);

        // Create the ship and put it in the middle of the screen
        Ship ship = new Ship(WIDTH / 2, HEIGHT / 2);
        // Asteroid, upgrades and projectile lists
        List<Asteroid> asteroids = new ArrayList<>();
        List<Asteroid> splitAsteroids = new ArrayList<>();
        List<Asteroid> smallAsteroids = new ArrayList<>();
        List<Projectile> projectiles = new ArrayList<>();
        List<Upgrades> upgrades = new ArrayList<>();
        // Spawn first asteroids in random positions (Only on the left side of window)
        for (int i = 0; i < 1; i++) {
            Random rnd = new Random();
            // Position where it's created
            Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH / 3), rnd.nextInt(HEIGHT));
            asteroids.add(asteroid);
        }
        // Add 1 upgrade at the start (up to x amount)
        for (int i = 0; i < 1; i++) {
            Upgrades upgrade = new Upgrades(WIDTH / 3, HEIGHT / 3);
            upgrades.add(upgrade);
        }

        // Add ship to the pane
        pane.getChildren().add(ship.getCharacter());
        // Add asteroids
        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));
        // Add upgrades
        upgrades.forEach(upgrade -> pane.getChildren().add(upgrade.getCharacter()));
        // Create new scene
        Scene startScene = new Scene(startPane);
        Scene scene = new Scene(pane);

        // Create a "Start" button
        Button startButton = createButton("Start");
        startButton.setOnAction(event -> {
            // Switch to the main game pane when the button is clicked
            stage.setScene(scene);
        });

        // Position the button in the center of the start pane
        startButton.setLayoutX(WIDTH / 2 - (BUTTON_WIDTH / 2));
        startButton.setLayoutY(HEIGHT / 2);

        // Add the button to the start pane
        startPane.getChildren().add(startButton);

        // Create a "Quit" button
        Button quitButton = createButton("Quit");
        quitButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        quitButton.setOnAction(event -> {
            // Close the program entirely
            stage.close();
        });

        // Position the "Quit" button below the "Start" button
        quitButton.setLayoutX(WIDTH / 2 - (BUTTON_WIDTH / 2));
        quitButton.setLayoutY((HEIGHT - BUTTON_HEIGHT + 100) / 2);

        // Add the button to the start pane
        startPane.getChildren().add(quitButton);


        // HashMap for pressed key values
        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();

        scene.setOnKeyPressed(event -> pressedKeys.put(event.getCode(), Boolean.TRUE));

        scene.setOnKeyReleased(event -> pressedKeys.put(event.getCode(), Boolean.FALSE));

        scene.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                mousePressed = true;
            }
        });

        scene.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                mousePressed = false;
            }
        });

        new AnimationTimer() {

            @Override
            public void handle(long now) {
                // Move forward
                if (pressedKeys.getOrDefault(KeyCode.W, false)) {
                    ship.accelerate();
                }
                // Move backwards
                if (pressedKeys.getOrDefault(KeyCode.S, false)) {
                    ship.decelerate();
                }
                scene.setOnMouseMoved(event -> {
                    if (!gameStopped) {
                        // Calculate the angle between the ship and the mouse cursor
                        double mouseX = event.getX();
                        double mouseY = event.getY();
                        double shipX = ship.getCharacter().getTranslateX() + (ship.getCharacter().getLayoutBounds().getWidth() / 2);
                        double shipY = ship.getCharacter().getTranslateY() + (ship.getCharacter().getLayoutBounds().getHeight() / 2);
                        double angle = Math.toDegrees(Math.atan2(mouseY - shipY, mouseX - shipX));

                        // Rotate the ship
                        ship.getCharacter().setRotate(angle);
                    }
                });
                // Stop the game when ESC is pressed
                if (pressedKeys.getOrDefault(KeyCode.ESCAPE, false)) {
                    gameStopped = true;
                    stop();
                    // Create a "Resume" button
                    Button resumeButton = createButton("Resume");
                    Button menuButton = createButton("Back to menu");

                    // Position the resume button in the center of the pane
                    resumeButton.setLayoutX(WIDTH / 2 - (BUTTON_WIDTH / 2));
                    resumeButton.setLayoutY(HEIGHT / 2);
                    // Position the quit button below the resume button
                    menuButton.setLayoutX(WIDTH / 2 - (BUTTON_WIDTH / 2));
                    menuButton.setLayoutY((HEIGHT - BUTTON_HEIGHT + 100) / 2);
                    pane.getChildren().addAll(resumeButton, menuButton);

                    resumeButton.setOnAction(event -> {
                        // Resume the game
                        gameStopped = false;
                        start();
                        // Remove the button once resumed
                        pane.getChildren().removeAll(resumeButton, menuButton);
                    });

                    // Create a "Back to menu" button
                    menuButton.setOnAction(event -> {
                        // Goes back to the main menu
                        stage.setScene(startScene);
                    });
                }
                // Shoot with Mouse1
                if (mousePressed && !gameStopped && projectiles.size() < projectileCount) {
                    // Creating a new projectile
                    Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
                    projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                    projectiles.add(projectile);

                    projectile.accelerate();
                    projectile.setMovement(projectile.getMovement().normalize().multiply(3));

                    pane.getChildren().add(projectile.getCharacter());
                }

                ship.move();
                asteroids.forEach(Asteroid::move);
                splitAsteroids.forEach(Asteroid::move);
                smallAsteroids.forEach(Asteroid::move);
                projectiles.forEach(Projectile::move);
                // Ship collision check
                asteroids.forEach(asteroid -> {
                    if (ship.collide(asteroid)) {
                        stop();                // Stop and
                        fadeLostTransition.play(); // transition to black
                    }
                });
                // Ship collision check with upgrades
                upgrades.forEach(upgrade -> {
                    if (ship.collide(upgrade)) {
                        upgrade.setAlive(false);
                        projectileCount++;
                        // Testing popup for upgrades
                        Label popupText = new Label("+1 projectiles! (" + projectileCount + " in total)");
                        popupText.setTextFill(Color.WHITESMOKE);
                        // Create popup
                        Popup popup = new Popup();
                        popup.getContent().add(popupText); // Set text
                        popup.setAutoFix(true); // Enable auto-fix
                        popup.setWidth(50); // Set width
                        popup.setHeight(100); // Set height

                        // Show popup
                        popup.show(stage);

                        // Hide popup after a certain duration
                        Duration duration = Duration.seconds(2);
                        KeyFrame keyFrame = new KeyFrame(duration, event -> popup.hide());
                        Timeline popupTime = new Timeline(keyFrame);
                        popupTime.play();
                    }
                });
                // Ship collision check with splitAsteroids
                splitAsteroids.forEach(splitAsteroid -> {
                    if (ship.collide(splitAsteroid)) {
                        stop();                // Stop and
                        fadeLostTransition.play(); // transition to black
                    }
                });
                smallAsteroids.forEach(smallAsteroid -> {
                    if (ship.collide(smallAsteroid)) {
                        stop();                // Stop and
                        fadeLostTransition.play(); // transition to black
                    }
                });
                // Collision check between projectiles and asteroids
                projectiles.forEach(projectile -> {
                    asteroids.forEach(asteroid -> {
                        if (projectile.collide(asteroid)) {
                            projectile.setAlive(false);
                            asteroid.setAlive(false);
                            text.setText("Points: " + points.addAndGet(500));
                            // TESTING Timeline
                            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
                                // Add 4 splitAsteroids to the place of asteroid
                                for (int i = 0; i < 2; i++) {
                                    Asteroid splitAsteroid = new Asteroid(30, (int) asteroid.getCharacter().getTranslateX(), (int) asteroid.getCharacter().getTranslateY());
                                    splitAsteroids.add(splitAsteroid);
                                    pane.getChildren().add(splitAsteroid.getCharacter());
                                }
                            }));
                            timeline.play();
                        }
                    });
                    // Additional collision check between projectiles and splitAsteroids
                    splitAsteroids.forEach(splitAsteroid -> {
                        if (projectile.collide(splitAsteroid)) {
                            projectile.setAlive(false);
                            splitAsteroid.setAlive(false);
                            text.setText("Points: " + points.addAndGet(250));
                            // TESTING Timeline
                            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
                                // Add 6 smallAsteroids to the place of splitAsteroids
                                for (int i = 0; i < 12; i++) {
                                    Asteroid smallAsteroid = new Asteroid(5, (int) splitAsteroid.getCharacter().getTranslateX(), (int) splitAsteroid.getCharacter().getTranslateY());
                                    smallAsteroids.add(smallAsteroid);
                                    pane.getChildren().add(smallAsteroid.getCharacter());
                                }
                            }));
                            timeline.play();
                        }
                    });
                    // Additional collision check between projectiles and smallAsteroids
                    smallAsteroids.forEach(smallAsteroid -> {
                        if (projectile.collide(smallAsteroid)) {
                            projectile.setAlive(false);
                            smallAsteroid.setAlive(false);
                            text.setText("Points: " + points.addAndGet(100));
                        }
                    });
                });
                upgrades.stream()
                        .filter(upgrade -> !upgrade.isAlive())
                        .forEach(upgrade -> pane.getChildren().remove(upgrade.getCharacter()));

                upgrades.removeAll(upgrades.stream()
                        .filter(upgrade -> !upgrade.isAlive())
                        .collect(Collectors.toList()));

                projectiles.stream()
                        .filter(projectile -> !projectile.isAlive())
                        .forEach(projectile -> pane.getChildren().remove(projectile.getCharacter()));

                projectiles.removeAll(projectiles.stream()
                        .filter(projectile -> !projectile.isAlive())
                        .collect(Collectors.toList()));

                asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .forEach(asteroid -> pane.getChildren().remove(asteroid.getCharacter()));

                asteroids.removeAll(asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .collect(Collectors.toList()));

                splitAsteroids.stream()
                        .filter(splitAsteroid -> !splitAsteroid.isAlive())
                        .forEach(splitAsteroid -> pane.getChildren().remove(splitAsteroid.getCharacter()));

                splitAsteroids.removeAll(splitAsteroids.stream()
                        .filter(splitAsteroid -> !splitAsteroid.isAlive())
                        .collect(Collectors.toList()));

                smallAsteroids.stream()
                        .filter(smallAsteroid -> !smallAsteroid.isAlive())
                        .forEach(smallAsteroid -> pane.getChildren().remove(smallAsteroid.getCharacter()));

                smallAsteroids.removeAll(smallAsteroids.stream()
                        .filter(smallAsteroid -> !smallAsteroid.isAlive())
                        .collect(Collectors.toList()));
                // Randomly spawn upgrade objects
                if (Math.random() < 0.0005) {
                    Random rnd = new Random();
                    Upgrades upgrade = new Upgrades(rnd.nextInt(WIDTH), rnd.nextInt(HEIGHT));
                    if (!upgrade.collide(ship)) {
                        upgrades.add(upgrade);
                        pane.getChildren().add(upgrade.getCharacter());
                    }
                }
                // Add a new asteroid to the game
                if (Math.random() < 0.0005) {
                    Asteroid asteroid = new Asteroid(WIDTH, HEIGHT);
                    if (!asteroid.collide(ship)) {
                        asteroids.add(asteroid);
                        pane.getChildren().add(asteroid.getCharacter());
                    }
                }
                // After x amount of points, stop and transition color
                if (points.get() >= 150000) {
                    gameStopped = true;
                    stop();                   // Stop and
                    fadeWonTransition.play(); // transition to green
                }
            }
        }.start();

        stage.setTitle("Fun game! :)");
        stage.setScene(startScene);
        stage.show();
    }

    private Button createButton(String buttonName) {
        Button button = new Button(buttonName);
        button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        return button;
    }

    public static void main(String[] args) {
        launch();
    }
}
