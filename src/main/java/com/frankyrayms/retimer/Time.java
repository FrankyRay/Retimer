package com.frankyrayms.retimer;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class Time {

    private LocalTime time = LocalTime.parse("00:00:00");

    private final Scene scene;
    private final Label hourLabel;
    private final Label minuteLabel;
    private final Label secondLabel;

    public Time() {
        // Hour
        hourLabel = new Label();
        hourLabel.setText("00");
        HBox hourBox = new HBox();
        hourBox.getChildren().add(hourLabel);
        hourBox.setId("time-box");

        // Minute
        minuteLabel = new Label();
        minuteLabel.setText("00");
        HBox minuteBox = new HBox();
        minuteBox.getChildren().add(minuteLabel);
        minuteBox.setId("time-box");

        // Second
        secondLabel = new Label();
        secondLabel.setText("00");
        HBox secondBox = new HBox();
        secondBox.getChildren().add(secondLabel);
        secondBox.setId("time-box");

        // Separator
        Label separatorHM = new Label();
        separatorHM.setText(":");

        Label separatorMS = new Label();
        separatorMS.setText(":");

        // Time Layout
        HBox timeBox = new HBox();
        timeBox.getChildren().setAll(hourBox, separatorHM, minuteBox, separatorMS, secondBox);
        timeBox.setId("main-box");

        // Time Scene
        scene = new Scene(timeBox);
        scene.getStylesheets().add(getClass().getResource("/styles/time.css").toExternalForm());
    }

    public void decrementTime() {
        time = time.minusSeconds(1);
        updateTime();
    }

    public void incrementTime() {
        time = time.plusSeconds(1);
        updateTime();
    }

    public void setTime(String timeString) throws DateTimeParseException {
        time = LocalTime.parse(timeString);
        updateTime();
    }

    private void updateTime() {
        hourLabel.setText(String.format("%02d", time.getHour()));
        minuteLabel.setText(String.format("%02d", time.getMinute()));
        secondLabel.setText(String.format("%02d", time.getSecond()));
    }

    public Scene getScene() {
        return scene;
    }
}
