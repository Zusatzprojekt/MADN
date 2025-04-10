package com.github.zusatzprojekt.madn.ui.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ConfirmationDialogYesNo extends Alert {

    public ConfirmationDialogYesNo(String title, String headerText, String bodyText) {
        super(AlertType.CONFIRMATION, bodyText, ButtonType.YES, ButtonType.NO);
        init(null, title, headerText);
    }

    public ConfirmationDialogYesNo(Window owner, String title, String headerText, String bodyText) {
        super(AlertType.CONFIRMATION, bodyText, ButtonType.YES, ButtonType.NO);
        init(owner, title, headerText);
    }

    private void init(Window owner, String title, String headerText) {

        if (owner != null) {
            initOwner(owner);
            ((Stage) getDialogPane().getScene().getWindow()).getIcons().addAll(((Stage) getOwner()).getIcons());
        }

        setTitle(title);
        setHeaderText(headerText);
    }

    public void setDefaultButton(ButtonType defaultButton) {
        this.getButtonTypes().forEach(buttonType -> {
            Button btn = (Button) this.getDialogPane().lookupButton(buttonType);
            btn.setDefaultButton(defaultButton.equals(buttonType));
        });
    }

    public void onYesButtonPressed(EventHandler<ActionEvent> event) {
        ((Button) this.getDialogPane().lookupButton(ButtonType.YES)).setOnAction(event);
    }

    public void onNoButtonPressed(EventHandler<ActionEvent> event) {
        ((Button) this.getDialogPane().lookupButton(ButtonType.NO)).setOnAction(event);
    }

}
