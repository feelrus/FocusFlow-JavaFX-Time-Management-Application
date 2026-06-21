module com.example.focusflow {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.focusflow to javafx.fxml;
    exports com.example.focusflow;
}