public class Spieler {
    private Wuerfel wuerfel;
    private Spielfeld spielfeld;
    public Spieler(){
        wuerfel = new Wuerfel();
        spielfeld = new Spielfeld();
    }
    public int move(){
        int roll = wuerfel.wuerfeln();
        return roll;
    }
}
