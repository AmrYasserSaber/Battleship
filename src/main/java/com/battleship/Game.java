package com.battleship;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Game extends StackPane {
    ArrayList<Integer> listOfShips = new ArrayList<>(Arrays.asList(2,3,4,5,6));
    private final Sidebar sidebar = new Sidebar();
    private Board enemyBoard;
    private Board playerBoard;
    private boolean running = false;
    private boolean enemyTurn = false;
    private final Random random = new Random();
    public HoveringShip shipHover = new HoveringShip(6);
    public boolean hPlacing = false;
    //    SoundHandling objects
    private final SoundHandling win = new SoundHandling("sounds/win.wav",0);
    private final SoundHandling lose = new SoundHandling("sounds/lose.wav",0);
    private final SoundHandling gamePlay = new SoundHandling("sounds/gamePlay.wav",0);
    //-----------
    public Game(SoundHandling theme){
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
            shipHover.rotate(hPlacing);
            shipHover.rotateStyle(hPlacing, shipHover.getType());
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
            if (playerBoard.placeShip(new Ship(shipHover.getType(), hPlacing), cell.x, cell.y, true,hPlacing)) {
                HoveringShip copyShip =playerBoard.copyHoveringShip(shipHover, cell.x, cell.y,hPlacing);
                if(copyShip!=null){
                    this.getChildren().add(copyShip);
                    listOfShips.remove(Integer.valueOf(shipHover.getType()));
                    if (!listOfShips.isEmpty()){
                        int randomInt = (int)Math.floor(Math.random() * (listOfShips.size()) + 0);
                        shipHover.setStyling(hPlacing,listOfShips.get(randomInt));
                    }
                    hPlacing=false;
                }
                if(listOfShips.isEmpty()){
                    shipHover.getStyleClass().clear();
                    sidebar.setScoreTxt("Game\nStarted");
                    theme.stop();
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
        gamePlay.play();
        /* place enemy ships */
        int type = 6;

        while (type > 1) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);


            if (enemyBoard.placeShip(new Ship(type, Math.random() < 0.5), x, y, false ,hPlacing)) {
                type--;
            }
        }

        running = true;
    }
    void getaSmallerShip() {
        int newType=shipHover.getType()-1;
        if(!listOfShips.contains(newType))
            return;
        shipHover.setStyling(hPlacing,newType);
    }
    void getaBiggerShip() {
        int newIndex = listOfShips.indexOf(shipHover.getType())+1;
        if(newIndex > listOfShips.size()-1) {
            return;
        }
        int newType= listOfShips.get(newIndex);
        shipHover.setStyling(hPlacing,newType);
    }

}
