package com.example.demo;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private static int numPlayers;
    private static String buttonStyle = "-fx-min-height: 132px;\n" +
            "    -fx-min-width: 128px;\n" +
            "    -fx-background-size: 100% 100%;\n" +
            "    -fx-background-repeat: no-repeat;\n" +
            "    -fx-background-position: center 8px;";
    private List<Integer> fishTiles = new ArrayList<>();
    private List<Button> fishTileButtons = new ArrayList<>();
    private Stage primaryStage;
    private Pane root;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.root = new Pane();
        this.scene = new Scene(root, 900, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Change this method to public to allow access from MainMenu
    public void setupGame(int numPlayers) {
        this.numPlayers = numPlayers;
        root.getChildren().clear();
        initializeFishTiles();
        createGameBoard(numPlayers);
        primaryStage.show();
    }

    private void createGameBoard(int numPlayers) {
        HBox penguinScreen = createPenguinDisplay();
        penguinScreen.setAlignment(Pos.TOP_RIGHT);
        root.getChildren().add(penguinScreen); // Optionally manage penguins based on players
        VBox scoreScreen = createScoreBoardDisplay();
        scoreScreen.setAlignment(Pos.TOP_LEFT);
        root.getChildren().add(scoreScreen);
        createGameTiles();
    }

    private HBox createPenguinDisplay() {
        HBox penguinDisplay = new HBox(10);
        addPenguins(penguinDisplay, "BluePeng", 4);
        addPenguins(penguinDisplay, "RedPeng", 4);
        return penguinDisplay;
    }

    private VBox createScoreBoardDisplay() {
        VBox scores = new VBox(2);
        TextField playerOneScore = new TextField("Player 1: ");
        TextField playerTwoScore = new TextField("Player 2: ");
        scores.getChildren().addAll(playerOneScore, playerTwoScore);
        return scores;
    }

    private void addPenguins(HBox penguinDisplay, String baseFileName, int count) {
        //int turn = 1;
        for (int i = 1; i <= count; i++) {
            String imagePath = "/Pengs/" + baseFileName + i + ".jpg"; // Adjust the path as needed
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                throw new RuntimeException("Cannot find resource: " + imagePath);
            }
            Image penguinImage = new Image(imageStream);
            ImageView penguinView = new ImageView(penguinImage);
            penguinView.setFitHeight(HEX_SIZE);
            penguinView.setFitWidth(HEX_SIZE);
            if(numPlayers==1){
                if((i==1||i==2||i==3||i==4) && baseFileName=="BluePeng"){
                    enableDragAndDrop(penguinView);
                }
            }
            penguinDisplay.getChildren().add(penguinView);
        }
    }

    private void enableDragAndDrop(ImageView imageView){
        imageView.setOnDragDetected(new EventHandler <MouseEvent>(){
            public void handle(MouseEvent event){
                Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putImage(imageView.getImage());
                db.setContent(content);
                event.consume();
            }
        });
        imageView.setOnDragOver(new EventHandler <DragEvent>(){
            public void handle(DragEvent event){
                if(event.getGestureSource() != imageView && event.getDragboard().hasImage()){
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            }
        });
        imageView.setOnDragDropped(new EventHandler <DragEvent>(){
            public void handle(DragEvent event){
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasImage()) {
                    imageView.setImage(db.getImage());
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
        imageView.setOnDragDone(new EventHandler <DragEvent>(){
            public void handle(DragEvent event){
                if(event.getTransferMode() == TransferMode.MOVE){
                    imageView.setImage(null);
                }
                event.consume();
            }
        });
    }

    public void enableDragAndDropForButton(Button button){
        button.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event){
                if(event.getGestureSource() != button && event.getDragboard().hasImage()){
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });

        button.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event){
                Dragboard db = event.getDragboard();
                boolean success = false;
                if(db.hasImage()){
                    Image droppedImage = db.getImage();
                    double buttonWidth = button.getWidth();
                    double buttonHeight = button.getHeight();
                    ImageView imageView = new ImageView(droppedImage);
                    imageView.setFitWidth(buttonWidth);
                    imageView.setFitHeight(buttonHeight);
                    button.setGraphic(imageView);
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
    }

    private void createGameTiles() {
        for (int row = 0; row < NUM_ROWS; row++) {
            int numColsInRow = row % 2 == 0 ? 7 : 8;
            for (int col = 0; col < numColsInRow; col++) {
                int fishCount = fishTiles.remove(0);
                String imagePath = "/Fish" + fishCount + ".png";
                Image fishImage = new Image(getClass().getResourceAsStream(imagePath),HEX_SIZE/0.5,HEX_SIZE/0.5,true,true);
                ImageView buttonView = new ImageView();
                buttonView.setImage(fishImage);
                //buttonView.setPreserveRatio(true);
                HexTile hex = new HexTile(HEX_SIZE, fishCount);
                double offsetX = (row % 2 == 0) ? (HEX_SIZE * 1.5) / 2 : 0;
                double x = col * (HEX_SIZE * 1.90 + GAP) + offsetX + BOARD_PADDING;
                double y = row * (HEX_SIZE * Math.sqrt(3) + GAP / 4) + BOARD_PADDING;
                TileButton tileButton = new TileButton(hex,buttonView);
                //tileButton.setPrefSize(HEX_SIZE,HEX_SIZE);
                tileButton.setLayoutX(x);
                tileButton.setLayoutY(y);
                tileButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                //tileButton.setStyle(buttonStyle);
                //tileButton.setGraphic(new ImageView(imagePath));
                enableDragAndDropForButton(tileButton);
                if(fishCount == 1){
                    tileButton.setOnAction(e -> System.out.println("Clicked one fish!"));
                }else if(fishCount == 2){
                    tileButton.setOnAction(e -> System.out.println("Clicked two fish!"));
                }else{
                    tileButton.setOnAction(e -> System.out.println("Clicked three fish!"));
                }
                fishTileButtons.add(tileButton);
                hex.setTranslateX(x);
                hex.setTranslateY(y);
                root.getChildren().add(tileButton);
            }
        }
    }

    private void initializeFishTiles() {
        fishTiles.clear();
        for (int i = 0; i < 30; i++) fishTiles.add(1);
        for (int i = 0; i < 20; i++) fishTiles.add(2);
        for (int i = 0; i < 10; i++) fishTiles.add(3);
        Collections.shuffle(fishTiles);
    }

    class TileButton extends Button{
        private HexTile hex;
        private ImageView imageView;
        public TileButton(HexTile hex, ImageView imageView){
            this.hex = hex;
            this.imageView = imageView;
            imageView.setFitWidth(this.getWidth());
            imageView.setFitHeight(this.getHeight());
            this.getChildren().addAll(hex, imageView);
            this.setPrefWidth(hex.getBoundsInLocal().getWidth() + imageView.getFitWidth());
            this.setPrefHeight(hex.getBoundsInLocal().getHeight() + imageView.getFitHeight());
            this.setGraphic(imageView);
        }
    }
    class HexTile extends StackPane {
        public HexTile(double size, int fishCount) {
            this.getChildren().add(createHexagonShape(size));
            this.getChildren().add(createFishImageView(fishCount));
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
            hexagon.setFill(Color.LIGHTBLUE);
            return hexagon;
        }

        private ImageView createFishImageView(int fishCount) {
            String imagePath = "/Fish" + fishCount + ".png";
            Image fishImage = new Image(getClass().getResourceAsStream(imagePath), HEX_SIZE / 4, HEX_SIZE / 4, true, true);
            ImageView imageView = new ImageView(fishImage);
            imageView.setPreserveRatio(true);
            return imageView;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}