package com.github.zusatzprojekt.madn.logic;

import com.github.zusatzprojekt.madn.ui.components.GameBoard;
import com.github.zusatzprojekt.madn.ui.controller.GameViewController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;

import java.util.*;

public class Game {
//    private final Player[] players;
    private Player currentPlayer;
    private final Dice dice;
    private final GameBoard gameBoard;
    private final GameViewController gameViewController;
    private final StringProperty currentPlayerString = new SimpleStringProperty();
    private final Button rollButton;
    private boolean startRoll = true;
    int a = 0;

    public Game(GameBoard gameBoard, GameViewController gameViewController) {
        this.gameBoard = gameBoard;
        this.gameViewController = gameViewController;

        currentPlayer = gameBoard.getPlayers()[0];
        rollButton = gameViewController.getRollButton();
        dice = new Dice();

        setup();
        startGame();
    }

    private void setup() {
        //TODO: Implementation
        gameViewController.getCurrentPlayerLabel().textProperty().bind(currentPlayerString);
        rollButton.setOnAction(event -> rollDice());
    }

    private void startGame(){
        // TODO: Implementierung

        setCurrentPlayerLabel(currentPlayer);
        rollButton.setDisable(false);

    }

    private void setCurrentPlayerLabel(Player player) {
         switch (player.getPlayerID()) {
             case BLUE:
                 currentPlayerString.set("Blau");
                 break;
             case YELLOW:
                 currentPlayerString.set("Gelb");
                 break;
             case GREEN:
                 currentPlayerString.set("Grün");
                 break;
             case RED:
                 currentPlayerString.set("Rot");
                 break;
        };
    }

    private void rollDice() {
//        currentPlayer.setLastRoll(dice.roll());

        if (a < 4) {
            a++;
            currentPlayer.setLastRoll(dice.roll());
            switchPlayer(gameBoard.getPlayers());
            setCurrentPlayerLabel(currentPlayer);
        } else {
            getHighestRoll(gameBoard.getPlayers());
        }
    }

    private void switchPlayer(Player[] players) {
        int curIndex = Arrays.asList(players).indexOf(currentPlayer);

        while (players[(curIndex + 1) % players.length].isFinished() && Arrays.stream(players).noneMatch(Player::isFinished)) {
            curIndex = (curIndex + 1) % players.length;
        }

        currentPlayer = players[(curIndex + 1) % players.length];
    }

    private Player[] getHighestRoll(Player[] players) {
//        Player[] sortedPlayers = Arrays.stream(players).sorted(Comparator.comparingInt(Player::getLastRoll)).toArray(Player[]::new);

        Player highestRoll = Arrays.stream(players).max(Comparator.comparingInt(Player::getLastRoll)).orElse(null);

        assert highestRoll != null;
        System.out.println("Höchste Zahl: " + highestRoll.getPlayerID());

        for (int i = 0; i < players.length; i++) {
            System.out.println("Spieler: " + players[i].getPlayerID() + ", Würfelzahl: " + players[i].getLastRoll());
        }

        return new Player[0];
    }
}
