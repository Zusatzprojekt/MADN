package com.github.zusatzprojekt.madn.logic;

import com.github.zusatzprojekt.madn.enums.MadnFigurePlacement;
import com.github.zusatzprojekt.madn.enums.MadnGamePhase;
import com.github.zusatzprojekt.madn.enums.MadnPlayerId;
import com.github.zusatzprojekt.madn.logic.components.MadnFigurePosition;
import com.github.zusatzprojekt.madn.ui.AppManager;
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

import java.util.*;

public class MadnGameL {
    private final int MAX_ROLL_COUNT = 3;
    private final MadnDiceL dice;
    private final MadnInfoTextV infoText;
    private final MadnPlayerL[] playerList;
    private final Map<String, Object> playersInGame;
    private final Map<MadnPlayerId, MadnFigureL[]> bases;
    private final Map<MadnPlayerId, MadnFigureL[]> homes;
    private final MadnFigureL[] waypoints = new MadnFigureL[40];
    private final ObjectProperty<MadnPlayerL> currentPlayer = new SimpleObjectProperty<>();
    private final ObjectProperty<MadnGamePhase> gamePhase = new SimpleObjectProperty<>(MadnGamePhase.INIT);
    private final ObjectProperty<EventHandler<MouseEvent>> figureClicked = new SimpleObjectProperty<>(this::onFigureClicked);
    private MadnPlayerL[] activePlayers;
    private MadnFigureL backToBase;
    private int finishedPlayers = 0;
    private int rollCount = 0;

    public MadnGameL(Map<String, Object> players, MadnBoardV board, MadnDiceV vDice, MadnInfoTextV iTxt) {
        dice = new MadnDiceL(vDice);
        infoText = iTxt;

        playersInGame = players;
        playerList = initPlayers(players);
        bases = initBases();
        homes = initHomes();

        board.setGame(this);

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

            if (oldPhase == MadnGamePhase.MOVE_ANIMATION && newPhase == MadnGamePhase.THROW_TRIGGER) {
                throwPlayer();
            }
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

    public void setupGame() {
        activePlayers = playerList;
        currentPlayer.setValue(activePlayers[0]);
    }

    public void startGame() {
        gamePhase.setValue(MadnGamePhase.START_ROLL);
        dice.setEnabled(true);
    }

    private void rollFinished() {

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
        int canMoveCount = getMovableFigureCount();
        int baseFigureCount = (int) Arrays.stream(getCurrentPlayer().getFigures()).filter(figure -> figure.getFigurePosition().getFigurePlacement() == MadnFigurePlacement.BASE).count();
        int homeFigureCount = (int) Arrays.stream(getCurrentPlayer().getFigures()).filter(figure -> figure.getFigurePosition().getFigurePlacement() == MadnFigurePlacement.HOME).count();
        MadnFigureL[] home = homes.get(getCurrentPlayer().getPlayerID());
        boolean figuresOptimalHome = true;
        rollCount++;

        for (int i = 0; i < home.length - homeFigureCount; i++) {
            if (home[i] != null) {
                figuresOptimalHome = false;
                break;
            }
        }

        if (canMoveCount > 0) {
            gamePhase.setValue(MadnGamePhase.FIGURE_SELECT);

        } else if (rollCount < MAX_ROLL_COUNT && ((baseFigureCount > 0 && figuresOptimalHome) || getCurrentPlayer().getLastRoll() == 6)) {
            dice.setEnabled(true);

        } else {

            if (baseFigureCount < getCurrentPlayer().getFigures().length) {

                infoText.setOnFinished(event ->  {
                    switchPlayer(playerList);
                    rollCount = 0;

                    infoText.setOnFinished(e -> dice.setEnabled(true));
                    infoText.showTextOverlay(getPlayerString(getCurrentPlayer()), Duration.millis(500));
                });
                infoText.showTextOverlay("Kein Zug möglich!", Duration.millis(500));

            } else {
                switchPlayer(playerList);
                rollCount = 0;

                infoText.setOnFinished(event -> dice.setEnabled(true));
                infoText.showTextOverlay(getPlayerString(getCurrentPlayer()), Duration.millis(500));
            }
        }
    }

    private int getMovableFigureCount() {
        getCurrentPlayer().enableCanMove(waypoints, bases, homes);

        return (int) Arrays.stream(getCurrentPlayer().getFigures()).filter(MadnFigureL::canMove).count();
    }

    private void switchPlayer(MadnPlayerL[] players) {
        int pLength = players.length;
        int curIndex = Arrays.asList(players).indexOf(currentPlayer.getValue());
        int counter = 0;

        while (players[(curIndex + 1) % pLength].isFinished() && Arrays.stream(players).anyMatch(player -> !player.isFinished())) {

            if (counter > pLength * 2) {
                throw new RuntimeException(new Exception("switchPlayer exceeded max loop count"));
            }

            curIndex = (curIndex + 1) % pLength;
            counter++;
        }

        currentPlayer.setValue(players[(curIndex + 1) % pLength]);
    }

    private String getPlayerString(MadnPlayerL player) {
        return switch (player.getPlayerID()) {
            case BLUE -> "Blau";
            case YELLOW -> "Gelb";
            case GREEN -> "Grün";
            case RED -> "Rot";
            case NONE -> "NONE";
        };
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
                });

