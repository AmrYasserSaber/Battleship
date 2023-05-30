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
    public void rotateStyle(boolean hPlacing, int type){
        this.getStyleClass().remove("ship" + type + "v");
        this.getStyleClass().remove("ship" + type);
        this.getStyleClass().add("ship" + type + (hPlacing? "v" : ""));
    }
    public void setStyling(boolean hPlacing,int type){
        this.type=type;
        this.getStyleClass().clear();
        this.getStyleClass().add("shipGeneral");
        if(hPlacing){
            this.getStyleClass().add("ship"+type);
            this.setPrefSize(180, 30);
            this.setMaxSize(180, 30);
        }
        else {
            this.getStyleClass().add("ship"+this.type+"v");
            this.setPrefSize(30, 180);
            this.setMaxSize(30, 180);
        }
    }
}
