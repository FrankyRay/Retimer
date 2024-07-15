module com.frankyrayms.retimer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.frankyrayms.retimer to javafx.fxml;
    exports com.frankyrayms.retimer;
}