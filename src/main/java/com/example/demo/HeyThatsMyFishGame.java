package com.example.demo;


import javafx.geometry.Insets;
import javafx.scene.media.AudioClip;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Polygon;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.io.InputStream;
import java.util.*;
import java.util.List;


public class HeyThatsMyFishGame extends Application {

    private static final int NUM_ROWS = 8;
    private static final double HEX_SIZE = 40;
    private static final double BOARD_PADDING = 50;
    private static final double PAIRING_VERTICAL = 80;
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
    private Label playerOneScore;
    private Label playerTwoScore;
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
    private boolean allPlaced = false;
    private HBox currentPenguinDisplay;
    Image backgroundImage = new Image(getClass().getResourceAsStream("/BigBackGround.jpg"));
    BackgroundImage bgImage = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.root = new Pane();
        root.setBackground(new Background(bgImage));
        this.scene = new Scene(root, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.hide();

    }

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
        this.currentPenguinDisplay = penguinScreen;
        HBox pengAndScores = new HBox();
        penguinScreen.setAlignment(Pos.TOP_LEFT);
        //root.getChildren().add(penguinScreen); // Optionally manage penguins based on players
        VBox scoreScreen = createScoreBoardDisplay();
        scoreScreen.setBackground(Background.fill(Color.DEEPPINK));
        scoreScreen.setPadding(new Insets  (5,5,5,5));
        pengAndScores.getChildren().add(scoreScreen);
        pengAndScores.getChildren().add(penguinScreen);
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
        playerOneScore = new Label("Player 1: " + playerScore);
        playerTwoScore = new Label("Player 2: " + opponentScore);
        Font font = new Font("Times New Roman", 16);
        playerOneScore.setFont(font);
        playerTwoScore.setFont(font);





