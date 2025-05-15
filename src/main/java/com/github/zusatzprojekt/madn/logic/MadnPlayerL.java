package com.github.zusatzprojekt.madn.logic;

public class MadnPlayerL {
    public enum PlayerID {BLUE, YELLOW, GREEN, RED}
    private PlayerID playerID;
    private int lastRoll = 0;

    public MadnPlayerL() {}

    public MadnPlayerL(PlayerID playerID) {
        this.playerID = playerID;
    }

    public PlayerID getPlayerID() {
        return playerID;
    }

    public int getLastRoll() {
        return lastRoll;
    }

}
