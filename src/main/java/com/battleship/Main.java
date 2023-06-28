package com.battleship;

import java.util.Objects;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static MainScene mainScene= new MainScene();

    @Override
    public void start(Stage primaryStage){
        Scene scene = new Scene(mainScene);
        primaryStage.setTitle("Battleship");
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(Config.RESOURCE)).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
