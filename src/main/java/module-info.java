module java {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.sql;
    opens model;
    opens controllers;
    opens view;
}