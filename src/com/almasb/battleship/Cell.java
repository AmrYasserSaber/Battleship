package com.almasb.battleship;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell extends Rectangle {
    public int x;
    public int y;
    public  Ship ship = null;
    public  boolean wasShot = false;
    public Board board;

    public Cell(int x, int y, Board board) {
        super(30, 30);
        this.board=board;
        this.x = x;
        this.y = y;
        setFill(Color.LIGHTGRAY);
        setStroke(Color.BLACK);
        setStrokeWidth(0);
    }
    public boolean shoot(){
        wasShot = true;
        setFill(Color.BLACK);
        setOpacity(0.5);

        if (ship != null) {
            ship.hit();
            //        ship has been hit adding bomb sound
            SoundHandling bomb = new SoundHandling("sounds/bomb.wav",1);
            bomb.play();
            setFill(Color.RED);
            setOpacity(0.5);
            if (!ship.isAlive()) {
                board.ships--;
            }
            return true;
        }
//          missed hit splashing sound
        SoundHandling splash =new SoundHandling("sounds/splash.wav",1);
        splash.play();
        return false;
    }
    }
