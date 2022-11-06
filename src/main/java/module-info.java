module com.example.groupfive {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;


    opens com.example.groupfive to javafx.fxml;
    exports com.example.groupfive;
}