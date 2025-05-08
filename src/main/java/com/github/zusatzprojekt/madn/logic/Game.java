package com.github.zusatzprojekt.madn.logic;

import com.github.zusatzprojekt.madn.ui.components.MadnGameBoard;
import com.github.zusatzprojekt.madn.ui.controller.GameViewController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;

import java.util.*;

public class Game {
//    private final Player[] gameBoard;
    private Player currentPlayer;
    private final Dice dice;
    private final MadnGameBoard gameBoard;
    private final GameViewController gameViewController;
    private final StringProperty currentPlayerString = new SimpleStringProperty();
    private final Button rollButton;
    private boolean startRoll = true;
//    int a = 0;

    public Game(MadnGameBoard gameBoard, GameViewController gameViewController) {
        this.gameBoard = gameBoard;
        this.gameViewController = gameViewController;

        currentPlayer = gameBoard.getPlayers()[0];
        rollButton = gameViewController.getRollButton();
        dice = new Dice();

        setup();
        startGame();

        gameViewController.getRollButton().setDisable(false);
    }

    private void setup() {
        //TODO: Implementation
        gameViewController.getCurrentPlayerLabel().textProperty().bind(currentPlayerString);
        rollButton.setOnAction(event -> rollDice(gameBoard.getPlayers()));
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

    private void rollDice(Player[] players) {
//        currentPlayer.setLastRoll(dice.roll());
//
//        if (a < players.length) {
//            a++;
        if (startRoll) {
            currentPlayer.setLastRoll(dice.roll());
//        } else {
//            getHighestRoll(players);
//        }
            if (/*a >= players.length*/ currentPlayer.equals(players[players.length - 1])) {
                getHighestRoll(players);
            } else {
                switchPlayer(players);
                setCurrentPlayerLabel(currentPlayer);
            }
        }
    }

    private void switchPlayer(Player[] players) {
        int pLength = players.length;
        int curIndex = Arrays.asList(players).indexOf(currentPlayer);

        while (players[(curIndex + 1) % pLength].isFinished() && Arrays.stream(players).noneMatch(Player::isFinished)) {
            curIndex = (curIndex + 1) % pLength;
        }

        currentPlayer = players[(curIndex + 1) % pLength];
    }

    private void getHighestRoll(Player[] players) {
        int maxRoll = Arrays.stream(players).max(Comparator.comparingInt(Player::getLastRoll)).orElseThrow().getLastRoll();
        Player[] highestRolls = Arrays.stream(players).filter(player -> player.getLastRoll() == maxRoll).toArray(Player[]::new);

        currentPlayer = highestRolls[0];
        setCurrentPlayerLabel(currentPlayer);

        for (Player roll : highestRolls) {
            System.out.println("Spieler: " + roll.getPlayerID() + ", Wurf: " + roll.getLastRoll());
        }
        System.out.println();

        if (highestRolls.length > 1) {
            rollButton.setOnAction(event -> rollDice(highestRolls));
//            a = 0;
        } else {
            startRoll = false;
            rollButton.setDisable(true);
            // TODO: next method
        }
    }
}
