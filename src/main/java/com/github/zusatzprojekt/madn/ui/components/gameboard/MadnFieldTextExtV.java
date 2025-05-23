package com.github.zusatzprojekt.madn.ui.components.gameboard;

import com.github.zusatzprojekt.madn.enums.MadnPlayerId;
import com.github.zusatzprojekt.madn.enums.MadnSpecialFieldType;
import com.github.zusatzprojekt.madn.interfaces.MadnFieldExtended;

public class MadnFieldTextExtV extends MadnFieldTextV implements MadnFieldExtended {
    private MadnSpecialFieldType fieldType = MadnSpecialFieldType.START;
    private MadnPlayerId fieldAssignment = MadnPlayerId.BLUE;


    // == Constructor ==================================================================================================

    public MadnFieldTextExtV() {}


    // == Getter / Setter ==============================================================================================

    @Override
    public MadnSpecialFieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(MadnSpecialFieldType fieldType) {
        this.fieldType = fieldType;
    }

    @Override
    public MadnPlayerId getFieldAssignment() {
        return fieldAssignment;
    }

    public void setFieldAssignment(MadnPlayerId fieldAssignment) {
        this.fieldAssignment = fieldAssignment;
    }

}
