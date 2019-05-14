package com.codecool.battleship;

public class Globals {
    public static Game game;
    public static int placementDirection = 0;
    public static GameState gameState = GameState.PLACEMENT;

    public static void changeDirection() {
        placementDirection = (placementDirection+1) % Direction.values().length;
    }

    public static Direction getPlacementDirection() {
        return Direction.values()[placementDirection];
    }
}
