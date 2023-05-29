package com.battleship;

import javafx.scene.layout.Pane;

public class HoveringShip extends Pane {
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public HoveringShip(int type){
        this.type=type;
        this.setPrefSize(30, 180);
        this.setMaxSize(30, 180);
        this.getStyleClass().add("shipGeneral");
        this.getStyleClass().add("ship6v");
    }
    public void rotate(boolean hPlacing){
        if(!hPlacing){
            this.setPrefSize(180, 30);
            this.setMaxSize(180, 30);
        }else {
            this.setPrefSize(30, 180);
            this.setMaxSize(30, 180);
        }
    }
    public void rotateStylee(boolean hPlacing, int shipsToPlace){
        this.getStyleClass().remove("ship" + shipsToPlace + "v");
        this.getStyleClass().remove("ship" + shipsToPlace);
        this.getStyleClass().add("ship" + shipsToPlace + (hPlacing? "v" : ""));
    }
    public void resettingCss(int length){
        this.getStyleClass().remove("ship" + (length) + "v");
        this.getStyleClass().remove("ship" + (length));
        this.setPrefSize(30, 180);
        this.setMaxSize(30, 180);
        this.getStyleClass().add("ship" + (length - 1) + "v");
    }
}
