package org.ilisi.backend.specs;


public class SpecUtils {
    private SpecUtils() {
    }

    public static String likePattern(String value) {
        return "%" + value + "%";
    }
}
