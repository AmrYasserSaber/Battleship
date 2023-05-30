package com.battleship;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class MainScene extends AnchorPane {
    public VBox menu = new VBox(50);
    private final SoundHandling click = new SoundHandling("sounds/Click.wav",1);

    private final SoundHandling theme=new SoundHandling("sounds/theme.wav",0);
    public MainScene() {
        this.setPrefSize(600,800);
        this.getStyleClass().add("anchor-pane");
        menu.setPrefSize(600, 600);
        Image logo = new Image(Config.PATH + "imgs/gameLogo.png", 400, 153.5, true, true);
        ImageView logoView = new ImageView(logo);
        Image startBtn = new Image(Config.PATH + "imgs/start.png", 200, 62, true, true);
        ImageView startBtnView = new ImageView(startBtn);
        startBtnView.getStyleClass().add("startBtn");
        menu.getChildren().addAll(logoView, startBtnView);
        menu.setAlignment(Pos.CENTER);
        this.getChildren().add(menu);
        theme.play();
        startBtnView.setOnMouseClicked((MouseEvent event) -> {
            Game game = new Game(theme);
//          play the click sound
            click.play();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(game);
            scene.setOnKeyPressed(event1 -> {
                switch (event1.getCode()) {
                    case LEFT ->
                        // Left arrow key pressed
                            game.getaSmallerShip();
                    case RIGHT ->
                        // Right arrow key pressed
                            game.getaBiggerShip();
                    default -> {}
                }
            });

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(Config.RESOURCE)).toExternalForm());
            stage.setScene(scene);
            stage.show();
        });

    }
}
