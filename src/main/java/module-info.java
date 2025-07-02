module com.github.zusatzprojekt.madn {
    requires javafx.controls;
    requires javafx.fxml;


    exports com.github.zusatzprojekt.madn;
    opens com.github.zusatzprojekt.madn to javafx.fxml;
    opens com.github.zusatzprojekt.madn.ui.components to javafx.fxml;
    opens com.github.zusatzprojekt.madn.ui.controller to javafx.fxml;
    opens com.github.zusatzprojekt.madn.ui.components.dice to javafx.fxml;
    opens com.github.zusatzprojekt.madn.ui.components.gameboard to javafx.fxml;
    opens com.github.zusatzprojekt.madn.enums to javafx.fxml;
}