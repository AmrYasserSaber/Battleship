package com.battleship;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class MainScene extends AnchorPane {
    public static Game game = new Game();
    public VBox menu = new VBox(50);
    private final String PATH = System.getProperty("user.dir")+"/src/main/resources/com/battleship/";
    public static final String RESOURCE ="style.css";
    private final SoundHandling click = new SoundHandling("sounds/Click.wav",1);
    public MainScene() {
        this.setPrefSize(600,800);
        this.getStyleClass().add("anchor-pane");
        menu.setPrefSize(600, 600);
        Image logo = new Image(PATH + "imgs/gameLogo.png", 400, 153.5, true, true);
        ImageView logoView = new ImageView(logo);
        Image startBtn = new Image(PATH + "imgs/start.png", 200, 62, true, true);
        ImageView startBtnView = new ImageView(startBtn);
        startBtnView.getStyleClass().add("startBtn");
        menu.getChildren().addAll(logoView, startBtnView);
        menu.setAlignment(Pos.CENTER);
        this.getChildren().add(menu);
        startBtnView.setOnMouseClicked((MouseEvent event) -> {
//          play the click sound
            click.play();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(game);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(RESOURCE)).toExternalForm());
            stage.setScene(scene);
            stage.show();
        });
    }
}
