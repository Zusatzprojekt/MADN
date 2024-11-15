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
//    private int augenanzahl;
    private final Random rand = new Random();

    /**
     * Konstruktor für Objekte der Klasse Würfel
     */
    public Wuerfel()
    {
        // Instanzvariable initialisieren
    }

    public int wuerfeln()
    {
        // Gibt eine Zahl zwischen 1 und 6 zurück
        return rand.nextInt(1, 7);
    }
}