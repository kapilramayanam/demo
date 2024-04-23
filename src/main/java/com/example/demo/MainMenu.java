package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class MainMenu extends Application {

    private HeyThatsMyFishGame game; // Add a reference to the game instance

    @Override
    public void start(Stage primaryStage) {
        VBox menuBox = new VBox(10);
        menuBox.setAlignment(Pos.BOTTOM_CENTER);

        Button onePlayerButton = new Button("Single Player");
        Button twoPlayerButton = new Button("Multiplayer");

        // Initialize the game
        this.game = new HeyThatsMyFishGame(); // Assuming no-args constructor or adjust as necessary
        game.start(new Stage()); // Start the game on a new stage or use primary stage as needed

        onePlayerButton.setOnAction(e -> game.setupGame(1));
        twoPlayerButton.setOnAction(e -> game.setupGame(2));

        // Background setup
        Image backgroundImage = new Image(getClass().getResourceAsStream("/bg.JPEG"));
        BackgroundImage bgImage = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        menuBox.setBackground(new Background(bgImage));

        menuBox.getChildren().addAll(onePlayerButton, twoPlayerButton);

        Scene scene = new Scene(menuBox, 1046, 297);
        primaryStage.setTitle("Hey That's My Fish!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}