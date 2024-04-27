package com.example.demo;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
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
import java.lang.reflect.Array;
import java.util.*;


public class HeyThatsMyFishGame extends Application {

    private static final int NUM_ROWS = 8;
    private static final double HEX_SIZE = 50;
    private static final double BOARD_PADDING = 50;
    private static final double GAP = 5;
    private static int numPlayers;
    private static int turn;
    private static int playerScore;
    private static int opponentScore;
    private static String buttonStyle = "-fx-min-height: 132px;\n" +
            "    -fx-min-width: 128px;\n" +
            "    -fx-background-size: 100% 100%;\n" +
            "    -fx-background-repeat: no-repeat;\n" +
            "    -fx-background-position: center 8px;";
    private List<Integer> fishTiles = new ArrayList<>();
    private List<Integer> fishScores = new ArrayList<>();
    private List<TileButton> fishTileButtons = new ArrayList<>();
    private TextField playerOneScore;
    private TextField playerTwoScore;
    private Stage primaryStage;
    private Pane root;
    private Scene scene;
    private BoardAndMovement bm = new BoardAndMovement();
    private Player playerOne = new Player();
    private Player playerTwo;
    private AI ai;
    private Penguin selected;
    private ArrayList<Image> bluePenguins = new ArrayList<>();
    private ArrayList<Image> redPenguins = new ArrayList<>();
    private ImageView imageView = new ImageView();
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.root = new Pane();
        this.scene = new Scene(root, 900, 800);
        primaryStage.setScene(scene);
        primaryStage.hide();
    }

    // Change this method to public to allow access from MainMenu
    public void setupGame(int numPlayers) {
        this.numPlayers = numPlayers;
        if(numPlayers == 1)
            ai = new AI();
        else
            playerTwo = new Player();
        root.getChildren().clear();
        initializeFishTiles();
        createGameBoard(numPlayers);
        primaryStage.show();
    }

    private void createGameBoard(int numPlayers) {
        HBox penguinScreen = createPenguinDisplay();
        penguinScreen.setAlignment(Pos.TOP_LEFT);
        root.getChildren().add(penguinScreen); // Optionally manage penguins based on players
        VBox pengAndScores = new VBox();
        pengAndScores.getChildren().add(penguinScreen);
        VBox scoreScreen = createScoreBoardDisplay();
        pengAndScores.getChildren().add(scoreScreen);
        pengAndScores.setAlignment(Pos.TOP_RIGHT);
        HBox.setHgrow(pengAndScores,Priority.ALWAYS);
        root.getChildren().add(pengAndScores);
        createGameTiles();
    }

    private HBox createPenguinDisplay() {
        HBox penguinDisplay = new HBox(10);
        bluePenguins = addPenguins(penguinDisplay, "BluePeng", 4);
        redPenguins = addPenguins(penguinDisplay, "RedPeng", 4); //give the penguin images to player classes
        return penguinDisplay;
    }

    private VBox createScoreBoardDisplay() {
        VBox scores = new VBox(2);
        playerOneScore = new TextField("Player 1: " + playerScore);
        playerTwoScore = new TextField("Player 2: " + opponentScore);
        scores.getChildren().addAll(playerOneScore, playerTwoScore);
        return scores;
    }

    private ArrayList<Image> addPenguins(HBox penguinDisplay, String baseFileName, int count) {
        turn = 1;
        Random rand = new Random();
        ArrayList<Image> pengChosen = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            String imagePath = "/Pengs/" + baseFileName + i + ".jpg"; // Adjust the path as needed
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                throw new RuntimeException("Cannot find resource: " + imagePath);
            }
            Image penguinImage = new Image(imageStream);
            pengChosen.add(penguinImage);
            ImageView penguinView = new ImageView(penguinImage);
            penguinView.setFitHeight(HEX_SIZE);
            penguinView.setFitWidth(HEX_SIZE);

            if(numPlayers==1){
                //Player 1 Chooses penguin.
                if(baseFileName=="BluePeng" && turn % 2 == 1){
                    enableDragAndDrop(penguinView);
                }
                //Player 2 (i.e. AI) Chooses Penguin.
                else if(baseFileName=="RedPeng" && turn % 2 == 0){
                    int tileChoice = rand.nextInt(59) + 1;
                    //Ensures we do not choose tile that has already been chosen.
                    if(fishTileButtons.get(tileChoice).getFishID() == 0){
                        while(fishTileButtons.get(tileChoice).getFishID()==0){
                            tileChoice = rand.nextInt(59)+1;
                        }
                    }
                    TileButton tileButtonChosen = fishTileButtons.get(tileChoice);
                    double buttonWidth = tileButtonChosen.getWidth();
                    double buttonHeight = tileButtonChosen.getHeight();
                    penguinView.setFitWidth(buttonWidth);
                    penguinView.setFitHeight(buttonHeight);
                    tileButtonChosen.setGraphic(penguinView);
                    turn += 1;
                }
            }else if(numPlayers==2){
                if(baseFileName=="BluePeng"){
                    //System.out.println("Turn " + turn + ": Player 1 Choose Penguin");
                    enableDragAndDrop(penguinView);
                }else if(baseFileName=="RedPeng"){
                    //System.out.println("Turn " + turn + ": Player 2 Choose Penguin");
                    enableDragAndDrop(penguinView);
                }
            }
            penguinDisplay.getChildren().add(penguinView);
        }
        for(Image image : pengChosen) {System.out.println("Peng: " + image);}
        return pengChosen;
    }
    private void enableDragAndDrop(ImageView iv){

        iv.setOnDragDetected(new EventHandler <MouseEvent>(){
            public void handle(MouseEvent event){
                imageView = iv;
                if((turn % 2 == 1 && redPenguins.stream().anyMatch(a -> a.equals(iv.getImage())))
                        || (turn % 2 == 0 && bluePenguins.stream().anyMatch(a -> a.equals(iv.getImage())))) {
                    return;
                }
                /*if(turn % 2 == 1){
                    bluePenguins.stream().filter(a -> a.equals(imageView.getImage())).
                            findFirst().ifPresent(a ->
                            });
                }*/
                Dragboard db = iv.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putImage(iv.getImage());
                db.setContent(content);
                //System.out.println("\t" + db.getImage() + "\t" + bluePenguins.get(0));
                //db.getImage() == bluePenguins.get(0);
                event.consume();
            }
        });
        iv.setOnDragOver(new EventHandler <DragEvent>(){
            public void handle(DragEvent event){
                if(event.getGestureSource() != iv && event.getDragboard().hasImage()){
                    event.acceptTransferModes(TransferMode.MOVE);

                    //System.out.println("imageview dragOver: " + imageView.getImage());

                }

                event.consume();
            }
        });
        iv.setOnDragDropped(new EventHandler <DragEvent>(){
            public void handle(DragEvent event){
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasImage()) {
                    System.out.println("DbIV dropped: " + db.getImage());
                    System.out.println("\tIV cur: " + iv.getImage());
                    iv.setImage(db.getImage());
                    System.out.println("\tIV new: " + iv.getImage());
                    imageView.setImage(db.getImage());
                    System.out.println("\timageView new: " + imageView.getImage());
                    success = true;
                }
                //TO DO: Handle better

                event.setDropCompleted(success);
                event.consume();
            }
        });
        iv.setOnDragDone(new EventHandler <DragEvent>(){
            public void handle(DragEvent event){
                if(event.getTransferMode() == TransferMode.MOVE){
                    System.out.println("iv done: " + iv.getImage());
                    iv.setImage(null);
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
                    //System.out.println("button drag: " + event.getDragboard().getImage());
                }
                event.consume();
            }
        });

        button.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event){
                Dragboard db = event.getDragboard();
                boolean success = false;

                if(db.hasImage()) {
                    System.out.println("Dropped button: " + imageView.getImage());
                    System.out.println(db.getImage());
                    //Image droppedImage = db.getImage();
                    double buttonWidth = button.getWidth();
                    double buttonHeight = button.getHeight();

                    Penguin newPeng = new Penguin(bm, new ImageView(imageView.getImage()));

                    boolean check = newPeng.setLocation(bm.activeSpots.get(fishTileButtons.indexOf(button)));
                    if(!check) {
                        //TO DO: Add error message?
                        return;
                    } else if (turn%2 == 1) { //TO DO: && correct penguin
                        playerOne.addPenguinPlayer(newPeng);
                    } else if (numPlayers==2) { //TO DO: && correct penguin
                        playerTwo.addPenguinPlayer(newPeng);
                    } else //Not correct penguin
                        return;


                    newPeng.getImageView().setFitWidth(buttonWidth);
                    newPeng.getImageView().setFitHeight(buttonHeight);
                    button.setGraphic(newPeng.getImageView());



                        /*System.out.println(imageView.getImage());
                        for(Image image : bluePenguins){
                            System.out.println("\t" +image);
                        }
                        for(Image image : redPenguins){
                            System.out.println("\t"+ image);
                        }*/

                    if(numPlayers==1){
                        if(button instanceof TileButton){
                            int fishTileID = ((TileButton) button).getFishID();
                            int fish = fishScores.get(fishTileID);
                            playerScore += fish;
                            playerOneScore.setText("Player 1: " + playerScore);
                        }
                        turn += 1;
                        if(ai.getPenguins().size() < 4) {
                            ai.randomStart();
                        }
                    }
                    else if(numPlayers==2){
                        if(button instanceof TileButton){
                            int fishTileID = ((TileButton) button).getFishID();
                            int fish = fishScores.get(fishTileID);
                            if(turn % 2 == 1){
                                playerScore += fish;
                            }else{
                                opponentScore += fish;
                            }
                            playerOneScore.setText("Player Score: " + playerScore);
                            playerTwoScore.setText("Player Score: " + opponentScore);
                        }
                    }
                    success = true;
                    turn += 1;
                    //System.out.println("You are now on turn: " + turn);
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
    }

    private void createGameTiles() {
        int fishID = 0;
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
                TileButton tileButton = new TileButton(hex,buttonView,fishID);
                //tileButton.setPrefSize(HEX_SIZE,HEX_SIZE);
                tileButton.setLayoutX(x);
                tileButton.setLayoutY(y);
                tileButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                //tileButton.setStyle(buttonStyle);
                //tileButton.setGraphic(new ImageView(imagePath));
                enableDragAndDropForButton(tileButton);
                //if(fishCount == 1){
                tileButton.setOnAction(e -> {
                    Penguin penguin = movePenguin(tileButton.getFishID());
                    if(penguin != null) {
                        tileButton.setGraphic(penguin.getImageView());
                        selected = null;
                    }
                });
                /*
                }else if(fishCount == 2)
                {
                    tileButton.setOnAction(e -> System.out.println("Clicked on tile of id: " + tileButton.getFishID()));
                }else{
                    tileButton.setOnAction(e -> System.out.println("Clicked on tile of id: " + tileButton.getFishID()));
                }*/
                fishTileButtons.add(tileButton);
                hex.setTranslateX(x);
                hex.setTranslateY(y);
                root.getChildren().add(tileButton);
                fishID += 1;
            }
        }
    }

    private void initializeFishTiles() {
        fishTiles.clear();
        for (int i = 0; i < 30; i++) fishTiles.add(1);
        for (int i = 0; i < 20; i++) fishTiles.add(2);
        for (int i = 0; i < 10; i++) fishTiles.add(3);
        Collections.shuffle(fishTiles);
        for(int i = 0;i < fishTiles.size();i++){
            fishScores.add(fishTiles.get(i));
        }
        bm.setStartSpots(fishTiles);
    }

    class TileButton extends Button{
        private HexTile hex;
        private ImageView imageView;
        private int fishID;
        public TileButton(HexTile hex, ImageView imageView, int fishID){
            this.hex = hex;
            this.imageView = imageView;
            this.fishID = fishID;
            imageView.setFitWidth(this.getWidth());
            imageView.setFitHeight(this.getHeight());
            this.getChildren().addAll(hex, imageView);
            this.setPrefWidth(hex.getBoundsInLocal().getWidth() + imageView.getFitWidth());
            this.setPrefHeight(hex.getBoundsInLocal().getHeight() + imageView.getFitHeight());
            this.setGraphic(imageView);
        }

        public int getFishID(){
            return this.fishID;
        }

        public void setFishID(int newFishID){
            this.fishID = newFishID;
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

    Penguin movePenguin(int fishID) {
        if(playerTwo.getPenguins().size() < 4) {return null;}
        Penguin validate = bm.penguinPresent(fishID);
        if(!(selected == null && validate == null)) {
            if (validate != null &&
                    ((turn % 2 == 1 && redPenguins.stream().anyMatch(a -> a.equals(validate.getImageView().getImage()))) ||
                            (turn % 2 == 0 && bluePenguins.stream().anyMatch(a -> a.equals(validate.getImageView().getImage()))))) {
                System.out.println(validate.getImageView().getImage() + " is not your penguin player " + turn%2);
                for (Image image : bluePenguins) {System.out.println("\t" + image);}
                return null;
            } else if (validate != null) {
                selected = validate;
                return null;
            }
            else {
                int[] old = selected.location;
                if (selected.setLocation(bm.activeSpots.get(fishID))) {
                    System.out.println("Moved penguin at " + Arrays.toString(old) +
                            " to " + Arrays.toString(selected.location));
                    turn++;
                    return selected;
                }
            }
        }
        //TO DO: Move the image of the penguin to the new button and indicate a new turn
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}