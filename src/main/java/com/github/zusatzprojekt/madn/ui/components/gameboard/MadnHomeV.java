package com.github.zusatzprojekt.madn.ui.components.gameboard;

import com.github.zusatzprojekt.madn.enums.MadnHomeDirection;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class MadnHomeV extends MadnFieldContainerExtV {
    private final ObjectProperty<MadnHomeDirection> direction = new SimpleObjectProperty<>(MadnHomeDirection.DOWN);


    // == Constructor ==================================================================================================

    public MadnHomeV() {
        super("ui/components/gameboard/madn-home-v.fxml");

        createBindings();
    }


    // == Bindings =====================================================================================================

    private void createBindings() {

        // Initial home field direction setup
        setupHomeDirection(direction.getValue());

        // Reorganize fields when home direction is changed
        direction.addListener((observableValue, oldDirection, newDirection) -> {
            setupHomeDirection(newDirection);
        });

    }


    // == Helper methods ===============================================================================================

    private void setupHomeDirection(MadnHomeDirection direction) {
        MadnFieldV[] fields = getFields();

        for (int i = 0; i < fields.length; i++) {
            int n = fields.length - i - 1;

            switch (direction) {
                case UP:
                    fields[i].layoutXProperty().unbind();
                    fields[i].layoutXProperty().setValue(0.0);
                    fields[i].layoutYProperty().bind(radius.multiply(2.0 * n).add(spacing.multiply(n)));
                    break;
                case DOWN:
                    fields[i].layoutXProperty().unbind();
                    fields[i].layoutXProperty().setValue(0.0);
                    fields[i].layoutYProperty().bind(radius.multiply(2.0 * i).add(spacing.multiply(i)));
                    break;
                case LEFT:
                    fields[i].layoutXProperty().bind(radius.multiply(2.0 * n).add(spacing.multiply(n)));
                    fields[i].layoutYProperty().unbind();
                    fields[i].layoutYProperty().setValue(0.0);
                    break;
                case RIGHT:
                    fields[i].layoutXProperty().bind(radius.multiply(2.0 * i).add(spacing.multiply(i)));
                    fields[i].layoutYProperty().unbind();
                    fields[i].layoutYProperty().setValue(0.0);
                    break;
            }
        }
    }


    // == Getter / Setter ==============================================================================================

    public MadnHomeDirection getHomeDirection() {
        return direction.getValue();
    }

    public void setHomeDirection(MadnHomeDirection direction) {
        this.direction.setValue(direction);
    }

}