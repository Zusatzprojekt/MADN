package com.github.zusatzprojekt.madn.ui.components.gameboard;

import com.github.zusatzprojekt.madn.ui.UIManager;
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
        UIManager.loadComponentFxml(fxmlFile, this, this);

        fields = fieldContainer.getChildrenUnmodifiable().toArray(MadnFieldV[]::new);
    }


    // == Getter / Setter ==============================================================================================

    public MadnFieldV[] getFields() {
        return fields;
    }

}

