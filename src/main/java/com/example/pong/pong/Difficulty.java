package com.example.pong.pong;

public enum Difficulty {
    EASY(0.3),
    MEDIUM(0.7),
    HARD(1.0);

    private final double speedMultiplier;

    Difficulty(double speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }
}
