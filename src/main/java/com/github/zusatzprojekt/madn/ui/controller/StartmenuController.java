package com.github.zusatzprojekt.madn.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

public class StartmenuController {
    @FXML
    public Button playButton;
    private int countPlayer;
    public boolean playerBlue;
    public boolean playerYellow;
    public boolean playerGreen;
    public boolean playerRed;

    public void blueCheckBox(ActionEvent actionEvent) {
        CheckBox blue = (CheckBox) actionEvent.getSource();
        if (blue.isSelected()){
            playerBlue = true;
            countPlayer++;
        } else {
            playerBlue = false;
            countPlayer--;
        }
        changeButtonState();
    }

    public void yellowCheckBox(ActionEvent actionEvent) {
        CheckBox yellow = (CheckBox) actionEvent.getSource();
        if (yellow.isSelected()){
            playerYellow = true;
            countPlayer++;
        } else {
            playerYellow = false;
            countPlayer--;
        }
        changeButtonState();
    }

    public void greenCheckBox(ActionEvent actionEvent) {
        CheckBox green = (CheckBox) actionEvent.getSource();
        if (green.isSelected()){
            playerGreen = true;
            countPlayer++;
        } else {
            playerGreen = false;
            countPlayer--;
        }
        changeButtonState();
    }

    public void redCheckBox(ActionEvent actionEvent) {
        CheckBox red = (CheckBox) actionEvent.getSource();
        if (red.isSelected()){
            playerRed = true;
            countPlayer++;
        } else {
            playerRed = false;
            countPlayer--;
        }
        changeButtonState();
    }

    public void changeButtonState(){
        playButton.setDisable(countPlayer < 2);
    }
}
