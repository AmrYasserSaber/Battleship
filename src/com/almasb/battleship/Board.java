package com.almasb.battleship;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.lang.reflect.Field;
import com.almasb.battleship.BattleshipMain;

public class Board extends Parent {
    private VBox rows = new VBox();
    private boolean enemy = false;
    public int ships = 5;

    public static void copyProperties(Object source, Object target) {
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        // Get all fields from the source object
        Field[] sourceFields = sourceClass.getDeclaredFields();

        // Copy the value of each field to the corresponding field in the target object
        for (Field sourceField : sourceFields) {
            try {
                Field targetField = targetClass.getDeclaredField(sourceField.getName());
                sourceField.setAccessible(true);
                targetField.setAccessible(true);
                targetField.set(target, sourceField.get(source));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Ignore fields that don't exist in the target object
            }
        }
    }

    public Board(boolean enemy, EventHandler<? super MouseEvent> handler) {
        this.enemy = enemy;
        for (int y = 0; y < 10; y++) {
            HBox row = new HBox();
            for (int x = 0; x < 10; x++) {
                Cell c = new Cell(x, y, this);
                c.setOpacity(0.2);
                c.getStyleClass().add("cell");
                c.setOnMouseClicked(handler);
                row.getChildren().add(c);
                row.getStyleClass().add("cell");
            }

            rows.getChildren().add(row);
        }
//        rows.getStyleClass().add("cell");

        getChildren().add(rows);
    }

    public boolean placeShip(Ship ship, int x, int y, boolean playerTurn) {

        if (canPlaceShip(ship, x, y)) {
            int length = ship.type;

//            System.out.println(length);
            if(playerTurn){

                double xBase = 35;
                double yBase = 425;
                /*-----------------Placing the image---------------------*/
                Pane lolD = new Pane();
                lolD.getStyleClass().addAll(BattleshipMain.shipHover.getStyleClass());
                lolD.setPrefSize(BattleshipMain.shipHover.getWidth(), BattleshipMain.shipHover.getHeight());
                lolD.setMaxSize(BattleshipMain.shipHover.getWidth(), BattleshipMain.shipHover.getHeight());
                double eqX = xBase + 30 * x + (BattleshipMain.hPlacing ? 17 : 15);
                lolD.setTranslateX(eqX);
                double eqY = yBase + 30 * y + (BattleshipMain.hPlacing ? 4 : 0);
                lolD.setTranslateY(eqY);

                BattleshipMain.basis.getChildren().add(lolD);

                double xVal =  BattleshipMain.shipHover.getTranslateX();
                double yVal =  BattleshipMain.shipHover.getTranslateY();


//                System.out.printf("\n\n\n\n\n\n1: cellNum: %d %d \n", x, y);
//
//                System.out.printf("getCell: %f %f \n", getCell(x, y).getTranslateX(), getCell(x, y).getTranslateY());
//                System.out.printf("predict: %f %f \n", eqX, eqY);
//                System.out.printf("shipHover: %f %f \n", xVal, yVal);
    //            getCell(x, y).getLayoutX();
    //            getCell(x, y).getLayoutY();



                /*----------------- -------------- ---------------------*/

                /*-------------  Resetting CSS Settings to avoid overwriting issues -----------*/
                BattleshipMain.shipHover.getStyleClass().remove("ship" + String.valueOf(length) + "v");
                BattleshipMain.shipHover.getStyleClass().remove("ship" + String.valueOf(length));
                BattleshipMain.shipHover.setPrefSize(30, 150);
                BattleshipMain.shipHover.setMaxSize(30, 150);
                BattleshipMain.shipHover.getStyleClass().add("ship" + String.valueOf(length-1) + "v");
                /*------------- --------------------------------------------------- -----------*/

            }
//            System.out.println(BattleshipMain.hPlacing);
            /*here the condition depends on whether it's the player turn or not*/
            if ((playerTurn ? !BattleshipMain.hPlacing : ship.vertical)) {

                for (int i = y; i < y + length; i++) {
                    Cell cell = getCell(x, i);
                    cell.ship = ship;
                    if (!enemy) {
                        cell.setFill(Color.LIGHTGREEN);
                        cell.setStroke(Color.GREEN);
                        cell.setOpacity(0.7);
                    }
                }
            }
            else {
                for (int i = x; i < x + length; i++) {
                    Cell cell = getCell(i, y);
                    cell.ship = ship;
                    if (!enemy) {
                        cell.setFill(Color.LIGHTGREEN);
                        cell.setStroke(Color.GREEN);
                        cell.setOpacity(0.7);

                    }
                }
            }


            BattleshipMain.hPlacing = false;

            return true;
        }

        return false;
    }

    public Cell getCell(int x, int y) {

        return (Cell)((HBox)rows.getChildren().get(y)).getChildren().get(x);
    }

    private Cell[] getNeighbors(int x, int y) {
        Point2D[] points = new Point2D[] {
                new Point2D(x - 1, y),
                new Point2D(x + 1, y),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1)
        };

        List<Cell> neighbors = new ArrayList<Cell>();

        for (Point2D p : points) {
            if (isValidPoint(p)) {
                neighbors.add(getCell((int)p.getX(), (int)p.getY()));
            }
        }

        return neighbors.toArray(new Cell[0]);
    }

    private boolean canPlaceShip(Ship ship, int x, int y) {
        int length = ship.type;

        if (ship.vertical) {
            for (int i = y; i < y + length; i++) {
                if (!isValidPoint(x, i))
                    return false;

                Cell cell = getCell(x, i);
                if (cell.ship != null)
                    return false;

                for (Cell neighbor : getNeighbors(x, i)) {
                    if (!isValidPoint(x, i))
                        return false;

                    if (neighbor.ship != null)
                        return false;
                }
            }
        }
        else {
            for (int i = x; i < x + length; i++) {
                if (!isValidPoint(i, y))
                    return false;

                Cell cell = getCell(i, y);
                if (cell.ship != null)
                    return false;

                for (Cell neighbor : getNeighbors(i, y)) {
                    if (!isValidPoint(i, y))
                        return false;

                    if (neighbor.ship != null)
                        return false;
                }
            }
        }

        return true;
    }

    private boolean isValidPoint(Point2D point) {
        return isValidPoint(point.getX(), point.getY());
    }

    private boolean isValidPoint(double x, double y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    public class Cell extends Rectangle {
        public int x, y;
        public Ship ship = null;
        public boolean wasShot = false;

        private Board board;

        public Cell(int x, int y, Board board) {
            super(30, 30);
            this.x = x;
            this.y = y;
            this.board = board;
            setFill(Color.LIGHTGRAY);
            setStroke(Color.BLACK);
            setStrokeWidth(0);
        }

        public boolean shoot() {
            wasShot = true;
            setFill(Color.BLACK);
            setOpacity(0.5);

            if (ship != null) {
                ship.hit();
                setFill(Color.RED);
                setOpacity(0.5);
                if (!ship.isAlive()) {
                    board.ships--;
                }
                return true;
            }

            return false;
        }
    }
}