package com.battleship;

import java.util.Objects;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BattleshipMain extends Application {
    public static final String PATH = System.getProperty("user.dir")+"/src/main/resources/com/battleship/";

    public static final String RESOURCE ="style.css";


    public static MainScene mainScene= new MainScene();

    //    SoundHandling objects


    @Override
    public void start(Stage primaryStage){
        Scene scene = new Scene(mainScene);
        primaryStage.setTitle("Battleship");
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(RESOURCE)).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        MainScene.game.theme.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
