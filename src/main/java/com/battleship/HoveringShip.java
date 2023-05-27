package com.battleship;

import javafx.scene.layout.Pane;

public class HoveringShip extends Pane {
    public HoveringShip(){
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
    public boolean changeStyling(boolean hPlacing,int shipsToPlace){
        this.getStyleClass().remove("ship" + shipsToPlace + "v");
        this.getStyleClass().remove("ship" + shipsToPlace);
        this.getStyleClass().add("ship" + shipsToPlace + (hPlacing? "v" : ""));
        return (!hPlacing);
    }
}
