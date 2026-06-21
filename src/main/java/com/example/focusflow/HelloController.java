//HelloController.java
package com.example.focusflow;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;

public class HelloController {

    @FXML private Label timerLabel;
    @FXML private Label cycleCounterLabel;
    @FXML private Label totalMinutesLabel;
    @FXML private Label totalHoursLabel;
    @FXML private ToggleButton muteToggle;
    @FXML private javafx.scene.control.TextField minutesField;
    @FXML private javafx.scene.control.TextField secondsField;

    // init timer
    private int totalSeconds = 5 * 60;
    private int remainingSeconds = totalSeconds;

    // total duration
    private int timerCompletionCount = 0;
    private int totalMinutes = 0;
    private int totalHours = 0;

    private Timeline timeline;
    private Timeline blinkTimeline;

    // play alarm
    private AudioClip alarmSound;

    @FXML
    public void initialize() {
        totalSeconds = 5 * 60;
        remainingSeconds = totalSeconds;

        updateLabel();
        updateCycleMinutesHours();
        loadSound();

        // show default values in input fields
        if (minutesField != null) {
            minutesField.setText("5");
        }
        if (secondsField != null) {
            secondsField.setText("0");
        }
    }

    private void loadSound() {
        try {
            URL resource = getClass().getResource("/sound/alarm.mp3");
            if (resource != null) {
                alarmSound = new AudioClip(resource.toExternalForm());
            } else {
                System.out.println("Alarm sound file not found.");
            }
        } catch (Exception e) {
            System.out.println("Error loading alarm sound: " + e.getMessage());
        }
    }

    @FXML
    private void onStart() {

        int minutes = parseSafe(minutesField.getText());
        int seconds = parseSafe(secondsField.getText());

        // fallback to 5 minutes if user enters nothing
        if (minutes == 0 && seconds == 0) {
            minutes = 5;
        }

        totalSeconds = (minutes * 60) + seconds;
        remainingSeconds = totalSeconds;

        if (timeline == null) {
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> tick()));
            timeline.setCycleCount(Timeline.INDEFINITE);
        }

        timeline.play();
    }

    private int parseSafe(String value) {
        try {
            if (value == null || value.isBlank()) return 0;
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @FXML
    private void onPause() {
        if (timeline != null) timeline.pause();
        stopBlinking();
    }

    @FXML
    private void onReset() {
        if (timeline != null) timeline.stop();

        remainingSeconds = totalSeconds;
        timerCompletionCount = 0;
        totalMinutes = 0;
        totalHours = 0;

        updateLabel();
        updateCycleMinutesHours();
        stopBlinking();
        timerLabel.setTextFill(Color.WHITE);
    }

    private void tick() {
        remainingSeconds--;
        updateLabel();

        if (remainingSeconds <= 3 && remainingSeconds > 0) startBlinking();

        if (remainingSeconds <= 0) {
            remainingSeconds = totalSeconds;

            timerCompletionCount++;
            totalMinutes += totalSeconds / 60;
            totalHours = totalMinutes / 60;

            updateCycleMinutesHours();

            stopBlinking();
            timerLabel.setTextFill(Color.WHITE);

            playAlarmSound();
        }
    }

    private void updateLabel() {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void updateCycleMinutesHours() {
        cycleCounterLabel.setText("(" + timerCompletionCount + "c)");
        totalHoursLabel.setText("(" + totalHours + "h)");
        totalMinutesLabel.setText("(" + totalMinutes + "m)");
    }

    private void startBlinking() {
        stopBlinking();
        blinkTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
            if (timerLabel.getTextFill().equals(Color.RED)) {
                timerLabel.setTextFill(Color.WHITE);
            } else {
                timerLabel.setTextFill(Color.RED);
            }
        }));
        blinkTimeline.setCycleCount(Timeline.INDEFINITE);
        blinkTimeline.play();
    }

    private void stopBlinking() {
        if (blinkTimeline != null) blinkTimeline.stop();
        timerLabel.setTextFill(Color.WHITE);
    }

    private void playAlarmSound() {
        if (muteToggle.isSelected()) return;
        if (alarmSound != null) alarmSound.play();
    }
}