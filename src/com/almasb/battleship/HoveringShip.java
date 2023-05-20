package com.almasb.battleship;

import javafx.scene.layout.Pane;

public class HoveringShip extends Pane {
    public HoveringShip(){
        this.setPrefSize(30, 150);
        this.setMaxSize(30, 150);

        this.getStyleClass().add("shipGeneral");
        this.getStyleClass().add("ship5v");
    }
    public void rotate(boolean hPlacing){
        if(!hPlacing){
            this.setPrefSize(150, 30);
            this.setMaxSize(150, 30);

        }else {
            this.setPrefSize(30, 150);
            this.setMaxSize(30, 150);
        }

    }
    public boolean changeStyling(boolean hPlacing,int shipsToPlace){
        this.getStyleClass().remove("ship" + shipsToPlace + "v");
        this.getStyleClass().remove("ship" + shipsToPlace);
        this.getStyleClass().add("ship" + shipsToPlace + (hPlacing? "v" : ""));

        return (!hPlacing);
    }
}
