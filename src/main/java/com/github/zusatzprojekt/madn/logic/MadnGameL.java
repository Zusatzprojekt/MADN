package com.github.zusatzprojekt.madn.logic;

import com.github.zusatzprojekt.madn.enums.MadnFigurePlacement;
import com.github.zusatzprojekt.madn.enums.MadnPlayerId;
import com.github.zusatzprojekt.madn.logic.components.MadnFigurePosition;
import com.github.zusatzprojekt.madn.ui.components.MadnBoardV;
import com.github.zusatzprojekt.madn.ui.components.MadnDiceV;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

import java.util.Arrays;
import java.util.Map;

public class MadnGameL {
    private final ObjectProperty<MadnPlayerL> currentPlayer = new SimpleObjectProperty<>();
    private final MadnPlayerL[] playerList;
    private final MadnDiceL dice = new MadnDiceL();
    private final BooleanProperty initPhase = new SimpleBooleanProperty(true);


    public MadnGameL(Map<String, Object> players, MadnBoardV board, MadnDiceV vDice) {
        playerList = initPlayers(players);

        initBindings(board);
        initListeners(board);
        initHandlers(vDice);
    }

    private void initBindings(MadnBoardV board) {
        board.initPhaseProperty().bind(initPhase);
    }

    private MadnPlayerL[] initPlayers(Map<String, Object> players) {
        int playerCount = (int) players.get("playerCount");
        MadnPlayerL[] playerList = new MadnPlayerL[playerCount];

        if ((boolean) players.get("playerRed")) {
            playerList[--playerCount] = new MadnPlayerL(MadnPlayerId.RED);
        }

        if ((boolean) players.get("playerGreen")) {
            playerList[--playerCount] = new MadnPlayerL(MadnPlayerId.GREEN);
        }

        if ((boolean) players.get("playerYellow")) {
            playerList[--playerCount] = new MadnPlayerL(MadnPlayerId.YELLOW);
        }

        if ((boolean) players.get("playerBlue")) {
            playerList[--playerCount] = new MadnPlayerL(MadnPlayerId.BLUE);
        }

        return playerList;
    }

    private void initListeners(MadnBoardV board) {

        currentPlayer.addListener((observableValue, oldPlayer, player) -> {
            board.currentRollProperty().bind(player.lastRollObservable());
        });
    }

    private void initHandlers(MadnDiceV vDice) {

        vDice.setOnDiceClicked(event -> {
            vDice.disable();

            int roll = dice.roll();
            currentPlayer.getValue().setLastRoll(roll);
            vDice.startAnimation(roll);
        });

        vDice.setOnFinished(event -> {
            rollFinished();
            vDice.enable();
        });
    }

    private void switchPlayer(MadnPlayerL[] players) {
        int pLength = players.length;
        int curIndex = Arrays.asList(players).indexOf(currentPlayer.getValue());

        while (players[(curIndex + 1) % pLength].isFinished() && Arrays.stream(players).filter(MadnPlayerL::isFinished).count() > 2) {
            curIndex = (curIndex + 1) % pLength;
        }

        currentPlayer.setValue(players[(curIndex + 1) % pLength]);
    }

    // TODO: Testszenario entfernen
    int index = -1;

    private void rollFinished() {
        System.out.println("Spieler " + currentPlayer.getValue().getPlayerID() + " hast eine " + currentPlayer.getValue().getLastRoll() + " gewürfelt!");

        index = index + currentPlayer.getValue().getLastRoll();

        if (index == -1) {
            currentPlayer.getValue().getFigures()[0].setFigurePosition(new MadnFigurePosition(MadnFigurePlacement.WAYPOINTS, 0));
            index = 0;
        } else if (index >= 40) {
            currentPlayer.getValue().getFigures()[0].setFigurePosition(new MadnFigurePosition(MadnFigurePlacement.HOME, index % 40));
        } else {
            currentPlayer.getValue().getFigures()[0].setFigurePosition(new MadnFigurePosition(MadnFigurePlacement.WAYPOINTS, index));
        }

    }

    public void startGame() {
        currentPlayer.setValue(playerList[0]);
        initPhase.setValue(false);
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
//        //TODO: Implementieren
//        gameViewController.getCurrentPlayerLabel().textProperty().bind(currentPlayerString);
//        rollButton.setOnAction(event -> rollDice(gameBoard.getPlayers()));
//    }
//
//    private void startGame(){
//        // TODO: Implementieren
//
//        setCurrentPlayerLabel(currentPlayer);
//        rollButton.setDisable(false);
//    }
//
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
//                //TODO: Implementieren
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