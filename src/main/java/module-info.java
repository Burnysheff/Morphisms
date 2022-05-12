module com.example.morphims {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens com.example.morphims to javafx.fxml;
    exports com.example.morphims;
}