package com.frankyrayms.retimer.controls;

import com.frankyrayms.retimer.Retimer;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.format.DateTimeParseException;

public class Timer {

    private final Retimer mainApp;
    private final Time timeDisplay;
    private final Scene scene;

    private final Button runButton;
    private final Button stopButton;
    private final Button resetButton;
    private final Button lockButton;

    private Timeline timeline;

    private final TextField quickTimeField;

    public Timer(Time timeDisplay, Retimer mainApp) {
        this.timeDisplay = timeDisplay;
        this.mainApp = mainApp;

        // Quick Timer
        Label quickLabel = new Label();
        quickLabel.setText("Quick Timer");

        Label quickDesc = new Label();
        quickDesc.setText("Set a quick timer!");

        quickTimeField = new TextField();
        quickTimeField.setText("00:00:00");
        quickTimeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{2}\\s*:\\s*\\d{2}\\s*:\\s*\\d{2}")) {
                quickDesc.setText("Invalid time format! Must be in HH:MM:SS format!");
                quickDesc.setStyle("-fx-text-fill: red;");
            } else {
                quickDesc.setText("Set a quick timer!");
                quickDesc.setStyle("-fx-text-fill: black;");
            }
        });

        VBox quickBox = new VBox();
        quickBox.getChildren().addAll(quickLabel, quickTimeField, quickDesc);


        // Timer control
        runButton = new Button();
        runButton.setText("Start");
        runButton.setOnAction(event -> runTimer());

        stopButton = new Button();
        stopButton.setText("Stop");
        stopButton.setOnAction(event -> stopTimer());

        resetButton = new Button();
        resetButton.setText("Restart");

        lockButton = new Button();
        lockButton.setText("Lock");
        lockButton.setOnAction(event -> lockTimer());

        HBox controlBox = new HBox();
        controlBox.getChildren().setAll(runButton, stopButton, lockButton);


        // Main config
        VBox configBox = new VBox();
        configBox.getChildren().setAll(quickBox);


        BorderPane timerPane = new BorderPane();
        timerPane.setCenter(configBox);
        timerPane.setBottom(controlBox);

        scene = new Scene(timerPane);
        scene.getStylesheets().add(getClass().getResource("/timer.css").toExternalForm());
    }

    public Scene getScene() {
        return scene;
    }

    private String parseTimerField() {
        String time = quickTimeField.getText();
        time = time.replaceAll("\\s", "");

        String[] timeSplit = time.split(":");
        if (timeSplit.length == 1) {
            time = String.format("00:00:%2s", timeSplit[0]);
        } else if (timeSplit.length == 2) {
            time = String.format("00:%2s:%2s", timeSplit[0], timeSplit[1]);
        } else if (timeSplit.length == 3) {
            time = String.format("%2s:%2s:%2s", timeSplit[0], timeSplit[1], timeSplit[2]);
        }

        return time.replaceAll(" ", "0");
    }

    private void runTimer() {
        if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
            pauseTimer();
            return;
        }

        try {
            if (timeline == null) {
                String time = parseTimerField();
                quickTimeField.setText(time);
                timeDisplay.setTime(time);

                timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
                timeline.setCycleCount(Animation.INDEFINITE);
            }

            timeline.play();
            runButton.setText("Pause");
        } catch (DateTimeParseException err) {
            err.printStackTrace();
        }
    }

    private void pauseTimer() {
        timeline.pause();
        runButton.setText("Start");
    }

    private void updateTimer() {
        timeDisplay.decrementTime();
    }

    private void stopTimer() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;

            timeDisplay.setTime("00:00:00");
            runButton.setText("Start");
        }
    }

    private void lockTimer() {
        boolean isLock = mainApp.lockTimeStage();
        lockButton.setText(isLock ? "Unlock" : "Lock");
    }
}
