package com.github.zusatzprojekt.madn.logic;

public class Game {
    private final Player[] players;
    private Player currentPlayer;
    private final Dice dice;

    public Game(Player[] players) {
        this.players = players;
        currentPlayer = this.players[0];
        dice = new Dice();
    }

    public void mainloop(){
        // TODO: Implementierung
        System.out.println(players);
        System.out.println(currentPlayer);
        System.out.println(dice);
    }
}
