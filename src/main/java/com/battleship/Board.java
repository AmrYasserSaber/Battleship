package com.battleship;

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


public class Board extends Parent {
    private final VBox rows = new VBox();
    private final boolean enemy ;
    public int ships = 5;



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

        getChildren().add(rows);
    }

    public boolean placeShip(Ship ship, int x, int y, boolean playerTurn) {
        if (canPlaceShip(ship, x, y)) {
            int length = ship.type;
            if(playerTurn){
                double xBase = 35;
                double yBase = 425;
                /*-----------------Placing the image---------------------*/
                Pane lolD = new Pane();
                lolD.getStyleClass().addAll(Game.shipHover.getStyleClass());
                lolD.setPrefSize(Game.shipHover.getWidth(), Game.shipHover.getHeight());
                lolD.setMaxSize(Game.shipHover.getWidth(), Game.shipHover.getHeight());
                double eqX = xBase + 30 * x + (Game.hPlacing ? 17 : 18);
                lolD.setTranslateX(eqX);
                double eqY = yBase + 30 * y + (Game.hPlacing ? 4 : 0);
                lolD.setTranslateY(eqY);

                MainScene.game.getChildren().add(lolD);



                /*----------------- -------------- ---------------------*/

                /*-------------  Resetting CSS Settings to avoid overwriting issues -----------*/
                Game.shipHover.getStyleClass().remove("ship" + (length) + "v");
                Game.shipHover.getStyleClass().remove("ship" + (length));
                Game.shipHover.setPrefSize(30, 180);
                Game.shipHover.setMaxSize(30, 180);
                Game.shipHover.getStyleClass().add("ship" + (length - 1) + "v");
                /*------------- --------------------------------------------------- -----------*/

            }
            /*here the condition depends on whether it's the player turn or not*/
            if ((playerTurn ? !Game.hPlacing : ship.vertical)) {
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
            Game.hPlacing = false;

            return true;
        }

        return false;
    }

    public Cell getCell(int x, int y) {
        return (Cell)((HBox)rows.getChildren().get(y)).getChildren().get(x);
    }

    private Cell[] getNeighbors(int x, int y) {
        Point2D[] points = new Point2D[] {
                new Point2D((double) x - 1, y),
                new Point2D((double) x + 1, y),
                new Point2D(x,(double) y - 1),
                new Point2D(x,(double) y + 1)
        };

        List<Cell> neighbors = new ArrayList<>();

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
}
