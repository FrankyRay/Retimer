package com.frankyrayms.retimer.controls;

import com.frankyrayms.retimer.Retimer;
import com.frankyrayms.retimer.Time;
import com.frankyrayms.retimer.data.TimerPreset;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private final Label quickDesc;

    public Timer(Time timeDisplay, Retimer mainApp) {
        this.timeDisplay = timeDisplay;
        this.mainApp = mainApp;

        // Quick Timer
        Label quickLabel = new Label();
        quickLabel.setText("Quick Timer");
        quickLabel.setId("header-1");

        quickDesc = new Label();
        quickDesc.setText("Set a timer!");
        quickDesc.setId("description");

        quickTimeField = new TextField();
        quickTimeField.setText("00:00:00");
        quickTimeField.setId("timer-in");
        quickTimeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("((\\d{1,2}\\s*:\\s*)?\\d{1,2}\\s*:\\s*)?\\d{1,2}")) {
                quickDesc.setText("Invalid time format! Must be in HH:MM:SS format!");
                quickDesc.setStyle("-fx-text-fill: orange;");
            } else {
                quickDesc.setText("Set a timer!");
                quickDesc.setStyle("-fx-text-fill: white;");
            }
        });

        VBox quickBox = new VBox();
        quickBox.getChildren().addAll(quickLabel, quickTimeField, quickDesc);
        quickBox.setId("control-timer");


        // Preset list
        Label timerLabel = new Label();
        timerLabel.setText("Timer Preset");
        timerLabel.setId("header-1");

        ListView<VBox> timerList = new ListView<>();
        timerList.setId("timer-list");
        timerList.setPrefHeight(200);
        timerList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                System.out.println(timerList.getSelectionModel().getSelectedItem().getChildren().getFirst());
            }
        });

        // TEST: Adding preset
        timerList.getItems().addAll(
                createTimerPreset("Khotbah", "00:30:00"),
                createTimerPreset("Perjamuan Kudus", "00:10:00")
        );

        VBox timerTableBox = new VBox();
        timerTableBox.getChildren().addAll(timerLabel, timerList);
        timerTableBox.setId("control-timer");


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
        lockButton.setTooltip(new Tooltip("Lock time window"));

        HBox controlBox = new HBox();
        controlBox.getChildren().setAll(runButton, stopButton, lockButton);
        controlBox.setId("control-timer");

        VBox timerBox = new VBox();
        timerBox.getChildren().setAll(quickBox, timerTableBox, controlBox);
        timerBox.setId("control-box");

        scene = new Scene(timerBox);
        scene.getStylesheets().addAll(
                getClass().getResource("/styles/control.css").toExternalForm(),
                getClass().getResource("/styles/timer.css").toExternalForm()
        );
    }

    public Scene getScene() {
        return scene;
    }

    private VBox createTimerPreset(String name, String time) {
        Label timerName = new Label(name);
        Label timerTime = new Label(time);

        VBox timerPreset = new VBox();
        timerPreset.getChildren().setAll(timerName, timerTime);
        timerPreset.setPrefHeight(40);
        return timerPreset;
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
                timeDisplay.setTime(time);
                quickTimeField.setText(time);

                timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
                timeline.setCycleCount(Animation.INDEFINITE);
            }

            timeline.play();
            runButton.setText("Pause");
        } catch (DateTimeParseException err) {
            err.printStackTrace();
            quickDesc.setText("Failed to parse! Consider check if the format matches HH:MM:DD.");
            quickDesc.setStyle("-fx-text-fill: red;");
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
