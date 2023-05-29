package com.battleship;

import java.util.Objects;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BattleshipMain extends Application {
    public static final String PATH = System.getProperty("user.dir")+"/src/main/resources/com/battleship/";

    public static final String RESOURCE ="style.css";

    public static boolean hPlacing = false;

    public static HoveringShip shipHover = new HoveringShip();
    public static Game game = new Game();

    //    SoundHandling objects
    private final SoundHandling click = new SoundHandling("sounds/Click.wav",1);
//-----------
    private  Parent createMainScene() {
        game.theme.play();
        AnchorPane mainScene= new AnchorPane();
        mainScene.setPrefSize(600,800);
        mainScene.getStyleClass().add("anchor-pane");
        VBox menu = new VBox(50);
        menu.setPrefSize(600, 600);
        Image logo = new Image(PATH + "imgs/gameLogo.png", 400, 153.5, true, true);
        ImageView logoView = new ImageView(logo);

        Image startBtn = new Image(PATH + "imgs/start.png", 200, 62, true, true);
        ImageView startBtnView = new ImageView(startBtn);

        startBtnView.getStyleClass().add("startBtn");

        menu.getChildren().addAll(logoView, startBtnView);
        menu.setAlignment(Pos.CENTER);

        mainScene.getChildren().add(menu);

        startBtnView.setOnMouseClicked((MouseEvent event) -> {
//          play the click sound
            click.play();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(game);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(RESOURCE)).toExternalForm());
            stage.setScene(scene);
            stage.show();
        });
        return mainScene;
    }

    @Override
    public void start(Stage primaryStage){
        Scene scene = new Scene(createMainScene());
        primaryStage.setTitle("Battleship");
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(RESOURCE)).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
