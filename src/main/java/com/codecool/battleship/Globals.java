package com.codecool.battleship;

public class Globals {
    public static Game game;
    public static int placementDirection = 0;
    public static GameState gameState = GameState.PLACEMENT;
    public static final double WINDOW_WIDTH = 1280;
    public static final double WINDOW_HEIGHT = 720;
    public static final double TILE_WIDTH = 60;
    public static final double TILE_HEIGHT = 60;
    public final static int DEFAULT_SERVER_PORT = 55655;
    public static int LOCAL_PORT = DEFAULT_SERVER_PORT;
    public static int REMOTE_PORT = DEFAULT_SERVER_PORT;
    public static String REMOTE_ADDRESS = "localhost";
    public static boolean CLIENT_CONNECTED = false;
    public static boolean GAME_IS_RUNNING = true;

    public static void changeDirection() {
        placementDirection = (placementDirection+1) % Direction.values().length;
    }

    public static Direction getPlacementDirection() {
        return Direction.values()[placementDirection];
    }
}
