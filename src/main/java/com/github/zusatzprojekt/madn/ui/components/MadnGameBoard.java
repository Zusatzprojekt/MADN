package com.github.zusatzprojekt.madn.ui.components;

import com.github.zusatzprojekt.madn.interfaces.FxmlValueReceiver;
import com.github.zusatzprojekt.madn.logic.Figure;
import com.github.zusatzprojekt.madn.logic.Game;
import com.github.zusatzprojekt.madn.logic.Player;
import com.github.zusatzprojekt.madn.ui.controller.GameViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.Map;

public class MadnGameBoard extends AnchorPane implements FxmlValueReceiver {
    private final MadnField[] baseBlue, baseYellow, baseGreen, baseRed, waypoints, homeBlue, homeYellow, homeGreen, homeRed;
    private Player[] players;
    private Game game;

    @FXML
    private Pane baseContainerBlue, baseContainerYellow, baseContainerGreen, baseContainerRed;
    @FXML
    private Pane waypointContainer;
    @FXML
    private Pane homeContainerBlue, homeContainerYellow, homeContainerGreen, homeContainerRed;
    @FXML
    private Pane playerContainer;
    @FXML
    private Rectangle overlayRect;

    @SuppressWarnings("SuspiciousToArrayCall")
    public MadnGameBoard() throws IOException {
        // Load madn-game-board.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("madn-game-board.fxml"));

        // Set this class as root and controller for use as custom component
        loader.setRoot(this);
        loader.setController(this);

        loader.load();

        // Get fields from board
        baseBlue = baseContainerBlue.getChildren().toArray(MadnField[]::new);
        baseYellow = baseContainerYellow.getChildren().toArray(MadnField[]::new);
        baseGreen = baseContainerGreen.getChildren().toArray(MadnField[]::new);
        baseRed = baseContainerRed.getChildren().toArray(MadnField[]::new);

        waypoints = waypointContainer.getChildren().toArray(MadnField[]::new);

        homeBlue = homeContainerBlue.getChildren().toArray(MadnField[]::new);
        homeYellow = homeContainerYellow.getChildren().toArray(MadnField[]::new);
        homeGreen = homeContainerGreen.getChildren().toArray(MadnField[]::new);
        homeRed = homeContainerRed.getChildren().toArray(MadnField[]::new);
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

        players = new Player[playerCount];

        if (playerRed) {
            playerCount--;
            players[playerCount] = new Player(Player.PlayerID.RED, 30);
        }

        if (playerGreen) {
            playerCount--;
            players[playerCount] = new Player(Player.PlayerID.GREEN, 20);
        }

        if (playerYellow) {
            playerCount--;
            players[playerCount] = new Player(Player.PlayerID.YELLOW, 10);
        }

        if (playerBlue) {
            playerCount--;
            players[playerCount] = new Player(Player.PlayerID.BLUE, 0);
        }

        for (Player p: players) {
            playerContainer.getChildren().addAll(p.getFigures());
            setupPlayer(p);
        }

        game = new Game(this, (GameViewController) values.remove("gameViewController"));
    }

    private void setupPlayer(Player player) {
        for (Figure figure : player.getFigures()) {
            placeFigure(player.getPlayerID(), figure);
        }
    }

    private void placeFigure(Player.PlayerID playerID, Figure figure) {
        int currentField = figure.getCurrentField();

        if (currentField < 0) {
            MadnField[] fields = switch (playerID) {
                case BLUE -> baseBlue;
                case YELLOW -> baseYellow;
                case GREEN -> baseGreen;
                case RED -> baseRed;
            };

            setFigureCoordinates(figure, fields[-1 + Math.abs(currentField)]);
        } else if (currentField < 40) {
            setFigureCoordinates(figure, waypoints[currentField]);
        } else if (currentField < 44){
            MadnField[] fields = switch (playerID) {
                case BLUE -> homeBlue;
                case YELLOW -> homeYellow;
                case GREEN -> homeGreen;
                case RED -> homeRed;
            };

            setFigureCoordinates(figure, fields[currentField - 40]);
        }
    }

    private void setFigureCoordinates(Figure figure, MadnField field) {
        figure.setLayoutX(field.getCenterX());
        figure.setLayoutY(field.getCenterY());
    }

    public MadnField[] getBaseBlue() {
        return baseBlue;
    }

    public MadnField[] getBaseYellow() {
        return baseYellow;
    }

    public MadnField[] getBaseGreen() {
        return baseGreen;
    }

    public MadnField[] getBaseRed() {
        return baseRed;
    }

    public MadnField[] getWaypoints() {
        return waypoints;
    }

    public MadnField[] getHomeBlue() {
        return homeBlue;
    }

    public MadnField[] getHomeYellow() {
        return homeYellow;
    }

    public MadnField[] getHomeGreen() {
        return homeGreen;
    }

    public MadnField[] getHomeRed() {
        return homeRed;
    }

    public Player[] getPlayers() {
        return players;
    }
}