        scores.getChildren().addAll(playerOneScore, playerTwoScore);
        return scores;
    }

    private ArrayList<Image> addPenguins(HBox penguinDisplay, String baseFileName, int count) {
        turn = 1;
        ArrayList<Image> pengChosen = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            String imagePath = "/Pengs/" + baseFileName + i + ".jpg"; // Adjust the path as needed
            String opponentPenImagePath = null;
            if(imagePath == "BluePeng"){
                opponentPenImagePath = "/Pengs/" + "RedPeng" + i + ".jpg";
            }
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
            }else if(numPlayers==2){
                if(baseFileName=="BluePeng"){
                    enableDragAndDrop(penguinView);
                }else if(baseFileName=="RedPeng"){
                    enableDragAndDrop(penguinView);
                }
            }
            penguinDisplay.getChildren().add(penguinView);
        }
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
                Dragboard db = iv.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putImage(iv.getImage());
                db.setContent(content);
                event.consume();
            }
        });
        iv.setOnDragOver(new EventHandler <DragEvent>(){
            public void handle(DragEvent event){
                if(event.getGestureSource() != iv && event.getDragboard().hasImage()){
                    event.acceptTransferModes(TransferMode.MOVE);
                }

                event.consume();
            }
        });
        iv.setOnDragDropped(new EventHandler <DragEvent>(){
            public void handle(DragEvent event){
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasImage()) {
                    iv.setImage(db.getImage());
                    imageView.setImage(db.getImage());
                    success = true;
                }

                event.setDropCompleted(success);
                event.consume();
            }
        });
        iv.setOnDragDone(new EventHandler <DragEvent>(){
            public void handle(DragEvent event){
                if(event.getTransferMode() == TransferMode.MOVE){
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
                }
                event.consume();
            }
        });

        button.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event){
                Dragboard db = event.getDragboard();
                boolean success = false;

                if(db.hasImage()) {
                    double buttonWidth = button.getWidth();
                    double buttonHeight = button.getHeight();

                    Penguin newPeng = new Penguin(bm, new ImageView(imageView.getImage()));

                    boolean check = newPeng.setLocation(bm.activeSpots.get(fishTileButtons.indexOf(button)));
                    if(!check) {
                        //Future update: Add error message?
                        return;
                    } else if (turn%2 == 1) {
                        playerOne.addPenguinPlayer(newPeng);
                    } else if (numPlayers==2) {
                        playerTwo.addPenguinPlayer(newPeng);
                    } else //Not correct penguin
                        return;
                    AudioClip gameStartSound = new AudioClip(getClass().getResource("/Click.wav").toExternalForm());
                    gameStartSound.play();

                    newPeng.getImageView().setFitWidth(buttonWidth);
                    newPeng.getImageView().setFitHeight(buttonHeight);
                    button.setGraphic(newPeng.getImageView());

                    if(numPlayers==1){
                        if(button instanceof TileButton){
                            int fishTileID = ((TileButton) button).getFishID();
                            int fish = fishScores.get(fishTileID);
                            playerScore += fish;
                            playerOneScore.setText("Player 1: " + playerScore);
                        }
                        System.out.println("Main: Turn: " + (++turn));

                        if(ai.getPenguins().size() < 4) {
                            ImageView iv = new ImageView(redPenguins.get(ai.getPenguins().size()));
                            iv.setFitWidth(button.getWidth());
                            iv.setFitHeight(button.getHeight());
                            Penguin aiPeng = new Penguin(bm, iv);
                            int[] fishTile = ai.randomStart(aiPeng);

                            Optional<int[]> bmSelected = bm.activeSpots.stream().filter(Objects::nonNull).
                                    filter(a -> Arrays.equals(a,fishTile)).findFirst();

                            if(bmSelected.isPresent()){
                                final int FISHID = bm.activeSpots.indexOf(bmSelected.get());
                                aiPeng.setLocation(fishTile);
                                System.out.println("Main: AI placed penguin at " + Arrays.toString(aiPeng.location));

                                TileButton button = fishTileButtons.get(FISHID);
                                button.setGraphic(aiPeng.getImageView());
                                currentPenguinDisplay.getChildren().remove(4);
                                int fish = bmSelected.get()[3];
                                opponentScore += fish;
                                playerTwoScore.setText("Player 2: " + opponentScore);
                            } else {
                                System.out.println("Main: Error: could not find value of " + Arrays.toString(fishTile));
                                return;
                            }
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
                            playerOneScore.setText("Player 1: " + playerScore);
                            playerTwoScore.setText("Player 2: " + opponentScore);
                        }
                    }
                    success = true;
                    turn += 1;
                    System.out.println("Main: Turn: " + turn);
                }
                if((playerTwo != null && playerTwo.getPenguins().size() == 4) ||
                        (ai != null && ai.getPenguins().size() == 4)){
                    allPlaced = true;
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
    }

    void postMoveCheck(List<TileButton> buttons) {
        System.out.println("\t\t~~~~~Move complete, check for removals~~~~~");
        ArrayList<Integer> removedLocations = new ArrayList<>();
        ArrayList<Penguin> removedPenguins = playerOne.penguinIsolation();
        if(!removedPenguins.isEmpty()) {
            for(Penguin p : removedPenguins) {
                if(!bm.activeSpots.contains(p.location)) {
                    System.out.println("Main: Error: Cannot find location " + Arrays.toString(p.location));
                } else {
                    removedLocations.add(bm.activeSpots.indexOf(p.location));
                    System.out.println("Main: Remove activePenguin at " + Arrays.toString(p.location) + ": " +
                            bm.activePenguins.remove(p));
                    System.out.println("Main: Remove playerOne at " + Arrays.toString(p.location) + ": " +
                            playerOne.penguins.remove(p));
                }
            }
        }
        if(numPlayers == 1) {
            removedPenguins = ai.penguinIsolation();
        } else {
            removedPenguins = playerTwo.penguinIsolation();
        }if(!removedPenguins.isEmpty()) {
            for(Penguin p : removedPenguins) {
                if(!bm.activeSpots.contains(p.location)) {
                    System.out.println("Main: Error: Cannot find location " + Arrays.toString(p.location));
                } else {
                    removedLocations.add(bm.activeSpots.indexOf(p.location));
                    if(numPlayers == 1) {
                        System.out.println("Main: Remove activePenguin at " + Arrays.toString(p.location) + ": " +
                                bm.activePenguins.remove(p));
                        System.out.println("Main: Remove ai at " + Arrays.toString(p.location) + ": " +
                                ai.penguins.remove(p));
                    } else if(numPlayers == 2) {System.out.println("Main: Remove activePenguin at " + Arrays.toString(p.location) + ": " +
                            bm.activePenguins.remove(p));
                        System.out.println("Main: Remove playerTwo at " + Arrays.toString(p.location) + ": " +
                                playerTwo.penguins.remove(p));
                    }
                }
            }
        }
        try {
            removedLocations.addAll(bm.checkForIsolation());
        } catch (Exception e) {System.out.println("Main: No islands.");}
        if(!removedLocations.isEmpty()){
            Image image = new Image("/Water.PNG");
            for (Integer i : removedLocations) {
                //NOTE: POSSIBLE BREAK SPOT - removedLocations represents an index 0-59 in activeSpots
                //Are buttons and activeSpots starting at different points?
                //4/29 Does not appear to be causing any breaking
                if (i >= 0 && i < buttons.size() && i < bm.activeSpots.size()) {
                    // Create an ImageView and set it as the graphic for the button
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(HEX_SIZE * 2); // Adjust size as necessary
                    imageView.setFitWidth(HEX_SIZE*5);  // Adjust size as necessary
                    imageView.setPreserveRatio(true);

                    buttons.get(i).setGraphic(imageView);

                    int[] states = bm.activeSpots.get(i);
                    if (states != null && states.length > 0) {
                        states[0] = 1; // Assume 1 represents an 'active' state
                        bm.activeSpots.set(i, null); // Update the list with the modified array
                    }
                }
            }

        }

        System.out.println("\t\t~~~~~Removal check complete~~~~~");
    }

    private void createGameTiles() {
        int fishID = 0;
        int pairingVertical = 17;
        for (int row = 0; row < NUM_ROWS; row++) {
            int numColsInRow = row % 2 == 0 ? 7 : 8;
            for (int col = 0; col < numColsInRow; col++) {
                int fishCount = fishTiles.remove(0);
                String imagePath = "/Fish" + fishCount + ".png";
                Image fishImage = new Image(getClass().getResourceAsStream(imagePath),HEX_SIZE/0.5,HEX_SIZE/0.5,true,true);
                ImageView buttonView = new ImageView();
                buttonView.setImage(fishImage);
                HexTile hex = new HexTile(HEX_SIZE, fishCount);
                double offsetX = (row % 2 == 0) ? (HEX_SIZE * 1.5) / 2 : 0;
                double x = col * (HEX_SIZE * 1.90 + GAP) + offsetX + BOARD_PADDING+20;
                double y = row * (HEX_SIZE * Math.sqrt(3) + GAP / 4) + BOARD_PADDING+20 + pairingVertical;
                TileButton tileButton = new TileButton(hex,buttonView,fishID);
                tileButton.setLayoutX(x);
                tileButton.setLayoutY(y);
                tileButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                enableDragAndDropForButton(tileButton);
                tileButton.setOnAction(e -> {
                    System.out.println("Main: Clicked " + tileButton.getFishID() + "\t" + turn);
                    Penguin penguin = movePenguin(tileButton.getFishID());
                    if(penguin != null) {
                        tileButton.setGraphic(penguin.getImageView());
                        selected = null;

                        postMoveCheck(fishTileButtons);
                        if(numPlayers == 1 && !ai.getPenguins().isEmpty()) {
                            AIMove();
                        }
                        //Checks if there are any turns left to take
                        if(playerOne.getPenguins().isEmpty() &&
                                ((numPlayers == 2 && playerTwo.getPenguins().isEmpty())
                                        || (numPlayers == 1 && ai.getPenguins().isEmpty()))) {
                            //GAME OVER :)
                            System.out.println("Game over!");
                        } else if(playerOne.getPenguins().isEmpty() && numPlayers == 1) {
                            //AI looped
                            while(!ai.getPenguins().isEmpty()) {
                                AIMove();
                            }
                            System.out.println("Game over!");

                        } else if(numPlayers == 2 && (playerOne.getPenguins().isEmpty())){
                            turn = 0;
                        } else if(numPlayers == 2 && playerTwo.getPenguins().isEmpty()) {
                            turn = 1;
                        }
                    }
                });

                fishTileButtons.add(tileButton);
                hex.setTranslateX(x);
                hex.setTranslateY(y);
                root.getChildren().add(tileButton);
                fishID += 1;
            }
            pairingVertical += 17;
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


    void AIMove(){
        turn++;

        Random rand = new Random();
        Penguin aiPenguin = ai.getPenguins().get(rand.nextInt(0, ai.getPenguins().size()));
        int[] location  = ai.randomMove(aiPenguin);
        if(location == null) {
            postMoveCheck(fishTileButtons);
            if(ai.getPenguins().isEmpty()) return;
            else {
                aiPenguin = ai.getPenguins().get(rand.nextInt(0, ai.getPenguins().size()));
                location  = ai.randomMove(aiPenguin);
            }
        }

        final int tileIndex = bm.activeSpots.indexOf(location);
        if(tileIndex != -1) {
            System.out.println("Main: AI tileID chosen: " + tileIndex);
            System.out.println("Main: AI Penguin chosen: " + aiPenguin);
            TileButton aiButton = fishTileButtons.get(tileIndex);
            System.out.println(aiPenguin.setLocation(bm.activeSpots.get(tileIndex)));
            aiButton.setGraphic(aiPenguin.getImageView());
            postMoveCheck(fishTileButtons);
        } else {
            System.out.println("Main: Penguin (" + aiPenguin + ") or tile (" + tileIndex + ") not found");
        }

        //Update Score
        if(location != null){
            if(location.length >= 4){
                int fishScore = location[3];
                opponentScore += fishScore;
                playerTwoScore.setText("Player 2: " + opponentScore);
            }
        }
    }

    Penguin movePenguin(int fishID) {
        if(!allPlaced) {
            System.out.println("Main: Not enough penguins laid");
            return null;
        }
        Penguin validate = bm.penguinPresent(fishID);
        System.out.println("Main: Penguin present? " + validate);
        if(!(selected == null && validate == null)) {
            if(playerOne.getPenguins().isEmpty() || ((numPlayers == 2 && playerTwo.getPenguins().isEmpty()) || (numPlayers == 1 && ai.getPenguins().isEmpty()))){}
            else if (validate != null &&
                    ((turn % 2 == 1 && redPenguins.stream().anyMatch(a -> a.equals(validate.getImageView().getImage()))) ||
                            (turn % 2 == 0 && bluePenguins.stream().anyMatch(a -> a.equals(validate.getImageView().getImage()))))) {
                System.out.println("Main: " + validate.getImageView().getImage() + " is not your penguin player " + turn%2);
                return null;
            } if (validate != null) {
                System.out.println("Main: Penguin selected");
                selected = validate;
                return null;
            }
            else {
                int[] old = selected.location;

                if (selected.setLocation(bm.activeSpots.get(fishID))) {
                    System.out.println("Main: Moved penguin at " + Arrays.toString(old) +
                            " to " + Arrays.toString(selected.location));
                    AudioClip gameStartSound = new AudioClip(getClass().getResource("/Click.wav").toExternalForm());
                    gameStartSound.play();

                    //Update score
                    if(selected.location.length >= 4){
                        int fishScore = selected.location[3];
                        if(turn %2 == 1) {
                            playerScore += fishScore;
                            playerOneScore.setText("Player 1: " + playerScore);
                        } else {
                            opponentScore += fishScore;
                            playerTwoScore.setText("Player 2: " + opponentScore);
                        }

                    }

                    if((turn%2 == 1 && ((numPlayers == 1 && ai.getPenguins().isEmpty())
                            ||(numPlayers == 2 && playerTwo.getPenguins().isEmpty())))
                            || (numPlayers == 2 && playerOne.getPenguins().isEmpty())) {return selected;}
                    System.out.println("Main: Turn: " + (++turn));

                    return selected;
                }
            }
        }
        System.out.println("Main: " + Arrays.toString(bm.activeSpots.get(fishID)) + " (" + bm.activeSpots.get(fishID) + ") is not a valid location");

        return null;
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

    public static void main(String[] args) {
        launch(args);
    }
}