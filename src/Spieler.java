import java.io.Console;

public class Spieler {
    private Wuerfel wuerfel;
    private Spielfeld spielfeld;

    public Spieler() {
        wuerfel = new Wuerfel();
        spielfeld = new Spielfeld();
    }

    public void move() {
        //int roll = wuerfel.wuerfeln();
        int roll = 1; //testen
        int[] result = spielfeld.get_positions("red");
        int werfen_target = spielfeld.pruefe_werfen("red", roll);
        if (werfen_target != 0) {
            spielfeld.werfen(werfen_target, roll);
        }
    }
}
