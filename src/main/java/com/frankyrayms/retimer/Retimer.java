package com.frankyrayms.retimer;

import com.frankyrayms.retimer.controls.Timer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Retimer extends Application {
    private Stage timeStage;

    @Override
    public void start(Stage stage) {
        // Initiate Time and Control class
        Time time = new Time();
        // Initiate time stage
        timeStage = new Stage();
//        timeStage.initStyle(StageStyle.UNDECORATED);
        timeStage.setTitle("Time");
        timeStage.setScene(time.getScene());
//        timeStage.setAlwaysOnTop(true);

        // Initiate control stage
        Timer timer = new Timer(time, this);
        stage.setTitle("Retimer");
        stage.setScene(timer.getScene());
        stage.setWidth(400);
        stage.setOnCloseRequest(e -> Platform.exit());


        stage.show();
        timeStage.show();
    }

    public boolean lockTimeStage() {
        StageStyle timeStyle = timeStage.getStyle();

        Stage newTimeStage = new Stage();
        newTimeStage.initStyle(timeStage.getStyle() == StageStyle.UNDECORATED
                ? StageStyle.DECORATED
                : StageStyle.UNDECORATED);
        newTimeStage.setScene(timeStage.getScene());
        newTimeStage.setTitle(timeStage.getTitle());
        newTimeStage.setX(timeStage.getX());
        newTimeStage.setY(timeStage.getY());

        timeStage.close();
        newTimeStage.show();
        timeStage = newTimeStage;

        return timeStage.getStyle() == StageStyle.UNDECORATED;
    }

    public static void main(String[] args) {
        launch();
    }
}