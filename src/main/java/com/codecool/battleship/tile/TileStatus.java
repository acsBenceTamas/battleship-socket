package com.codecool.battleship.Tile;

import javafx.scene.paint.Color;

public enum TileStatus {
    INTACT(Color.GREY),
    HIT(Color.YELLOW),
    SUNK(Color.RED),
    WATER(Color.BLUE),
    MISS(Color.DARKBLUE),
    UNKNOWN(Color.DARKGREY);

    public Color color;

    TileStatus(Color color) {
        this.color = color;
    }
}
