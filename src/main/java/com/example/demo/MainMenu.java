package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Objects;

public class MainMenu extends Application {

    private HeyThatsMyFishGame game; // Add a reference to the game instance

    @Override
    public void start(Stage primaryStage) throws MalformedURLException {
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
        URL backgroundUrl = new URL("https://m.media-amazon.com/images/S/aplus-media/vc/f886c95f-470b-4f1f-bf6d-3ee40f65ed93._CR0,0,970,300_PT0_SX970__.jpg");
        Image backgroundImage = new Image(backgroundUrl.toString());
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