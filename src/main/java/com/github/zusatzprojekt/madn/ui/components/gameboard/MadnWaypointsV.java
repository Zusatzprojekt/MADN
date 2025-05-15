package com.github.zusatzprojekt.madn.ui.components.gameboard;

import com.github.zusatzprojekt.madn.ui.UILoader;

public class MadnWaypointsV extends MadnFieldContainerV {

    // == Constructor ==================================================================================================

    @SuppressWarnings("SuspiciousToArrayCall")
    public MadnWaypointsV() {
        UILoader.loadComponentFxml("ui/components/gameboard/madn-waypoints-v.fxml", this, this);

        setFields(this.getChildren().toArray(MadnFieldV[]::new));
    }

}
