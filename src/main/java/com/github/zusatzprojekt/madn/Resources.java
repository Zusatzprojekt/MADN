package com.github.zusatzprojekt.madn;

import java.io.InputStream;
import java.net.URL;

import java.util.Objects;

/**
 * Hilfsklasse zum Laden von Ressourcen aus dem Klassenpfad.
 */
public class Resources {
    /**
     * Gibt die URL zu einer Ressource zurück, die sich im Klassenpfad befindet.
     *
     * @param resourceName          Der Pfad zur Ressource relativ zum Klassenpfad.
     * @return                      Die URL zur angegebenen Ressource.
     * @throws NullPointerException Wenn die Ressource nicht gefunden wird.
     */
    public static URL getURL(String resourceName) {
        return Objects.requireNonNull(Resources.class.getResource(resourceName));
    }

    /**
     * Gibt einen InputStream zu einer Ressource zurück, die sich im Klassenpfad befindet.
     * Dieser Stream kann verwendet werden, um den Inhalt der Ressource zu lesen, z.B. bei Bildern.
     *
     * @param resourceName          Der Pfad zur Ressource relativ zum Klassenpfad.
     * @return                      Ein InputStream der Ressource.
     * @throws NullPointerException Wenn die Ressource nicht gefunden wird.
     */
    public static InputStream getStream(String resourceName) {
        return Objects.requireNonNull(Resources.class.getResourceAsStream(resourceName));
    }
}
