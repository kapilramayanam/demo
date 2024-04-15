package com.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class HeyThatsMyFishGame extends Application {

    private static final int NUM_ROWS = 8;
    private static final double HEX_SIZE = 50;
    private static final double BOARD_PADDING = 50;
    private static final double GAP = 5;

    private List<Integer> fishTiles;

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        // Initialize the fish distribution based on the game's rules
        initializeFishTiles();

        for (int row = 0; row < NUM_ROWS; row++) {
            int numColsInRow = row % 2 == 0 ? 7 : 8;

            for (int col = 0; col < numColsInRow; col++) {
                int fishCount = fishTiles.remove(0); // Remove a fish count from the list
                HexTile hex = new HexTile(HEX_SIZE, fishCount);

                double offsetX = (row % 2 == 0) ? (HEX_SIZE * 1.5) / 2 : 0;
                double x = col * (HEX_SIZE * 1.90+ GAP) + offsetX + BOARD_PADDING;
                double y = row * (HEX_SIZE * Math.sqrt(3)+GAP/4) + BOARD_PADDING;

                hex.setTranslateX(x);
                hex.setTranslateY(y);
//                hex.setFill(row % 2 == col % 2 ? Color.SKYBLUE : Color.LIGHTBLUE);
                root.getChildren().add(hex);

            }
        }

        Scene scene = new Scene(root, 970, 970); // Adjust the size of the scene as needed
        primaryStage.setTitle("Hey That's My Fish!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeFishTiles() {
        fishTiles = new ArrayList<>();
        // Add fish counts to the list based on the game's distribution
        for (int i = 0; i < 30; i++) fishTiles.add(1); // 30 one-fish tiles
        for (int i = 0; i < 20; i++) fishTiles.add(2); // 20 two-fish tiles
        for (int i = 0; i < 10; i++) fishTiles.add(3); // 10 three-fish tiles
        Collections.shuffle(fishTiles); // Shuffle the list to randomize the fish distribution
    }

    public static void main(String[] args) {
        launch(args);
    }

    public class HexTile extends StackPane {
        public HexTile(double size, int fishCount) {
            Polygon hexagon = createHexagonShape(size);
            hexagon.setFill(Color.LIGHTBLUE);
            this.getChildren().add(hexagon);

            ImageView fishImageView = createFishImageView(fishCount);
            this.getChildren().add(fishImageView);
        }

        private Polygon createHexagonShape(double size) {
            Polygon hexagon = new Polygon();
            for (int i = 0; i < 6; i++) {
                double angle = Math.PI / 6 + Math.PI / 3.0 * i;
                double x_i = size * Math.cos(angle);
                double y_i = size * Math.sin(angle);
                hexagon.getPoints().addAll(x_i, y_i);
            }
            hexagon.setStroke(Color.BLACK);
            hexagon.setStrokeWidth(1);
            return hexagon;
        }

        private ImageView createFishImageView(int fishCount) {
            // Make sure the path starts with a '/' if the images are located at the root of the resources folder
            String imagePath = "/Fish" + fishCount + ".png"; // Adjust this path as necessary
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                throw new RuntimeException("Cannot find resource: " + imagePath);
            }
            Image fishImage = new Image(imageStream);
            ImageView imageView = new ImageView(fishImage);
            imageView.setFitHeight(HEX_SIZE /0.5 ); // Adjust size as needed
            imageView.setFitWidth(HEX_SIZE /0.5);
            imageView.setPreserveRatio(true);
            return imageView;
        }
    }
}
