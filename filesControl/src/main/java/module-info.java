module com.example.filescontrol {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires org.apache.poi.scratchpad;
    requires org.apache.logging.slf4j;
    requires org.apache.poi.ooxml;
    requires javafx.swing;
    requires java.sql;

    opens com.example.filescontrol to javafx.fxml;
    exports com.example.filescontrol;
    exports com.example.filescontrol.Watcher;
    opens com.example.filescontrol.Watcher to javafx.fxml;
}