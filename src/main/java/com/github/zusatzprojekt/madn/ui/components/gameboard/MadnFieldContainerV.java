package com.github.zusatzprojekt.madn.ui.components.gameboard;

import com.github.zusatzprojekt.madn.ui.AppManager;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;

@SuppressWarnings("SuspiciousToArrayCall")
public abstract class MadnFieldContainerV extends Group {
    private final MadnFieldV[] fields;

    @FXML
    private Parent fieldContainer;


    // == Constructor ==================================================================================================

    public MadnFieldContainerV(String fxmlFile) {
        AppManager.loadComponentFxml(fxmlFile, this, this);

        fields = initFields();
    }


    // == Helper methods ===============================================================================================

    private MadnFieldV[] initFields() {
        MadnFieldV[] madnFields = fieldContainer.getChildrenUnmodifiable().toArray(MadnFieldV[]::new);

        for (MadnFieldV field : madnFields) {
            field.setContainer(this);
        }

        return madnFields;
    }


    // == Getter / Setter ==============================================================================================

    public MadnFieldV[] getFields() {
        return fields;
    }

}

