package sample;

/**
 * Created by timurg
 * on 31.08.2021
 * project name TGText
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        loader.setControllerFactory(t -> new Controller(new EditorModel()));

        stage.setScene(new Scene(loader.load()));

        stage.setMinWidth(1024);
        stage.setMinHeight(600);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}