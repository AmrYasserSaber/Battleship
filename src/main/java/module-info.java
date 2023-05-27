module com.example.battleship {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.battleship to javafx.fxml;
    exports com.battleship;
}