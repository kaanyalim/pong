package com.example.pong.pong.util;


public final class Args {
    private Args() {
        throw new AssertionError("No net.toiviainen.pong.util.Args instances for you!");
    }

    public static <T> T notNull(T object, String message) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
        return object;
    }

    public static int isLte(int value, int limit, String message) throws IllegalArgumentException {
        if (value > limit) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static int isGte(int value, int limit, String message) throws IllegalArgumentException {
        if (value < limit) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static double isGte(double value, double limit, String message) throws IllegalArgumentException {
        if (value < limit) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static int isBetween(int value, int min, int max, String message) throws IllegalArgumentException {
        if (value < min || value > max) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }
}
