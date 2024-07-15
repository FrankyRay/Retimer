package com.frankyrayms.retimer;

import com.frankyrayms.retimer.controls.Timer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Retimer extends Application {
    private Stage timeStage;

    @Override
    public void start(Stage stage) {
        // Initiate Time and Control class
        Time time = new Time();
        Timer timer = new Timer(time);

        // Initiate control stage
        stage.setTitle("Retimer");
        stage.setScene(timer.getScene());
        stage.setWidth(400);
        stage.show();
        stage.setOnCloseRequest(e -> Platform.exit());

        // Initiate time stage
        timeStage = new Stage();
        timeStage.setTitle("Time");
        timeStage.setScene(time.getScene());
        timeStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}