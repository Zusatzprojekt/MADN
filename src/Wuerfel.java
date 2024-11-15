
/**
 * Beschreiben Sie hier die Klasse Würfel.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
import java.util.Random;

public class Wuerfel
{
    // Instanzvariablen - ersetzen Sie das folgende Beispiel mit Ihren Variablen
    private int augenanzahl;
    private Random rand = new Random();

    /**
     * Konstruktor für Objekte der Klasse Würfel
     */
    public Wuerfel()
    {
        // Instanzvariable initialisieren
    }

    public int wuerfeln()
    {
        // tragen Sie hier den Code ein
        int roll = rand.nextInt(1, 7);
        return roll;
    }
}
