module org.example.infoprotectionui {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires com.fasterxml.jackson.databind;


    opens org.example.infoprotectionui to javafx.fxml;
    exports org.example.infoprotectionui;
    exports org.example.infoprotectionui.controllers;
    opens org.example.infoprotectionui.controllers to javafx.fxml;
    exports org.example.infoprotectionui.entities to com.fasterxml.jackson.databind;
}