import java.io.Console;
import java.util.Scanner;

public class Spieler {
    private Wuerfel wuerfel;
    private Spielfeld spielfeld;
    private Scanner scanner;

    public Spieler() {
        wuerfel = new Wuerfel();
        spielfeld = new Spielfeld();
        scanner = new Scanner(System.in);
    }

    public void move() {
        //int roll = wuerfel.wuerfeln();
        int roll = 1; //testen
        int[] positionen = spielfeld.get_positions("red");
        System.out.println("Welche Figur soll bewegt werden, gebe bitte das jeweilige Feld an");
        int auswahl = scanner.nextInt();
        int figur=0;
        for(int position: positionen){
            if(position == auswahl){
                figur = auswahl;
            }
        }
        //int index_figur =
        int werfen_target = spielfeld.pruefe_werfen("red",figur, roll);
        if (werfen_target != 0) {
            spielfeld.werfen(werfen_target, roll);
        }
    }
}
