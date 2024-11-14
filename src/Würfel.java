
/**
 * Beschreiben Sie hier die Klasse Würfel.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
import java.util.Random;

public class Würfel
{
    // Instanzvariablen - ersetzen Sie das folgende Beispiel mit Ihren Variablen
    private int augenanzahl;
    private Random rand = new Random();

    /**
     * Konstruktor für Objekte der Klasse Würfel
     */
    public Würfel()
    {
        // Instanzvariable initialisieren
    }

    /**
     * Ein Beispiel einer Methode - ersetzen Sie diesen Kommentar mit Ihrem eigenen
     * 
     * @param  y    ein Beispielparameter für eine Methode
     * @return        die Summe aus x und y
     */
    public int würfeln()
    {
        // tragen Sie hier den Code ein
        int roll = rand.nextInt(7);
        return roll;
    }
}
