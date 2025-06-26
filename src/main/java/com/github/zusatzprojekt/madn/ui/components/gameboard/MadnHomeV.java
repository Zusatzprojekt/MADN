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
                    fields[i].setLayoutX(0.0);
                    fields[i].setLayoutY(85.0 * n);
                    break;
                case DOWN:
                    fields[i].setLayoutX(0.0);
                    fields[i].setLayoutY(85.0 * i);
                    break;
                case LEFT:
                    fields[i].setLayoutX(85.0 * n);
                    fields[i].setLayoutY(0.0);
                    break;
                case RIGHT:
                    fields[i].setLayoutX(85.0 * i);
                    fields[i].setLayoutY(0.0);
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