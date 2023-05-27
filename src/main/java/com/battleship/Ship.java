package com.battleship;

import javafx.scene.Parent;


public class Ship extends Parent {
    public int type;
    public boolean vertical;
    private int health;

    public Ship(int type, boolean vertical) {
        this.type = type;
        this.vertical = vertical;
        health = type;
    }

    public void hit() {
        health--;
    }

    public boolean isAlive() {
        return health > 0;
    }
}