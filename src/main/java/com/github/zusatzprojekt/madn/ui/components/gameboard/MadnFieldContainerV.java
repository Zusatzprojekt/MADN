package com.github.zusatzprojekt.madn.ui.components.gameboard;

import javafx.scene.Group;

public abstract class MadnFieldContainerV extends Group {
    private MadnFieldV[] fields;

    public MadnFieldContainerV() {}

    public MadnFieldV[] getFields() {
        return fields;
    }

    protected void setFields(MadnFieldV[] fieldList) {
        if (fields == null) {
            fields = fieldList;
        } else {
            throw new RuntimeException(new IllegalAccessException("'fields' already initialized; cannot be changed"));
        }
    }
}
