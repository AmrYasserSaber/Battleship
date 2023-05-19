package com.almasb.battleship;

import java.util.Objects;
import java.util.Random;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;

import javax.sound.sampled.*;
import java.io.IOException;


import com.almasb.battleship.Board.Cell;


public class BattleshipMain extends Application {

    public static Pane shipHover = new Pane();
    public static StackPane basis = new StackPane();

    public static int scoreVal = 0;
    Text scoreTxt = new Text(35, 75, "Map Out\nYour Strategy");

    public final String path = System.getProperty("user.dir")+"/Battleship/src/com/almasb/battleship/";

    private boolean running = false;
    private Board enemyBoard;
    private Board playerBoard;

    private int shipsToPlace = 6;

    private boolean enemyTurn = false;

    public static boolean hPlacing = false;

    private final Random random = new Random();

    private final SoundHandling theme = new SoundHandling("sounds/theme.wav",0);
    private final SoundHandling click = new SoundHandling("sounds/Click.wav",1);
    private final  SoundHandling win = new SoundHandling("sounds/win.wav",0);
    private final SoundHandling lose = new SoundHandling("sounds/lose.wav",0);
    private final SoundHandling gamePlay = new SoundHandling("sounds/gamePlay.wav",0);

    public BattleshipMain() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        //this constructor only exists to throw exceptions
    }

    private  Parent createMainScene() {
        theme.play();
        AnchorPane mainScene= new AnchorPane();
        mainScene.setPrefSize(600,800);
        mainScene.getStyleClass().add("anchor-pane");
        VBox menu = new VBox(50);
        menu.setPrefSize(600, 600);

        Image logo = new Image(path + "imgs/gameLogo.png", 400, 153.5, true, true);
        ImageView logoView = new ImageView(logo);

        Image startBtn = new Image(path + "imgs/start.png", 200, 62, true, true);
        ImageView startBtnView = new ImageView(startBtn);

        startBtnView.getStyleClass().add("startBtn");

        menu.getChildren().addAll(logoView, startBtnView);
        menu.setAlignment(Pos.CENTER);

        mainScene.getChildren().add(menu);

        startBtnView.setOnMouseClicked((MouseEvent event) -> {
//            play the click sound
            click.play();
            Parent root = createGame();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        });
        return mainScene;
    }
    private Parent createGame() {
        basis.setMinSize(600, 800);
        BorderPane root = new BorderPane();
        root.setPrefSize(600, 800);
        root.getStyleClass().add("gamePlay");

        ArrayList<Integer> shipList = new ArrayList<>(5);
        for (int i = 1; i <= 5; i++) {
            shipList.add(i);
        }
        renderShips(shipList,root);

        enemyBoard = new Board(true, event -> {
            if (!running)
                return;

            Cell cell = (Cell) event.getSource();
            if (cell.wasShot)
                return;
            try {
                enemyTurn = !cell.shoot();
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                throw new RuntimeException(e);
            }

            if(!enemyTurn){
                scoreTxt.setStyle("-fx-font-size: 35px;");
                scoreVal += 10;
                scoreTxt.setText(String.valueOf(scoreVal));
            }

            if (enemyBoard.ships == 0) {
//                stop the gameplay sound
                gamePlay.stop();
//              playing win sound
                win.play();
                scoreTxt.setStyle("-fx-font-size: 30px;");
                scoreTxt.setText("You Win");
            }

            if (enemyTurn) {
                try {
                    enemyMove();
                } catch (UnsupportedAudioFileException | LineUnavailableException | IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        playerBoard = new Board(false, event -> {
            if (running)
                return;

            Cell cell = (Cell) event.getSource();
            if (playerBoard.placeShip(new Ship(shipsToPlace, event.getButton() == MouseButton.PRIMARY), cell.x, cell.y, true) && (--shipsToPlace == 1)) {
                    scoreTxt.setText("Game\nStarted");
                startGame();

            }
        });



        shipHover.setPrefSize(30, 150);
        shipHover.setMaxSize(30, 150);

        shipHover.getStyleClass().add("shipGeneral");
        shipHover.getStyleClass().add("ship5v");

        /* moves shipHover object with the mouse*/
        basis.setOnMouseMoved(event -> {
            double x = event.getX();
            double y = event.getY();
            shipHover.setTranslateX(x-15);
            shipHover.setTranslateY(y+1);
        });

        /* Changing shipHover image and dimensions on scroll */
        basis.setOnScroll(e -> {
            if(!hPlacing){
                shipHover.setPrefSize(150, 30);
                shipHover.setMaxSize(150, 30);

            }else {
                shipHover.setPrefSize(30, 150);
                shipHover.setMaxSize(30, 150);
            }

            BattleshipMain.shipHover.getStyleClass().remove("ship" + shipsToPlace + "v");
            BattleshipMain.shipHover.getStyleClass().remove("ship" + shipsToPlace);
            BattleshipMain.shipHover.getStyleClass().add("ship" + shipsToPlace + (hPlacing? "v" : ""));

            hPlacing = !hPlacing;

        });

        VBox vbox = new VBox(50, enemyBoard, playerBoard);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(50));

        root.setCenter(vbox);
        basis.setAlignment(Pos.TOP_LEFT);
        basis.getChildren().addAll(root, shipHover);
        return basis;
    }

    private void renderShips(ArrayList<Integer> shipList, BorderPane root) {
        int n=shipList.size();
        VBox sideBar = new VBox();
        sideBar.setAlignment(Pos.CENTER);

        HBox ships = new HBox();

        sideBar.getStyleClass().add("sideBar");

        Font scoreFont = Font.font("Thoma", 20);
        for (int i=0;i<n;i++){
            VBox ship =new VBox();
            for (int j = 1; j <= shipList.get(i);j++) {
                ShipCell cell = new ShipCell();
                cell.setOpacity(0);
                ship.getChildren().add(cell);
                ship.setAlignment(Pos.CENTER_LEFT);
            }
            ships.getChildren().add(ship);
        }

        StackPane score = new StackPane();
        score.setPrefWidth(100);
        score.setPrefHeight(400);

        ImageView scoreIcon = new ImageView(new Image(path + "imgs/scoreBoard.png"));
        scoreIcon.setFitWidth(150);
        scoreIcon.setPreserveRatio(true);


        scoreTxt.setFont(scoreFont);
        scoreTxt.setFill(Color.web("#ffffffbb"));
        scoreTxt.setTextAlignment(TextAlignment.CENTER);

        score.getChildren().addAll(scoreIcon, scoreTxt);

        sideBar.getChildren().add(score);
        sideBar.getChildren().add(ships);
        sideBar.setSpacing(5);
        sideBar.setPadding(new Insets(20));

        root.setRight(sideBar);
    }

    private void enemyMove() throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException {
        while (enemyTurn) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            Cell cell = playerBoard.getCell(x, y);
            if (cell.wasShot) {
                continue;
            }
            Thread.sleep(100);
            enemyTurn = cell.shoot();

            if (playerBoard.ships == 0) {
//                stop the gameplay sound
                gamePlay.stop();
//                playing lose sound
                lose.play();
                scoreTxt.setStyle("-fx-font-size: 30px;");
                scoreTxt.setText("You Lost");
            }
        }
    }

    private void startGame(){
//        stop the theme song and start the gameplay song
        theme.stop();
        gamePlay.play();
        /* place enemy ships */
        int type = 6;

        while (type > 1) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);


            if (enemyBoard.placeShip(new Ship(type, Math.random() < 0.5), x, y, false)) {
                type--;
            }
        }

        running = true;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        Scene scene = new Scene(createMainScene());
        primaryStage.setTitle("Battleship");
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
