package com.github.zusatzprojekt.madn.ui.components.gameboard;

import com.github.zusatzprojekt.madn.enums.MadnPlayerId;
import com.github.zusatzprojekt.madn.enums.MadnFieldFunction;
import com.github.zusatzprojekt.madn.ui.AppManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class MadnFieldV extends Group {
    private final ObjectProperty<Paint> fillProp = new SimpleObjectProperty<>(Color.DODGERBLUE);
    private final BooleanProperty smallFieldProp = new SimpleBooleanProperty(false);
    private MadnFieldFunction fieldType = MadnFieldFunction.NONE;
    private MadnPlayerId fieldAssignment = MadnPlayerId.NONE;
    private boolean cornerField = false;
    private Parent container;

    @FXML
    private Circle circle;
    @FXML
    private StackPane textContainer;
    @FXML
    private Text fieldText;


    // == Constructor ==================================================================================================

    public MadnFieldV() {
        // Load fxml file with ui structure
        AppManager.loadComponentFxml("ui/components/gameboard/madn-field-v.fxml", this, this);

        // Create bindings to/between the UI elements
        initBindings();
    }


    // == Bindings =====================================================================================================

    private void initBindings() {
        circle.radiusProperty().bind(Bindings.when(smallFieldProp).then(30.0).otherwise(35.0));
        circle.layoutXProperty().bind(circle.radiusProperty());
        circle.layoutYProperty().bind(circle.radiusProperty());
        circle.fillProperty().bind(fillProp);

        textContainer.prefWidthProperty().bind(circle.radiusProperty().multiply(2.0));
        textContainer.prefHeightProperty().bind(circle.radiusProperty().multiply(2.0));
    }


    // == Getter / Setter ==============================================================================================

    public Paint getFill() {
        return fillProp.getValue();
    }

    public void setFill(Paint fill) {
        fillProp.setValue(fill);
    }

    public boolean isSmallField() {
        return smallFieldProp.getValue();
    }

    public void setSmallField(boolean b) {
        smallFieldProp.setValue(b);
    }

    public MadnFieldFunction getFieldType() {
        return fieldType;
    }

    public void setFieldType(MadnFieldFunction fieldType) {
        this.fieldType = fieldType;
    }

    public MadnPlayerId getFieldAssignment() {
        return fieldAssignment;
    }

    public void setFieldAssignment(MadnPlayerId fieldAssignment) {
        this.fieldAssignment = fieldAssignment;
    }

    public boolean isCornerField() {
        return cornerField;
    }

    public void setCornerField(boolean isCorner) {
        cornerField = isCorner;
    }

    public void setContainer(Parent parent) {
        this.container = parent;
    }

    public String getText() {
        return fieldText.getText();
    }

    public void setText(String s) {
        fieldText.setText(s);
    }

    public double getCenterAbsoluteX() {
        return container == null ? circle.getRadius() : container.getLayoutX() + getLayoutX() + circle.getRadius();
    }

    public double getCenterAbsoluteY() {
        return container == null ? circle.getRadius() : container.getLayoutY() + getLayoutY() + circle.getRadius();
    }

    public double getRadius() {
        return circle.getRadius();
    }

    // == Getter / Setter properties ===================================================================================

    public ObjectProperty<Paint> fillProperty() {
        return fillProp;
    }

}