                infoText.showTextOverlay( getPlayerString(getCurrentPlayer()) + " beginnt!", Duration.seconds(2));
            }

        } else {
            switchPlayer(activePlayers);
            dice.setEnabled(true);
        }
    }

    private String generateInfoText() {
        StringBuilder sb = new StringBuilder("Spieler mit gleichem Wurf: ");

        for (int i = 0; i < activePlayers.length; i++) {
            sb.append(getPlayerString(activePlayers[i]));

            if (i < activePlayers.length - 1) {
                sb.append(", ");
            }
        }

        sb.append("\nWeiter würfeln!");
        return sb.toString();
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
            int destinationIndex = player.getStartIndex();
            base[figurePos.getFieldIndex()] = null;

            if (waypoints[destinationIndex] != null) {
                backToBase = waypoints[destinationIndex];
            }

            waypoints[destinationIndex] = figure;

            figure.setFigurePosition(new MadnFigurePosition(MadnFigurePlacement.WAYPOINTS, destinationIndex));

        } else if (figurePos.getFigurePlacement() == MadnFigurePlacement.WAYPOINTS) {
            int homeIndex = player.getHomeIndex();
            int fieldIndex = figurePos.getFieldIndex();

            waypoints[fieldIndex] = null;

            if (fieldIndex <= homeIndex && fieldIndex + player.getLastRoll() > homeIndex) {
                MadnFigureL[] home = homes.get(player.getPlayerID());
                int destinationIndex = fieldIndex + player.getLastRoll() - homeIndex - 1;

                home[destinationIndex] = figure;
                figure.setFigurePosition(new MadnFigurePosition(MadnFigurePlacement.HOME, destinationIndex));

            } else {
                int destinationIndex = (fieldIndex + player.getLastRoll()) % waypoints.length;

                if (waypoints[destinationIndex] != null) {
                    backToBase = waypoints[destinationIndex];
                }

                waypoints[destinationIndex] = figure;
                figure.setFigurePosition(new MadnFigurePosition(MadnFigurePlacement.WAYPOINTS, destinationIndex));
            }

        } else if (figurePos.getFigurePlacement() == MadnFigurePlacement.HOME) {
            int oldIndex = figurePos.getFieldIndex();
            int newIndex = oldIndex + player.getLastRoll();
            MadnFigureL[] home = homes.get(player.getPlayerID());

            if (newIndex < home.length && home[newIndex] == null) {
                home[oldIndex] = null;
                home[newIndex] = figure;

                figure.setFigurePosition(new MadnFigurePosition(MadnFigurePlacement.HOME, newIndex));
            }
        }

        gamePhase.setValue(MadnGamePhase.MOVE_ANIMATION);
    }

    private void throwPlayer() {
        if (backToBase != null) {

            infoText.setOnFinished(event -> {
                MadnFigurePosition basePos = backToBase.getBasePosition();
                MadnFigureL[] base = bases.get(backToBase.getPlayer().getPlayerID());

                gamePhase.setValue(MadnGamePhase.MOVE_ANIMATION);
                base[basePos.getFieldIndex()] = backToBase;
                backToBase.setFigurePosition(basePos);
                backToBase = null;
            });

            infoText.showTextOverlay(getPlayerString(backToBase.getPlayer()) + " wurde geworfen!", Duration.millis(750));

        } else {
            afterFigureAnimations();
        }
    }

    private void afterFigureAnimations() {
        int figuresInHome = (int) Arrays.stream(homes.get(getCurrentPlayer().getPlayerID())).filter(Objects::nonNull).count();

        if (figuresInHome >= getCurrentPlayer().getFigures().length && !getCurrentPlayer().isFinished()) {
            setPlayerFinishPos();
        }

        if (finishedPlayers >= playerList.length - 1) {
            switchPlayer(playerList);
            setPlayerFinishPos();

            AppManager.loadScene("ui/end-view.fxml", createDataPacket());

        } else  {

            if (getCurrentPlayer().isFinished()) {

                infoText.setOnFinished(event -> {
                    nextPlayerInfoAndSwitch();
                });
                infoText.showTextOverlay(getPlayerString(getCurrentPlayer()) + " ist fertig!", Duration.millis(500));

            } else if (rollCount >= MAX_ROLL_COUNT || getCurrentPlayer().getLastRoll() != 6 || getCurrentPlayer().isFinished()) {
                nextPlayerInfoAndSwitch();

            } else {
                gamePhase.setValue(MadnGamePhase.DICE_ROLL);
                dice.setEnabled(true);
            }
        }
    }

    private void nextPlayerInfoAndSwitch() {
        rollCount = 0;

        switchPlayer(playerList);

        infoText.setOnFinished(event -> {
            gamePhase.setValue(MadnGamePhase.DICE_ROLL);
            dice.setEnabled(true);
        });

        infoText.showTextOverlay(getPlayerString(getCurrentPlayer()), Duration.millis(500));
    }

    private void setPlayerFinishPos() {
        finishedPlayers++;
        currentPlayer.getValue().setFinishedPos(finishedPlayers);
    }

    private Map<String, Object> createDataPacket() {
        Map<String, Object> map = new HashMap<>(playersInGame);
        map.put("playerObjectArray", playerList);

        return map;
    }

    public MadnPlayerL[] getPlayerList() {
        return playerList;
    }

    public MadnPlayerL getCurrentPlayer() {
        return currentPlayer.getValue();
    }

    public void setGamePhase(MadnGamePhase phase) {
        gamePhase.setValue(phase);
    }

    public ObservableValue<MadnPlayerL> currentPlayerObservable() {
        return currentPlayer;
    }

    public ObservableValue<EventHandler<MouseEvent>> figureClickedObservable() {
        return figureClicked;
    }

    public ObjectProperty<MadnGamePhase> gamePhaseProperty() {
        return gamePhase;
    }

}