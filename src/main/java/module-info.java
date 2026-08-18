module com.example.pong {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.pong to javafx.fxml;
    exports com.example.pong;
    exports com.example.pong.pong;
    opens com.example.pong.pong to javafx.fxml;
    exports com.example.pong.pong.scene;
    opens com.example.pong.pong.scene to javafx.fxml;
    exports com.example.pong.pong.util;
    exports com.example.pong.pong.ai;
}