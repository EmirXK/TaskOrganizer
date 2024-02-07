module com.example.taskorganizer {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.taskorganizer to javafx.fxml;
    exports com.example.taskorganizer;
}