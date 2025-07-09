package com.github.zusatzprojekt.madn.logic;

import com.github.zusatzprojekt.madn.enums.MadnFigurePlacement;
import com.github.zusatzprojekt.madn.enums.MadnGamePhase;
import com.github.zusatzprojekt.madn.enums.MadnPlayerId;
import com.github.zusatzprojekt.madn.logic.components.MadnFigurePosition;
import com.github.zusatzprojekt.madn.ui.components.MadnBoardV;
import com.github.zusatzprojekt.madn.ui.components.MadnDiceV;
import com.github.zusatzprojekt.madn.ui.components.MadnFigureV;
import com.github.zusatzprojekt.madn.ui.components.MadnInfoTextV;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MadnGameL {
    private final MadnDiceL dice;
    private final MadnInfoTextV infoText;
    private final MadnPlayerL[] playerList;
    private final Map<MadnPlayerId, MadnFigureL[]> bases;
    private final Map<MadnPlayerId, MadnFigureL[]> homes;
    private final MadnFigureL[] waypoints = new MadnFigureL[40];
    private final ObjectProperty<MadnPlayerL> currentPlayer = new SimpleObjectProperty<>();
    private final ObjectProperty<MadnGamePhase> gamePhase = new SimpleObjectProperty<>(MadnGamePhase.INIT);
    private final ObjectProperty<EventHandler<MouseEvent>> figureClicked = new SimpleObjectProperty<>(this::onFigureClicked);
    private MadnPlayerL[] activePlayers;
    private int rollCount = 0;
    private int finishedPlayers = 0;
    private MadnFigureL backToBase;

    public MadnGameL(Map<String, Object> players, MadnBoardV board, MadnDiceV vDice, MadnInfoTextV iTxt) {
        dice = new MadnDiceL(vDice);
        infoText = iTxt;
        playerList = initPlayers(players);
        bases = initBases();
        homes = initHomes();

        initBindings(board);
        initListeners(board);
        initHandlers(vDice);
    }

    private Map<MadnPlayerId, MadnFigureL[]> initBases() {
        Map<MadnPlayerId, MadnFigureL[]> bases = new HashMap<>();

        for (MadnPlayerL player: playerList) {
            MadnFigureL[] playerFigs = player.getFigures();
            MadnFigureL[] figs = new MadnFigureL[playerFigs.length];

            System.arraycopy(playerFigs, 0, figs, 0, playerFigs.length);

            bases.put(player.getPlayerID(), figs);
        }

        return bases;
    }

    private Map<MadnPlayerId, MadnFigureL[]> initHomes() {
        Map<MadnPlayerId, MadnFigureL[]> homes = new HashMap<>();

        for (MadnPlayerL player: playerList) {
            homes.put(player.getPlayerID(), new MadnFigureL[player.getFigures().length]);
        }

        return homes;
    }

    private void initBindings(MadnBoardV board) {
        board.gamePhaseProperty().bindBidirectional(gamePhase);
    }

    private MadnPlayerL[] initPlayers(Map<String, Object> players) {
        int playerCount = (int) players.get("playerCount");
        MadnPlayerL[] playerList = new MadnPlayerL[playerCount];

        if ((boolean) players.get("playerRed")) {
            playerList[--playerCount] = new MadnPlayerL(MadnPlayerId.RED, 30);
        }

        if ((boolean) players.get("playerGreen")) {
            playerList[--playerCount] = new MadnPlayerL(MadnPlayerId.GREEN, 20);
        }

        if ((boolean) players.get("playerYellow")) {
            playerList[--playerCount] = new MadnPlayerL(MadnPlayerId.YELLOW, 10);
        }

        if ((boolean) players.get("playerBlue")) {
            playerList[--playerCount] = new MadnPlayerL(MadnPlayerId.BLUE, 0);
        }

        return playerList;
    }

    private void initListeners(MadnBoardV board) {

        currentPlayer.addListener((observableValue, oldPlayer, player) -> {
            board.currentRollProperty().bind(player.lastRollObservable());
        });

        gamePhase.addListener((observable, oldPhase, newPhase) -> {
            if (oldPhase == MadnGamePhase.MOVE_ANIMATION && newPhase == MadnGamePhase.THROW_ANIMATION) {
                throwPlayer();
            }

            System.out.println("Spielphase " + oldPhase + " -> " + newPhase); // TODO: Entfernen
        });
    }

    private void initHandlers(MadnDiceV vDice) {

        vDice.setOnDiceClicked(event -> {
            dice.setEnabled(false);
            int roll = dice.roll();
            currentPlayer.getValue().setLastRoll(roll);
            vDice.startAnimation(roll);
        });

        vDice.setOnFinished(event -> {
            rollFinished();
        });
    }

    private void rollFinished() {
        System.out.println("Spieler " + currentPlayer.getValue().getPlayerID() + " hat eine " + currentPlayer.getValue().getLastRoll() + " gewürfelt!"); // TODO: Entfernen

        switch (gamePhase.getValue()) {
            case START_ROLL:
                startRoll();
                break;

            case DICE_ROLL:
                diceRoll();
                break;
        }

    }

    private void diceRoll() {
        rollCount++;

        // TODO: Wird aktuell bearbeitet
        if (rollCount < 3) {
            int canMoveCount = getMovablePlayerCount();

            if (canMoveCount < 1) {
                dice.setEnabled(true);
            } else {
                gamePhase.setValue(MadnGamePhase.FIGURE_SELECT);
            }

        } else if (rollCount == 3 && getCurrentPlayer().getLastRoll() == 6) {
            int canMoveCount = getMovablePlayerCount();

            if (canMoveCount > 0) {
                gamePhase.setValue(MadnGamePhase.FIGURE_SELECT);
            }

        } else {
            rollCount = 0;
            gamePhase.setValue(MadnGamePhase.DICE_ROLL);
            switchPlayer(playerList);
            dice.setEnabled(true);
        }

    }

    public void setupGame() {
        activePlayers = playerList;
        currentPlayer.setValue(activePlayers[0]);
    }

    public void startGame() {
        gamePhase.setValue(MadnGamePhase.START_ROLL);
        dice.setEnabled(true);
    }

    private int getMovablePlayerCount() {
        getCurrentPlayer().checkCanMove(waypoints);

        return (int) Arrays.stream(getCurrentPlayer().getFigures()).filter(MadnFigureL::canMove).count();
    }

    private void switchPlayer(MadnPlayerL[] players) {
        int pLength = players.length;
        int curIndex = Arrays.asList(players).indexOf(currentPlayer.getValue());

        while (players[(curIndex + 1) % pLength].isFinished() && Arrays.stream(players).filter(MadnPlayerL::isFinished).count() > 2) {
            curIndex = (curIndex + 1) % pLength;
        }

        currentPlayer.setValue(players[(curIndex + 1) % pLength]);
    }

    private void startRoll() {
        if (currentPlayer.getValue() == activePlayers[activePlayers.length - 1]) {
            activePlayers = getHighestRoll();

            if (activePlayers.length > 1) {
                String info = generateInfoText();

                infoText.setOnFinished(event -> {
                    switchPlayer(activePlayers);
                    dice.setEnabled(true);
                });

                infoText.showTextOverlay(info, Duration.seconds(4));
            } else {
                currentPlayer.setValue(activePlayers[0]);

                infoText.setOnFinished(event -> {
                    gamePhase.setValue(MadnGamePhase.DICE_ROLL);
                    dice.setEnabled(true);

                    System.out.println("Spieler " + currentPlayer.getValue().getPlayerID() + " hat die höchste Zahl (" + currentPlayer.getValue().getLastRoll() + ") gewürfelt. Dieser Spieler beginnt"); //TODO: Entfernen
                });

                infoText.showTextOverlay( switch (currentPlayer.getValue().getPlayerID()) {
                    case BLUE -> "Blau";
                    case YELLOW -> "Gelb";
                    case GREEN -> "Grün";
                    case RED -> "Rot";
                    case NONE -> "NONE";
                } + " beginnt!", Duration.seconds(2));
            }

        } else {
            switchPlayer(activePlayers);
            dice.setEnabled(true);
        }
    }

    private String generateInfoText() {
        StringBuilder info = new StringBuilder("Spieler mit gleichem Wurf: ");

        for (int i = 0; i < activePlayers.length; i++) {
            info.append(switch (activePlayers[i].getPlayerID()) {
                case BLUE -> "Blau";
                case YELLOW -> "Gelb";
                case GREEN -> "Grün";
                case RED -> "Rot";
                case NONE -> "NONE";
            });

            if (i < activePlayers.length - 1) {
                info.append(", ");
            }
        }

        info.append("\nWeiter würfeln!");
        return info.toString();
    }

    private MadnPlayerL[] getHighestRoll() {
        MadnPlayerL[] players = Arrays.stream(activePlayers).sorted(Comparator.comparingInt(MadnPlayerL::getLastRoll).reversed()).toArray(MadnPlayerL[]::new);
        return Arrays.stream(players).filter(player -> player.getLastRoll() == players[0].getLastRoll()).toArray(MadnPlayerL[]::new);
    }

    private void onFigureClicked(MouseEvent mouseEvent) {
        MadnFigureL figure = ((MadnFigureV) ((Node) mouseEvent.getSource()).getParent()).getLogicalFigure();
        MadnFigurePosition figurePos = figure.figurePositionObservable().getValue();
        MadnPlayerL player = figure.getPlayer();

        if (figurePos.getFigurePlacement() == MadnFigurePlacement.BASE && player.getLastRoll() == 6) {
            MadnFigureL[] base = bases.get(player.getPlayerID());
            int startIndex = player.getStartIndex();
            base[figurePos.getFieldIndex()] = null;

            if (waypoints[startIndex] != null) {
                backToBase = waypoints[startIndex];
            }

            waypoints[startIndex] = figure;

            figure.setFigurePosition(new MadnFigurePosition(MadnFigurePlacement.WAYPOINTS, startIndex));

            gamePhase.setValue(MadnGamePhase.MOVE_ANIMATION);

        }







        System.out.println(figure + " has been Clicked!");
    }

    private void throwPlayer() {
        if (backToBase != null) {

            infoText.setOnFinished(event -> {
                MadnFigurePosition basePos = backToBase.getBasePosition();
                MadnFigureL[] base = bases.get(backToBase.getPlayer().getPlayerID());
                base[basePos.getFieldIndex()] = backToBase;
                backToBase.setFigurePosition(basePos);
                backToBase = null;

                afterFigureAnimations();
            });

            infoText.showTextOverlay(switch (backToBase.getPlayer().getPlayerID()) {
                case BLUE -> "Blau";
                case YELLOW -> "Gelb";
                case GREEN -> "Grün";
                case RED -> "Rot";
                case NONE -> "NONE";
            } + " wurde geworfen!", Duration.seconds(1));

        } else {
            afterFigureAnimations();
        }

    }

    private void afterFigureAnimations() {
        rollCount = 0;
        gamePhase.setValue(MadnGamePhase.DICE_ROLL);
        switchPlayer(playerList);
        dice.setEnabled(true);
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

    public ObservableValue<EventHandler<MouseEvent>> figureClickedObservable() {
        return figureClicked;
    }

}