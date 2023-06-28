package com.battleship;

import javafx.scene.Parent;


public class Ship extends Parent {
    public int type;
    public boolean hPlacing;
    private int health;

    public Ship(int type, boolean hPlacing) {
        this.type = type;
        this.hPlacing = hPlacing;
        health = type;
    }

    public void hit() {
        health--;
    }

    public boolean isAlive() {
        return health > 0;
    }
}