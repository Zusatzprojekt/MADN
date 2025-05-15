package com.github.zusatzprojekt.madn.ui.components.gameboard;

import com.github.zusatzprojekt.madn.ui.UILoader;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MadnFieldTextV extends MadnFieldV {
    private final DoubleProperty radius = radiusProperty();
    private final StringProperty text = new SimpleStringProperty("");
    private final ObjectProperty<Font> font = new SimpleObjectProperty<>(new Font("Arial Bold", 142.85));

    @FXML
    private StackPane textContainer;
    @FXML
    private Text fieldText;


    // == Constructor ==================================================================================================

    public MadnFieldTextV() {
        // Load fxml file with ui structure
        UILoader.loadComponentFxml("ui/components/gameboard/madn-field-text-v.fxml", this, this);

        // Create bindings to/between the UI elements
        createBindings();
    }


    // == Bindings =====================================================================================================

    private void createBindings() {
        fieldText.textProperty().bind(text);
        fieldText.fontProperty().bind(font);
        textContainer.prefWidthProperty().bind(radius.multiply(2.0));
        textContainer.prefHeightProperty().bind(radius.multiply(2.0));

        radius.addListener((observableValue, oldValue, value) -> {
            font.setValue(new Font("Arial Bold", value.doubleValue() / 0.7));
        });
    }


    // == Getter / Setter ==============================================================================================

    public String getText() {
        return text.getValue();
    }

    public void setText(String value) {
        text.setValue(value);
    }


    // == Getter / Setter properties ===================================================================================

    public StringProperty textProperty() {
        return text;
    }

}
