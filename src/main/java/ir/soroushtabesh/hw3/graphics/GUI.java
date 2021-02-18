package ir.soroushtabesh.hw3.graphics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        var fxmlLoader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Parent root = fxmlLoader.load();
        var scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("LZ77 - Soroush Tabesh");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
