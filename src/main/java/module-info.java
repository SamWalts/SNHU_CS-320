module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    opens org.example to javafx.fxml;
    exports org.example;
    exports org.example.models;
    exports org.example.controllers;
    exports org.example.data;
    opens org.example.controllers to javafx.fxml;
}