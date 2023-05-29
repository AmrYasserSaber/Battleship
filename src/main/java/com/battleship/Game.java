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
    public HoveringShip shipHover = new HoveringShip(shipsToPlace);
    public static boolean hPlacing = false;
    //    SoundHandling objects
    public final  SoundHandling theme = new SoundHandling("sounds/theme.wav",0);
    private final SoundHandling win = new SoundHandling("sounds/win.wav",0);
    private final SoundHandling lose = new SoundHandling("sounds/lose.wav",0);
    //-----------
    public Game(SoundHandling gamePlay){
        BorderPane root = new BorderPane();
        this.setMinSize(600, 800);
        root.setPrefSize(600, 800);
        root.getStyleClass().add("gamePlay");
        root.setRight(sidebar);
        /* moves shipHover object with the mouse*/
        this.setOnMouseMoved(event -> {
            double x = event.getX();
            double y = event.getY();
            shipHover.setTranslateX(x-15);
            shipHover.setTranslateY(y+1);
        });
        /* Changing shipHover image and dimensions on scroll */
        this.setOnScroll(e -> {
            shipHover.rotate(Game.hPlacing);
            shipHover.rotateStylee(Game.hPlacing, shipsToPlace);
            hPlacing=!hPlacing;
        });
        enemyBoard = new Board(true, event -> {
            if (!running)
                return;

            Cell cell = (Cell) event.getSource();
            if (cell.wasShot)
                return;
            enemyTurn = !cell.shoot();

            if(!enemyTurn){
                sidebar.setScoreStyle("-fx-font-size: 35px;");
                sidebar.setScoreVal(sidebar.getScoreVal()+5);
                sidebar.setScoreTxt(String.valueOf(sidebar.getScoreVal()));
            }

            if (enemyBoard.ships == 0) {
//              stop the gameplay sound
                gamePlay.stop();
//              playing win sound
                win.play();
                sidebar.setScoreStyle("-fx-font-size: 30px;");
                sidebar.setScoreTxt("You Win");
            }

            if (enemyTurn) {
                enemyMove(gamePlay);
            }
        });
        playerBoard = new Board(false, event -> {
            if (running)
                return;

            Cell cell = (Cell) event.getSource();
            if (playerBoard.placeShip(new Ship(shipsToPlace, !Game.hPlacing), cell.x, cell.y, true)) {
                HoveringShip copyShip =playerBoard.copyHoveringShip(shipHover, cell.x, cell.y);
                if(copyShip!=null){
                    this.getChildren().add(copyShip);
                    shipHover.resettingCss(shipsToPlace);
                    shipHover.setType(--shipsToPlace);
                    hPlacing=false;
                }
                if((shipsToPlace == 1)){
                    sidebar.setScoreTxt("Game\nStarted");
                    startGame(gamePlay);
                }
            }
        });
        VBox boardsBox = new VBox(50, enemyBoard, playerBoard);
        boardsBox.setAlignment(Pos.CENTER);
        boardsBox.setPadding(new Insets(50));
        root.setCenter(boardsBox);
        this.setAlignment(Pos.TOP_LEFT);
        this.getChildren().addAll(root, shipHover);
    }
    private void enemyMove(SoundHandling gamePlay){
        while (enemyTurn) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            Cell cell = playerBoard.getCell(x, y);
            if (cell.wasShot) {
                continue;
            }
            enemyTurn = cell.shoot();
            if(enemyTurn){
                sidebar.setScoreStyle("-fx-font-size: 35px;");
                sidebar.setScoreVal(sidebar.getScoreVal()-5);
                sidebar.setScoreTxt(String.valueOf(sidebar.getScoreVal()));
            }
            if (playerBoard.ships == 0) {
//                stop the gameplay sound
                gamePlay.stop();
//                playing lose sound
                lose.play();
                sidebar.setScoreStyle("-fx-font-size: 30px;");
                sidebar.setScoreTxt("You Lost");
            }
        }
    }
    private void startGame(SoundHandling gamePlay){
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
