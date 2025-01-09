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

    public void move(String farbe) {
        //int roll = wuerfel.wuerfeln();
        int roll = 1; //testen
        int[] positionen = spielfeld.get_positions("red");
        //int index_figur =
        int werfen_target = spielfeld.pruefe_werfen("red", roll);
        if (werfen_target != 0) {
            spielfeld.werfen(werfen_target, roll);
        } else{
            System.out.println("Welche Figur soll bewegt werden, gebe bitte das jeweilige Feld an");
            int auswahl = scanner.nextInt();
            int figur=0;
            for(int position: positionen){
                if(position == auswahl){
                    figur = auswahl;
                    int[] arrayToUpdate = spielfeld.get_positions("red");
                    int figur_index = spielfeld.findIndex(arrayToUpdate, figur);

                    // Modify the array
                    arrayToUpdate[figur_index] = auswahl + roll;

                    // Put the modified array back into the map
                    spielfeld.get_positions().put(farbe, arrayToUpdate);
                }
            }

        }
    }
}
