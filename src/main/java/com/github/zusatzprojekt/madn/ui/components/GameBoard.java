package com.github.zusatzprojekt.madn.ui.components;

import com.github.zusatzprojekt.madn.interfaces.FxmlValueReceiver;
import com.github.zusatzprojekt.madn.logic.Figure;
import com.github.zusatzprojekt.madn.logic.Game;
import com.github.zusatzprojekt.madn.logic.Player;
import com.github.zusatzprojekt.madn.ui.controller.GameViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.Map;

public class GameBoard extends StackPane implements FxmlValueReceiver {
    private Point2D[] fields;
    private Point2D[] blueBase = new Point2D[]{new Point2D(85, 850), new Point2D(85, 765), new Point2D(0, 765), new Point2D(0,850)};
    private Point2D[] yellowBase = new Point2D[]{new Point2D(0, 85), new Point2D(85, 85), new Point2D(85, 0), new Point2D(0, 0)};
    private Point2D[] greenBase = new Point2D[]{new Point2D(765, 0), new Point2D(765, 85), new Point2D(850, 85), new Point2D(850, 0)};
    private Point2D[] redBase = new Point2D[]{new Point2D(765, 850), new Point2D(765, 765), new Point2D(850, 765), new Point2D(850, 850)};
    private Point2D[] blueHome = new Point2D[]{new Point2D(425, 765), new Point2D(425, 680), new Point2D(425, 595), new Point2D(425,510)};
    private Point2D[] yellowHome = new Point2D[]{new Point2D(85, 425), new Point2D(170, 425), new Point2D(255, 425), new Point2D(340, 425)};
    private Point2D[] greenHome = new Point2D[]{new Point2D(425, 85), new Point2D(425, 170), new Point2D(425, 255), new Point2D(425, 340)};
    private Point2D[] redHome = new Point2D[]{new Point2D(510, 425), new Point2D(595, 425), new Point2D(680, 425), new Point2D(765, 425)};
    private Player[] players;
    private Game game;

    @FXML
    private Pane playerPane;
    @FXML
    private Polygon waypoints;
    @FXML
    private Rectangle overlayRect;

    public GameBoard() throws IOException {
        // Load game-board.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game-board.fxml"));

        // Set this class as root and controller for use as custom component
        loader.setRoot(this);
        loader.setController(this);

        loader.load();

        // Initialize fields with the polygon-points from the board to get the coordinates of each field on the board
        Double[] allPoints = waypoints.getPoints().toArray(new Double[0]);
        fields = new Point2D[allPoints.length / 2];

        for (int i = 0; i < fields.length; i++) {
            fields[i] = new Point2D(allPoints[i * 2], allPoints[i * 2 + 1]);
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
            playerPane.getChildren().addAll(p.getFigures());
            setupPlayer(p);
        }

        game = new Game(this, (GameViewController) values.remove("gameViewController"));
    }

    private void setupPlayer(Player player) {

        switch (player.getPlayerID()) {
            case BLUE:
                placeFigure(player.getFigures(), blueBase);
                break;

            case YELLOW:
                placeFigure(player.getFigures(), yellowBase);
                break;

            case GREEN:
                placeFigure(player.getFigures(), greenBase);
                break;

            case RED:
                placeFigure(player.getFigures(), redBase);
                break;
        }
    }

    private void placeFigure(Figure figure, Point2D point) {
        figure.setTranslateX(point.getX());
        figure.setTranslateY(point.getY());
    }

    private void placeFigure(Figure[] figures, Point2D[] points) {
        for (Figure f: figures) {
            if (f.getCurrentField() < 0) {
                placeFigure(f, points[-1 + Math.abs(f.getCurrentField())]);
            } else if (f.getCurrentField() > 39) {
                //TODO spielfelder
            } else {
                //TODO ende
            }
        }
    }

    public Player[] getPlayers() {
        return players;
    }

}
