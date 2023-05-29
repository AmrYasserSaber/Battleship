package com.battleship;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Sidebar extends VBox {
    private final Text scoreTxt = new Text(35, 75, "Map Out\nYour Strategy");

    private int scoreVal = 0;
    Sidebar(){
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("sideBar");
        StackPane score = new StackPane();
        score.setPrefWidth(100);
        score.setPrefHeight(400);
        ImageView scoreIcon = new ImageView(new Image(Utils.PATH + "imgs/scoreBoard.png"));
        scoreIcon.setFitWidth(150);
        scoreIcon.setPreserveRatio(true);
        Font scoreFont = Font.font("Thoma", 20);
        scoreTxt.setFont(scoreFont);
        scoreTxt.setFill(Color.web("#ffffffbb"));
        scoreTxt.setTextAlignment(TextAlignment.CENTER);
        score.getChildren().addAll(scoreIcon, scoreTxt);
        this.getChildren().add(score);
        this.setSpacing(5);
        this.setPadding(new Insets(20));
    }
    public void setScoreVal (int newValue){
        this.scoreVal= newValue;
    }

    public int getScoreVal() {
        return scoreVal;
    }

    public void setScoreTxt(String scoreTxt) {
        this.scoreTxt.setText(String.valueOf(scoreTxt));
    }

    public void setScoreStyle(String scoreStyle){
        this.scoreTxt.setStyle(scoreStyle);
    }
}
