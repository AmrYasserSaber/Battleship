package com.battleship;

import java.util.Objects;
import java.util.Random;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BattleshipMain extends Application {
    public static final String PATH = System.getProperty("user.dir")+"/src/main/resources/com/battleship/";

    public static final String RESOURCE ="style.css";

    public static boolean hPlacing = false;

    public static StackPane basis;


    public static HoveringShip shipHover = new HoveringShip();

    private final Random random = new Random();
    private boolean running = false;

    private Board enemyBoard;

    private Board playerBoard;


    private int shipsToPlace = 6;


    private boolean enemyTurn = false;
    public Sidebar sidebar = new Sidebar();


    //    SoundHandling objects
    private final SoundHandling theme = new SoundHandling("sounds/theme.wav",0);
    private final SoundHandling click = new SoundHandling("sounds/Click.wav",1);
    private final SoundHandling win = new SoundHandling("sounds/win.wav",0);
    private final SoundHandling lose = new SoundHandling("sounds/lose.wav",0);
    private final SoundHandling gamePlay = new SoundHandling("sounds/gamePlay.wav",0);
//-----------
    private  Parent createMainScene() {
        theme.play();
        AnchorPane mainScene= new AnchorPane();
        mainScene.setPrefSize(600,800);
        mainScene.getStyleClass().add("anchor-pane");
        VBox menu = new VBox(50);
        menu.setPrefSize(600, 600);
        Image logo = new Image(PATH + "imgs/gameLogo.png", 400, 153.5, true, true);
        ImageView logoView = new ImageView(logo);

        Image startBtn = new Image(PATH + "imgs/start.png", 200, 62, true, true);
        ImageView startBtnView = new ImageView(startBtn);

        startBtnView.getStyleClass().add("startBtn");

        menu.getChildren().addAll(logoView, startBtnView);
        menu.setAlignment(Pos.CENTER);

        mainScene.getChildren().add(menu);

        startBtnView.setOnMouseClicked((MouseEvent event) -> {
//          play the click sound
            click.play();
            Parent game = createGame();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(game);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(RESOURCE)).toExternalForm());
            stage.setScene(scene);
            stage.show();
        });
        return mainScene;
    }
    private Parent createGame() {
        basis = new StackPane();
        basis.setMinSize(600, 800);
        BorderPane root = new BorderPane();
        root.setPrefSize(600, 800);
        root.getStyleClass().add("gamePlay");


        root.setRight(sidebar);

        enemyBoard = new Board(true, event -> {
            if (!running)
                return;

            Cell cell = (Cell) event.getSource();
            if (cell.wasShot)
                return;
            enemyTurn = !cell.shoot();

            if(!enemyTurn){
                sidebar.setScorestyle("-fx-font-size: 35px;");
                sidebar.setScoreVal(sidebar.getScoreVal()+5);
                sidebar.setScoreTxt(String.valueOf(sidebar.getScoreVal()));
            }

            if (enemyBoard.ships == 0) {
//              stop the gameplay sound
                gamePlay.stop();
//              playing win sound
                win.play();
                sidebar.setScorestyle("-fx-font-size: 30px;");
                sidebar.setScoreTxt("You Win");
            }

            if (enemyTurn) {
                enemyMove();
            }
        });

        playerBoard = new Board(false, event -> {
            if (running)
                return;

            Cell cell = (Cell) event.getSource();
            if (playerBoard.placeShip(new Ship(shipsToPlace, !hPlacing), cell.x, cell.y, true) && (--shipsToPlace == 1)) {
                sidebar.setScoreTxt("Game\nStarted");
                startGame();

            }
        });
        /* moves shipHover object with the mouse*/
        basis.setOnMouseMoved(event -> {
            double x = event.getX();
            double y = event.getY();
            shipHover.setTranslateX(x-15);
            shipHover.setTranslateY(y+1);
        });

        /* Changing shipHover image and dimensions on scroll */
        basis.setOnScroll(e -> {
            shipHover.rotate(hPlacing);
            hPlacing = shipHover.changeStyling(hPlacing, shipsToPlace);
        });

        VBox vbox = new VBox(50, enemyBoard, playerBoard);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(50));

        root.setCenter(vbox);
        basis.setAlignment(Pos.TOP_LEFT);
        basis.getChildren().addAll(root, shipHover);
        return basis;
    }


    private void enemyMove(){
        while (enemyTurn) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            Cell cell = playerBoard.getCell(x, y);
            if (cell.wasShot) {
                continue;
            }
            enemyTurn = cell.shoot();
            if(enemyTurn){
                sidebar.setScorestyle("-fx-font-size: 35px;");
                sidebar.setScoreVal(sidebar.getScoreVal()-5);
                sidebar.setScoreTxt(String.valueOf(sidebar.getScoreVal()));
            }

            if (playerBoard.ships == 0) {
//                stop the gameplay sound
                gamePlay.stop();
//                playing lose sound
                lose.play();
                sidebar.setScorestyle("-fx-font-size: 30px;");
                sidebar.setScoreTxt("You Lost");
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
    public void start(Stage primaryStage){
        Scene scene = new Scene(createMainScene());
        primaryStage.setTitle("Battleship");
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(RESOURCE)).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
