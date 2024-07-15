package com.frankyrayms.retimer.controls;

import com.frankyrayms.retimer.Time;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Timer {

    private final Time timeDisplay;
    private final Scene scene;

    private Timeline timeline;

    private final TextField quickTimeField;

    public Timer(Time timeDisplay) {
        this.timeDisplay = timeDisplay;

        // Quick Timer
        Label quickLabel = new Label();
        quickLabel.setText("Quick Timer");

        Label quickDesc = new Label();
        quickDesc.setText("Set a quick timer!");

        quickTimeField = new TextField();
        quickTimeField.setText("00:00:00");
        quickTimeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d{1,2}:)?\\d{1,2}:\\d{1,2}")) {
                quickDesc.setText("Invalid time format! Must be in HH:MM:SS or MM:SS format!");
                quickDesc.setStyle("-fx-text-fill: red;");
            } else {
                quickDesc.setText("Set a quick timer!");
                quickDesc.setStyle("-fx-text-fill: black;");
            }
        });

        VBox quickBox = new VBox();
        quickBox.getChildren().addAll(quickLabel, quickTimeField, quickDesc);


        // Timer control
        Button timerStart = new Button();
        timerStart.setText("Start");
        timerStart.setOnAction(event -> {
            if (timeline == null || timeline.getStatus() == Animation.Status.PAUSED) {
                timerStart.setText("Pause");
                startTimer();
            } else {
                timerStart.setText("Start");
                pauseTimer();
            }
        });

        Button timerStop = new Button();
        timerStop.setText("Stop");
        timerStop.setOnAction(event -> stopTimer());

        Button timerRestart = new Button();
        timerRestart.setText("Restart");

        HBox controlBox = new HBox();
        controlBox.getChildren().setAll(timerStart, timerRestart, timerStop);


        // Main config
        VBox configBox = new VBox();
        configBox.getChildren().setAll(quickBox);


        BorderPane timerPane = new BorderPane();
        timerPane.setCenter(configBox);
        timerPane.setBottom(controlBox);

        scene = new Scene(timerPane);
    }

    public Scene getScene() {
        return scene;
    }

    private void startTimer() {
        if (timeline == null) {
            String time = quickTimeField.getText();
            timeDisplay.setTime(time);

            timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
            timeline.setCycleCount(Animation.INDEFINITE);
        }

        timeline.play();
    }

    private void pauseTimer() {
        timeline.pause();
    }

    private void updateTimer() {
        timeDisplay.decrementTime();
    }

    private void stopTimer() {
        timeline.stop();
        timeline = null;
        timeDisplay.setTime("00:00:00");


    }
}
