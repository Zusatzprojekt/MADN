package com.github.zusatzprojekt.madn.logic;

import com.github.zusatzprojekt.madn.ui.components.MadnDiceV;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

public class MadnGameL {
    private final ObjectProperty<MadnPlayerL> currentPlayer = new SimpleObjectProperty<>();
    private final MadnPlayerL[] playerList;
    private final MadnDiceL dice = new MadnDiceL();

    public MadnGameL(MadnPlayerL[] playerList, MadnDiceV vDice) {
        this.playerList = playerList;

        currentPlayer.setValue(playerList[0]);

        vDice.setOnDiceClicked(event -> {
            vDice.setDisable(true);

            int roll = dice.roll();
            currentPlayer.getValue().setLastRoll(roll);
            vDice.startAnimation(roll);
        });

        vDice.setOnFinished(event -> {rollFinished(); vDice.setDisable(false);});
    }

    private void rollFinished() {
        System.out.println("Spieler " + currentPlayer.getValue().getPlayerID() + " hast eine " + currentPlayer.getValue().getLastRoll() + " gewürfelt!");
    }

    public MadnPlayerL[] getPlayerList() {
        return playerList;
    }

    public MadnPlayerL getCurrentPlayer() {
        return currentPlayer.getValue();
    }

    public ObservableValue<MadnPlayerL> currentPlayerObservable() {
        return currentPlayer;
    }














    //    private final Player[] players;
//    private Player currentPlayer;
//    private final MadnDiceL dice;
//    private final MadnGameBoard gameBoard;
//    private final GameViewController gameViewController;
//    private final StringProperty currentPlayerString = new SimpleStringProperty();
//    private final Button rollButton;
//    private boolean startRoll = true;
//    private int rollCount = 0;

//    public MadnGameL(/*MadnGameBoard gameBoard,*/ GameViewController gameViewController) {
//        this.gameBoard = gameBoard;
//        this.gameViewController = gameViewController;

//        currentPlayer = gameBoard.getPlayers()[0];
//        rollButton = gameViewController.getRollButton();
//        dice = new MadnDiceL();

//        setup();
//        startGame();

//        gameViewController.getRollButton().setDisable(false);
//    }

//    private void setup() {
//        //TODO: Implementation
//        gameViewController.getCurrentPlayerLabel().textProperty().bind(currentPlayerString);
//        rollButton.setOnAction(event -> rollDice(gameBoard.getPlayers()));
//    }
//
//    private void startGame(){
//        // TODO: Implementierung
//
//        setCurrentPlayerLabel(currentPlayer);
//        rollButton.setDisable(false);
//    }
//
//    private void setCurrentPlayerLabel(Player player) {
//         switch (player.getPlayerID()) {
//             case BLUE:
//                 currentPlayerString.set("Blau");
//                 break;
//             case YELLOW:
//                 currentPlayerString.set("Gelb");
//                 break;
//             case GREEN:
//                 currentPlayerString.set("Grün");
//                 break;
//             case RED:
//                 currentPlayerString.set("Rot");
//                 break;
//        };
//    }
//
//    private void rollDice(Player[] players) {
//        if (startRoll) {
//            currentPlayer.setLastRoll(dice.roll());
//            if (currentPlayer.equals(players[players.length - 1])) {
//                getHighestRoll(players);
//            } else {
//                switchPlayer(players);
//                setCurrentPlayerLabel(currentPlayer);
//            }
//        } else {
//            System.out.println("Spieler " + currentPlayer.getPlayerID() + " hat eine " + currentPlayer.getLastRoll() + " gewürfelt und darf ziehen.");
//
//            currentPlayer.setLastRoll(dice.roll());
//            rollButton.setDisable(true);
//            rollCount++;
//            checkState(currentPlayer);
//        }
//    }
//
//    private void switchPlayer(Player[] players) {
//        int pLength = players.length;
//        int curIndex = Arrays.asList(players).indexOf(currentPlayer);
//
//        while (players[(curIndex + 1) % pLength].isFinished() && Arrays.stream(players).noneMatch(Player::isFinished)) {
//            curIndex = (curIndex + 1) % pLength;
//        }
//
//        currentPlayer = players[(curIndex + 1) % pLength];
//    }
//
//    private void getHighestRoll(Player[] players) {
//        int maxRoll = Arrays.stream(players).max(Comparator.comparingInt(Player::getLastRoll)).orElseThrow().getLastRoll();
//        Player[] highestRolls = Arrays.stream(players).filter(player -> player.getLastRoll() == maxRoll).toArray(Player[]::new);
//
//        currentPlayer = highestRolls[0];
//        setCurrentPlayerLabel(currentPlayer);
//
//        for (Player roll : highestRolls) {
//            System.out.println("Spieler: " + roll.getPlayerID() + ", Wurf: " + roll.getLastRoll());
//        }
//        System.out.println();
//
//        if (highestRolls.length > 1) {
//            rollButton.setOnAction(event -> rollDice(highestRolls));
//        } else {
//            startRoll = false;
//            rollButton.setOnAction(event -> rollDice(gameBoard.getPlayers()));
//        }
//    }
//
//    private void checkState(Player player) {
//        Figure[] currentFigures = player.getFigures();
//        boolean onField = Arrays.stream(currentFigures).anyMatch(figure -> figure.getCurrentField() >= 0);
//
//        if (!onField) {
//            if (player.getLastRoll() == 6) {
//                //TODO: Implement
//                System.out.println("Spieler kann raus");
//                rollCount = 0;
//            } else if (rollCount < 3) {
//                rollButton.setDisable(false);
//            } else {
//                switchPlayer(gameBoard.getPlayers());
//                setCurrentPlayerLabel(currentPlayer);
//                rollCount = 0;
//            }
//        }
//
//
//
//
//
//    }
}