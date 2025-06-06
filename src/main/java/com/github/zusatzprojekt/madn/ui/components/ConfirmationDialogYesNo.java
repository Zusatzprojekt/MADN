package com.github.zusatzprojekt.madn.ui.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Eine erweiterte Alert-Dialogklasse, die speziell für Ja/Nein-Bestätigungsdialoge gedacht ist.
 * Sie bietet Komfortmethoden zur Initialisierung, zur Einstellung des Standard-Buttons
 * und zur Registrierung von Event-Handlern.
 */
public class ConfirmationDialogYesNo extends Alert {


    // == Constructor ==================================================================================================

    /**
     * Konstruktor für einen Bestätigungsdialog ohne Besitzerfenster.
     *
     * @param title       Titel des Dialogfensters
     * @param headerText  Überschrift des Dialogs
     * @param bodyText    Hauptinhalt / Nachricht im Dialog
     */
    public ConfirmationDialogYesNo(String title, String headerText, String bodyText) {
        super(AlertType.CONFIRMATION, bodyText, ButtonType.YES, ButtonType.NO);
        init(null, title, headerText);
    }

    /**
     * Konstruktor für einen Bestätigungsdialog mit Angabe eines Besitzerfensters.
     *
     * @param owner       Besitzerfenster des Dialogs
     * @param title       Titel des Dialogfensters
     * @param headerText  Überschrift des Dialogs
     * @param bodyText    Hauptinhalt / Nachricht im Dialog
     */
    public ConfirmationDialogYesNo(Window owner, String title, String headerText, String bodyText) {
        super(AlertType.CONFIRMATION, bodyText, ButtonType.YES, ButtonType.NO);
        init(owner, title, headerText);
    }


    // == Initialization ===============================================================================================

    /**
     * Gemeinsame Initialisierungsmethode für beide Konstruktoren.
     * Setzt Titel, Überschrift und bei Bedarf Besitzer und Icon.
     *
     * @param owner         Besitzerfenster des Dialogs
     * @param title         Titel des Dialogfensters
     * @param headerText    Überschrift des Dialogs
     */
    private void init(Window owner, String title, String headerText) {

        if (owner != null) {
            initOwner(owner);
            ((Stage) getDialogPane().getScene().getWindow()).getIcons().addAll(((Stage) getOwner()).getIcons());
        }

        setTitle(title);
        setHeaderText(headerText);
    }


    // == Custom getter / setter =======================================================================================

    /**
     * Legt fest, welcher der beiden Buttons (JA / NEIN) standardmäßig gesetzt sein soll.
     *
     * @param defaultButton Der ButtonType, der als Default gesetzt werden soll
     */
    public void setDefaultButton(ButtonType defaultButton) {
        this.getButtonTypes().forEach(buttonType -> {
            Button btn = (Button) getDialogPane().lookupButton(buttonType);
            btn.setDefaultButton(defaultButton.equals(buttonType));
        });
    }


    // == Event handlers ===============================================================================================

    /**
     * Setzt einen Event-Handler für den JA-Button.
     *
     * @param event Der auszuführende Event-Handler beim Klicken auf JA
     */
    public void onYesButtonPressed(EventHandler<ActionEvent> event) {
        ((Button) getDialogPane().lookupButton(ButtonType.YES)).setOnAction(event);
    }

    /**
     * Setzt einen Event-Handler für den NEIN-Button.
     *
     * @param event Der auszuführende Event-Handler beim Klicken auf NEIN
     */
    public void onNoButtonPressed(EventHandler<ActionEvent> event) {
        ((Button) getDialogPane().lookupButton(ButtonType.NO)).setOnAction(event);
    }

}
