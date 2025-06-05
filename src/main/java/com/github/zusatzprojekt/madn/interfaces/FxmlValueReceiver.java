package com.github.zusatzprojekt.madn.interfaces;

import java.util.Map;

/**
 * Interface für Klassen, die Werte aus einer FXML-Datei oder
 * einem Controller empfangen und verarbeiten können.
 */
public interface FxmlValueReceiver {
    /**
     * Empfängt eine Map mit Schlüssel-Wert-Paaren.
     *
     * @param values Eine Map mit String-Schlüsseln und beliebigen Objekten als Werten,
     *               die von der aufrufenden Stelle übergeben werden.
     */
    void receiveValues(Map<String, Object> values);
}
