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

/**
 * Hauptklasse zur Spiellogik für „Mensch ärgere Dich nicht“.
 * Sie verwaltet Spieler, Spielfiguren, Spielphasen und -abläufe, inklusive Würfelmechanik,
 * Bewegungen, Wechseln der Spieler und Siegbedingungen.
 */
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

    /**
     * Konstruktor. Initialisiert die Spiellogik mit UI-Komponenten und Spielern.
     *
     * @param players Spielereinstellungen (aktivierte Farben / Anzahl der Spieler)
     * @param board   das visuelle Spielfeld
     * @param vDice   die visuelle Würfelkomponente
     * @param iTxt    die Text-Info-Komponente
     */
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

    /**
     * Initialisiert die Startpositionen (Basen) der Figuren aller Spieler.
     *
     * @return Gibt die Basis für jeden Spieler und dessen Figur als HashMap zurück.
     */
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

    /**
     * Initialisiert die Zielbereiche (HOME) aller Spieler.
     *
     * @return Gibt die Zielpositionen für jeden Spieler und dessen Figuren als HashMap zurück.
     */
    private Map<MadnPlayerId, MadnFigureL[]> initHomes() {
        Map<MadnPlayerId, MadnFigureL[]> homes = new HashMap<>();

        for (MadnPlayerL player: playerList) {
            homes.put(player.getPlayerID(), new MadnFigureL[player.getFigures().length]);
        }

        return homes;
    }

    /**
     * Erstellt die Spieler basierend auf der übergebenen Konfiguration.
     *
     * @param players Spielereinstellungen (aktivierte Farben / Anzahl der Spieler)
     * @return  Gibt die Spielerliste mit allen aktiven Spielern als Array zurück
     */
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

    /**
     * Verbindet Listener mit dem visuellen Spielfeld und den Spielphasen, wie z. B. das Werfen anderer Spielfiguren.
     *
     * @param board das visuelle Spielfeld
     */
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

    /**
     * Initialisiert UI-Handler für Würfelinteraktion.
     *
     * @param vDice die visuelle Würfelkomponente
     */
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

    /**
     * Setzt das Spiel initial auf den ersten aktiven Spieler.
     */
    public void setupGame() {
        activePlayers = playerList;
        currentPlayer.setValue(activePlayers[0]);
    }

    /**
     * Startet das Spiel und aktiviert den Würfel.
     */
    public void startGame() {
        gamePhase.setValue(MadnGamePhase.START_ROLL);
        dice.setEnabled(true);
    }

    /**
     * Wird nach Abschluss der Würfelanimation aufgerufen.
     * Leitet je nach Spielphasen entsprechend weiter.
     */
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

    /**
     * Führt die Spiel-Logik nach einem Würfelwurf aus.
     *
     * <p>Diese Methode entscheidet basierend auf dem aktuellen Würfelergebnis und dem Zustand
     * der Spielfiguren, ob der Spieler eine Figur auswählen darf, erneut würfeln darf oder ob
     * der nächste Spieler an der Reihe ist.</p>
     *
     * <p>Berücksichtigt dabei:
     * <ul>
     *   <li>Ob eine Figur bewegt werden kann</li>
     *   <li>Ob eine Figur aus der BASE ins Spiel darf</li>
     *   <li>Ob Figuren optimal im HOME-Bereich stehen</li>
     *   <li>Ob der Spieler eine 6 geworfen hat</li>
     *   <li>Ob das Wurflimit erreicht ist</li>
     * </ul></p>
     */
    private void diceRoll() {
        int canMoveCount = getMovableFigureCount();
        int baseFigureCount = (int) Arrays.stream(getCurrentPlayer().getFigures()).filter(figure -> figure.getFigurePosition().getFigurePlacement() == MadnFigurePlacement.BASE).count();
        int homeFigureCount = (int) Arrays.stream(getCurrentPlayer().getFigures()).filter(figure -> figure.getFigurePosition().getFigurePlacement() == MadnFigurePlacement.HOME).count();
        MadnFigureL[] home = homes.get(getCurrentPlayer().getPlayerID());
        boolean wayFigure = Arrays.stream(getCurrentPlayer().getFigures()).anyMatch(figure -> figure.getFigurePosition().getFigurePlacement() == MadnFigurePlacement.WAYPOINTS);
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

        } else if (rollCount < MAX_ROLL_COUNT && ((baseFigureCount > 0 && figuresOptimalHome && !wayFigure) || getCurrentPlayer().getLastRoll() == 6)) {
            dice.setEnabled(true);

        } else {

            if (baseFigureCount < getCurrentPlayer().getFigures().length) {

                infoText.setOnFinished(event ->  {
                    switchPlayer(playerList);
                    rollCount = 0;

                    infoText.setOnFinished(e -> dice.setEnabled(true));
                    infoText.showTextOverlay(getPlayerString(getCurrentPlayer()), Duration.millis(500));
                });
                infoText.showTextOverlay("Kein Zug möglich!", Duration.millis(750));

            } else {
                switchPlayer(playerList);
                rollCount = 0;

                infoText.setOnFinished(event -> dice.setEnabled(true));
                infoText.showTextOverlay(getPlayerString(getCurrentPlayer()), Duration.millis(500));
            }
        }
    }

    /**
     * Bestimmt die Anzahl beweglicher Figuren des aktuellen Spielers.
     *
     * @return Gibt die Anzahl der Figuren zurück, die bewegt werden können
     */
    private int getMovableFigureCount() {
        getCurrentPlayer().enableCanMove(waypoints, bases, homes);

        return (int) Arrays.stream(getCurrentPlayer().getFigures()).filter(MadnFigureL::canMove).count();
    }

    /**
     * Wechselt den aktuellen Spieler zum nächsten, der das Spiel noch nicht beendet hat.
     *
     * <p>Die Methode überspringt alle Spieler, deren Spielstatus bereits als "finished" markiert ist.
     * Sollte kein gültiger Spieler mehr gefunden werden (was theoretisch nicht passieren darf), wird
     * nach einer bestimmten Anzahl eine {@link RuntimeException} geworfen, um Endlosschleifen zu vermeiden.</p>
     *
     * @param players Ein Array aller Spieler im Spiel (in Spielreihenfolge)
     * @throws RuntimeException falls eine unerwartete Schleifenbedingung auftritt
     */
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

    /**
     * Gibt den Namen eines Spielers basierend auf seiner ID zurück.
     *
     * @param player Der Spieler, welcher ausgewertet werden soll
     * @return Gibt die Spielerfarbe als String zurück
     */
    private String getPlayerString(MadnPlayerL player) {
        return switch (player.getPlayerID()) {
            case BLUE -> "Blau";
            case YELLOW -> "Gelb";
            case GREEN -> "Grün";
            case RED -> "Rot";
            case NONE -> "NONE";
        };
    }

    /**
     * Regelt den Spielstart nach den Startwürfen aller Spieler.
     *
     * <p>Wenn alle Spieler ihren Startwurf gemacht haben, bestimmt diese Methode:
     * <ul>
     *   <li>Welcher Spieler das Spiel beginnt (höchster Wurf)</li>
     *   <li>Ob ein Stechen bei Gleichstand nötig ist</li>
     *   <li>Wer als nächstes würfeln darf</li>
     * </ul>
     * Das Ergebnis wird über ein Overlay dem Spieler mitgeteilt, und anschließend wird entweder
     * das Spiel gestartet oder das Stechen fortgeführt.</p>
     */
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

    /**
     * Erzeugt einen Infotext für Spieler, die einen gleich hohen Startwurf hatten.
     *
     * @return Gibt den Infotext als String zurück
     */
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

    /**
     * Ermittelt den Spieler mit dem höchsten Startwurf.
     *
     * @return Gibt den Spieler mit dem höchsten Startwurf als Array zurück.
     */
    private MadnPlayerL[] getHighestRoll() {
        MadnPlayerL[] players = Arrays.stream(activePlayers).sorted(Comparator.comparingInt(MadnPlayerL::getLastRoll).reversed()).toArray(MadnPlayerL[]::new);
        return Arrays.stream(players).filter(player -> player.getLastRoll() == players[0].getLastRoll()).toArray(MadnPlayerL[]::new);
    }

    /**
     * Wird aufgerufen, wenn eine Spielfigur angeklickt wird.
     * Diese Methode verarbeitet den Spielzug der angeklickten Figur
     * abhängig von ihrer aktuellen Position (BASE, WAYPOINTS, HOME)
     * und dem letzten Würfelergebnis des Spielers.
     *
     * <p>Die Methode überprüft die Zugmöglichkeiten und bewegt
     * die Figur ggf. auf das Spielfeld, in den HOME-Bereich oder
     * setzt sie zurück in die BASE (bei Schlagzug).</p>
     *
     * @param mouseEvent das MouseEvent, das durch den Klick auf eine Figur ausgelöst wurde
     */
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

    /**
     * Behandelt das Zurückwerfen einer gegnerischen Figur auf das BASE-Feld.
     *
     * <p>Falls {@code backToBase} gesetzt ist, wird die betroffene Figur an ihre ursprüngliche
     * Position in der BASE zurückgesetzt und eine entsprechende Overlay-Nachricht angezeigt.
     * Ist keine Figur zurückzuwerfen, wird direkt die nächste Spielaktion eingeleitet.</p>
     */
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

    /**
     * Führt die Spiellogik nach dem Abschluss von Figuren-Animationen aus.
     *
     * <p>Diese Methode prüft:
     * <ul>
     *     <li>Ob ein Spieler das Spiel abgeschlossen hat (alle Figuren im HOME-Bereich)</li>
     *     <li>Ob das Spiel beendet ist (alle bis auf einen Spieler fertig)</li>
     *     <li>Ob der nächste Spieler am Zug ist oder der aktuelle Spieler erneut würfeln darf</li>
     * </ul>
     * Falls das Spiel beendet ist, wird zur Endansicht gewechselt.</p>
     */
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
                infoText.showTextOverlay(getPlayerString(getCurrentPlayer()) + " ist fertig!", Duration.millis(750));

            } else if (rollCount >= MAX_ROLL_COUNT || getCurrentPlayer().getLastRoll() != 6 || getCurrentPlayer().isFinished()) {
                nextPlayerInfoAndSwitch();

            } else {
                gamePhase.setValue(MadnGamePhase.DICE_ROLL);
                dice.setEnabled(true);
            }
        }
    }

    /**
     * Setzt den nächsten Spieler als aktiv, zeigt dessen Namen im Overlay an
     * und aktiviert anschließend den Würfel.
     *
     * <p>Wird typischerweise nach dem Abschluss eines Zuges oder nach einem ungültigen Zug aufgerufen.</p>
     */
    private void nextPlayerInfoAndSwitch() {
        rollCount = 0;

        switchPlayer(playerList);

        infoText.setOnFinished(event -> {
            gamePhase.setValue(MadnGamePhase.DICE_ROLL);
            dice.setEnabled(true);
        });

        infoText.showTextOverlay(getPlayerString(getCurrentPlayer()), Duration.millis(500));
    }

    /**
     * Markiert den aktuellen Spieler als „fertig“ und weist ihm die nächste Platzierungsposition zu.
     *
     * <p>Die Methode erhöht den Zähler {@code finishedPlayers} und
     * speichert die Position, an welcher der Spieler das Spiel abgeschlossen hat.</p>
     */
    private void setPlayerFinishPos() {
        finishedPlayers++;
        currentPlayer.getValue().setFinishedPos(finishedPlayers);
    }

    /**
     * Erstellt ein Datenpaket (Map), das an die Startansicht oder Endansicht übergeben werden kann.
     *
     * <p>Enthält u.a. die Spielerinformationen im Array {@code playerObjectArray}.</p>
     *
     * @return eine Map mit Spielerinformationen
     */
    private Map<String, Object> createDataPacket() {
        Map<String, Object> map = new HashMap<>(playersInGame);
        map.put("playerObjectArray", playerList);

        return map;
    }

    /**
     * Gibt die vollständige Spielerliste zurück.
     *
     * @return Array aller Spieler im Spiel
     */
    public MadnPlayerL[] getPlayerList() {
        return playerList;
    }

    /**
     * Gibt den aktuell aktiven Spieler zurück.
     *
     * @return das {@link MadnPlayerL}-Objekt des aktuellen Spielers
     */
    public MadnPlayerL getCurrentPlayer() {
        return currentPlayer.getValue();
    }

    /**
     * Setzt die aktuelle Spielphase.
     *
     * @param phase die neue Spielphase
     */
    public void setGamePhase(MadnGamePhase phase) {
        gamePhase.setValue(phase);
    }

    /**
     * Gibt die observable Property für den aktuellen Spieler zurück.
     *
     * @return ObservableValue des aktuellen Spielers
     */
    public ObservableValue<MadnPlayerL> currentPlayerObservable() {
        return currentPlayer;
    }

    /**
     * Gibt die observable Property für das Klick-Event auf Figuren zurück.
     *
     * <p>Diese Property kann von der View gebunden werden, um auf Spielfigur-Klicks zu reagieren.</p>
     *
     * @return ObservableValue für den {@code EventHandler<MouseEvent>}
     */
    public ObservableValue<EventHandler<MouseEvent>> figureClickedObservable() {
        return figureClicked;
    }

    /**
     * Gibt die Property der aktuellen Spielphase zurück.
     *
     * <p>Nützlich zum Binden oder Beobachten durch die UI.</p>
     *
     * @return ObjectProperty der {@link MadnGamePhase}
     */
    public ObjectProperty<MadnGamePhase> gamePhaseProperty() {
        return gamePhase;
    }

}