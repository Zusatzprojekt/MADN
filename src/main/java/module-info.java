module com.github.zusatzprojekt.madn {
    requires javafx.controls;
    requires javafx.fxml;


    exports com.github.zusatzprojekt.madn;
    opens com.github.zusatzprojekt.madn to javafx.fxml;
    opens com.github.zusatzprojekt.madn.ui to javafx.fxml;
    opens com.github.zusatzprojekt.madn.ui.parts to javafx.fxml;
    opens com.github.zusatzprojekt.madn.ui.controller to javafx.fxml;
}