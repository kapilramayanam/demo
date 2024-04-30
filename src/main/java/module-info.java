module com.example.demo{
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.media;
    requires java.desktop;
    opens com.example.demo to javafx.graphics;
    exports com.example.demo;
}