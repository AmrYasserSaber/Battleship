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
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.almasb.battleship.Board.Cell;

public class BattleshipMain extends Application {

    private boolean running = false;
    private Board enemyBoard;
    private Board playerBoard;

    private int shipsToPlace = 5;

    private boolean enemyTurn = false;

    private Random random = new Random();

    private  Parent createMainScene(){
        AnchorPane mainScene= new AnchorPane();
        mainScene.setPrefSize(600,500);
        mainScene.getStyleClass().add("anchor-pane");
        mainScene.setOnMouseClicked((MouseEvent event) -> {
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
        BorderPane root = new BorderPane();
        root.setPrefSize(600, 800);

        ArrayList<Integer> shipList = new ArrayList<Integer>(5);
        for (int i = 1; i <= 5; i++) {
            shipList.add(i);
        }
        root.setRight(renderShips(shipList));

        enemyBoard = new Board(true, event -> {
            if (!running)
                return;

            Cell cell = (Cell) event.getSource();
            if (cell.wasShot)
                return;

            enemyTurn = !cell.shoot();

            if (enemyBoard.ships == 0) {
                System.out.println("YOU WIN");
                System.exit(0);
            }

            if (enemyTurn)
                enemyMove();
        });

        playerBoard = new Board(false, event -> {
            if (running)
                return;

            Cell cell = (Cell) event.getSource();
            if (playerBoard.placeShip(new Ship(shipsToPlace, event.getButton() == MouseButton.PRIMARY), cell.x, cell.y)) {
                if (--shipsToPlace == 0) {
                    startGame();
                }
            }
        });

        VBox vbox = new VBox(50, enemyBoard, playerBoard);
        vbox.setAlignment(Pos.CENTER);

        root.setCenter(vbox);
        return root;
    }

    private Node renderShips(ArrayList<Integer> shipList) {
        int n=shipList.size();
        VBox sideBar = new VBox();
        for (int i=0;i<n;i++){
            HBox ship =new HBox();
            for (int j = 1; j <= shipList.get(i);j++) {
                ShipCell cell = new ShipCell();
                ship.getChildren().add(cell);
            }
            sideBar.getChildren().add(ship);
        }
        sideBar.setSpacing(50);
        sideBar.setPadding(new Insets(200, 50, 0, 50));

        return sideBar;
    }

    private void enemyMove() {
        while (enemyTurn) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            Cell cell = playerBoard.getCell(x, y);
            if (cell.wasShot)
                continue;

            enemyTurn = cell.shoot();

            if (playerBoard.ships == 0) {
                System.out.println("YOU LOSE");
                System.exit(0);
            }
        }
    }

    private void startGame() {
        // place enemy ships
        int type = 5;

        while (type > 0) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            if (enemyBoard.placeShip(new Ship(type, Math.random() < 0.5), x, y)) {
                type--;
            }
        }

        running = true;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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
