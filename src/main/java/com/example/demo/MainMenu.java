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

    @Override
    public void start(Stage primaryStage) {
        VBox menuBox = new VBox(10);
        menuBox.setAlignment(Pos.BOTTOM_CENTER);

        Button onePlayerButton = new Button("Single Player");
        Button twoPlayerButton = new Button("Multiplayer");

        URL imageUrl = MainMenu.class.getResource("/bg.JPEG");
        if (imageUrl == null) {
            System.err.println("Resource not found. Path may be incorrect.");
        } else {
            Image backgroundImage = new Image(imageUrl.toString());
            BackgroundImage bgImage = new BackgroundImage(backgroundImage,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            menuBox.setBackground(new Background(bgImage));
        }

        onePlayerButton.setOnAction(e -> {
            HeyThatsMyFishGame game = new HeyThatsMyFishGame(1, primaryStage);
            game.start();
        });

        twoPlayerButton.setOnAction(e -> {
            HeyThatsMyFishGame game = new HeyThatsMyFishGame(2, primaryStage);
            game.start();
        });

        menuBox.getChildren().addAll(onePlayerButton, twoPlayerButton);

        Scene scene = new Scene(menuBox, 1046, 297);
        primaryStage.setTitle("Hey That's My Fish!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        URL resUrl = MainMenu.class.getResource("/bg.JPEG");
        System.out.println("Resource URL: " + resUrl);
        launch(args);
    }
}
