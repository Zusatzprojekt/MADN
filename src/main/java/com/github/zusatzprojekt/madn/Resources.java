package com.github.zusatzprojekt.madn;

import java.io.InputStream;
import java.net.URL;

import static java.util.Objects.requireNonNull;

public class Resources {
    public static URL getURL(String resourceName) {
        return requireNonNull(Resources.class.getResource(resourceName));
    }

    public static InputStream getStream(String resourceName) {
        return requireNonNull(Resources.class.getResourceAsStream(resourceName));
    }
}
