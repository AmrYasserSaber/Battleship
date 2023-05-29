package com.battleship;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Random;

public class Game extends StackPane {
    private final Sidebar sidebar = new Sidebar();
    private Board enemyBoard;
    private Board playerBoard;
    private boolean running = false;
    private boolean enemyTurn = false;
    private final Random random = new Random();
    private int shipsToPlace = 6;
    public static HoveringShip shipHover = new HoveringShip();
    public static boolean hPlacing = false;
    //    SoundHandling objects
    public final  SoundHandling theme = new SoundHandling("sounds/theme.wav",0);
    private final SoundHandling win = new SoundHandling("sounds/win.wav",0);
    private final SoundHandling lose = new SoundHandling("sounds/lose.wav",0);
    private final SoundHandling gamePlay = new SoundHandling("sounds/gamePlay.wav",0);
    //-----------
    public Game(){
        BorderPane root = new BorderPane();
        this.setMinSize(600, 800);
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
            if (playerBoard.placeShip(new Ship(shipsToPlace, !Game.hPlacing), cell.x, cell.y, true) && (--shipsToPlace == 1)) {
                sidebar.setScoreTxt("Game\nStarted");
                startGame();

            }
        });
        /* moves shipHover object with the mouse*/
        this.setOnMouseMoved(event -> {
            double x = event.getX();
            double y = event.getY();
            Game.shipHover.setTranslateX(x-15);
            Game.shipHover.setTranslateY(y+1);
        });
        /* Changing shipHover image and dimensions on scroll */
        this.setOnScroll(e -> {
            Game.shipHover.rotate(Game.hPlacing);
            Game.hPlacing = Game.shipHover.changeStyling(Game.hPlacing, shipsToPlace);
        });
        VBox boardsBox = new VBox(50, enemyBoard, playerBoard);
        boardsBox.setAlignment(Pos.CENTER);
        boardsBox.setPadding(new Insets(50));
        root.setCenter(boardsBox);
        this.setAlignment(Pos.TOP_LEFT);
        this.getChildren().addAll(root, Game.shipHover);
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

}
