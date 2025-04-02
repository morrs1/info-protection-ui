module org.example.infoprotectionui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires static lombok;


    opens org.example.infoprotectionui to javafx.fxml;
    exports org.example.infoprotectionui;
    exports org.example.infoprotectionui.controllers;
    opens org.example.infoprotectionui.controllers to javafx.fxml;
}