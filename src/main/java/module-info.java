
module com.example.demo{
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.media;
    opens com.example.demo to javafx.graphics;
    exports com.example.demo;
}
