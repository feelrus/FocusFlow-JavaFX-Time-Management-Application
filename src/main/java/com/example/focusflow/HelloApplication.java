//HelloApplication.java
package com.example.focusflow;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader =
                new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 340, 180);

        // Apply dark theme
        scene.getStylesheets().add(
                HelloApplication.class.getResource("dark-theme.css").toExternalForm()
        );

        stage.setTitle("FocusFlow");
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);

        // Centered Exit Confirmation Dialog
        stage.setOnCloseRequest((WindowEvent event) -> {
            event.consume();

            ButtonType okButton =
                    new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton =
                    new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.initOwner(stage);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Exit");

            // -------- Centered Message --------
            Label message = new Label(
                    "Are you sure you want to exit?\n" +
                            "Your timer progress will be lost if you exit."
            );

            message.setWrapText(true);
            message.setMaxWidth(Double.MAX_VALUE);
            message.setAlignment(Pos.CENTER);
            message.setStyle(
                    "-fx-text-alignment: center; " +
                            "-fx-font-size: 14px;"
            );

            alert.getButtonTypes().addAll(okButton, cancelButton);

            // -------- Get Real Buttons --------
            Button okBtn =
                    (Button) alert.getDialogPane().lookupButton(okButton);
            Button cancelBtn =
                    (Button) alert.getDialogPane().lookupButton(cancelButton);

            okBtn.setMinWidth(70);
            cancelBtn.setMinWidth(70);

            // -------- Center Buttons --------
            HBox buttonBox = new HBox(15, okBtn, cancelBtn);
            buttonBox.setAlignment(Pos.CENTER);

            // -------- Main Layout --------
            VBox content = new VBox(20, message, buttonBox);
            content.setAlignment(Pos.CENTER);

            alert.getDialogPane().setContent(content);
            alert.getDialogPane().setPrefSize(360, 150);
            alert.getDialogPane().setMinSize(
                    Region.USE_PREF_SIZE,
                    Region.USE_PREF_SIZE
            );

            alert.showAndWait().ifPresent(result -> {
                if (result == okButton) {
                    Platform.exit();
                }
            });
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}