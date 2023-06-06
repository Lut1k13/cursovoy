package com.example.filescontrol;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    /**
     * @param stage stage
     * @throws IOException exception
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("auth-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Контроль файлов");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args args for console
     */
    public static void main(String[] args) {
        launch();
    }
}