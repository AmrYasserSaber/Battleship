package com.almasb.battleship;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

    public class ShipCell extends Rectangle {
        public ShipCell() {
            super(30, 30);
            setFill(Color.LIGHTGRAY);
            setStroke(Color.BLACK);
        }
}
