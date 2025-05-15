package com.github.zusatzprojekt.madn.ui.components;

import com.github.zusatzprojekt.madn.interfaces.FxmlValueReceiver;
import com.github.zusatzprojekt.madn.logic.MadnPlayerL;
import com.github.zusatzprojekt.madn.ui.UILoader;
import com.github.zusatzprojekt.madn.ui.components.gameboard.MadnFieldContainerV;
import com.github.zusatzprojekt.madn.ui.components.gameboard.MadnFieldV;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.Map;

public class MadnBoardV extends AnchorPane implements FxmlValueReceiver {
    //TODO: Implement
    private final ListProperty<MadnPlayerL> playerList = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<MadnPlayerL> currentPlayer = new SimpleObjectProperty<>();

    @FXML
    private MadnFieldContainerV baseContainerBlue,
            baseContainerYellow,
            baseContainerGreen,
            baseContainerRed,
            waypointContainer,
            homeContainerBlue,
            homeContainerYellow,
            homeContainerGreen,
            homeContainerRed;

    @FXML
    private Pane figureContainer;

    public MadnBoardV() {
        UILoader.loadComponentFxml("ui/components/madn-board-v.fxml", this, this);

        playerList.addListener((ListChangeListener<? super MadnPlayerL>) changed -> {
            System.out.println("== ListChangeListener ========================================================");
            System.out.println(changed);
            System.out.println("------------------------------------------------------------------------------");
        });

        playerList.addListener((ListChangeListener<? super MadnPlayerL>) change -> {

            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(this::setupPlayer);
                }
            }
        });
    }

    private void setupPlayer(MadnPlayerL player) {
        System.out.println(player);

        switch (player.getPlayerID()) {
            case BLUE:
                setupFigures(Color.web("#3387F5"), baseContainerBlue);
                break;

            case YELLOW:
                setupFigures(Color.web("#FFFF00"), baseContainerYellow);
                break;

            case GREEN:
                setupFigures(Color.web("#009A00"), baseContainerGreen);
                break;

            case RED:
                setupFigures(Color.web("#FF3030"), baseContainerRed);
                break;
        }

    }

    private void setupFigures(Color color, MadnFieldContainerV fieldContainer) {

        for (MadnFieldV field : fieldContainer.getFields()) {
            MadnFigureV figure = new MadnFigureV(20, color);
            figure.setLayoutX(fieldContainer.getLayoutX() + field.getCenterX() - figure.getRadius());
            figure.setLayoutY(fieldContainer.getLayoutY() + field.getCenterY() - figure.getRadius());
            figure.setRingColor(Color.web("#ADD8E6"));
            figureContainer.getChildren().add(figure);
        }
    }

    @Override
    public void receiveValues(Map<String, Object> values) {
        setupBoard(values);
    }

    private void setupBoard(Map<String, Object> values) {
        boolean playerBlue = (boolean) values.get("playerBlue");
        boolean playerYellow = (boolean) values.get("playerYellow");
        boolean playerGreen = (boolean) values.get("playerGreen");
        boolean playerRed = (boolean) values.get("playerRed");
        int playerCount = (int) values.get("playerCount");

        if (playerBlue) {
            playerList.add(new MadnPlayerL(MadnPlayerL.PlayerID.BLUE));
        }

        if (playerYellow) {
            playerList.add(new MadnPlayerL(MadnPlayerL.PlayerID.YELLOW));
        }

        if (playerGreen) {
            playerList.add(new MadnPlayerL(MadnPlayerL.PlayerID.GREEN));
        }

        if (playerRed) {
            playerList.add(new MadnPlayerL(MadnPlayerL.PlayerID.RED));
        }
    }


    public ListProperty<MadnPlayerL> playerListProperty() {
        return playerList;
    }

    public ObjectProperty<MadnPlayerL> currentPlayerProperty() {
        return currentPlayer;
    }

}
