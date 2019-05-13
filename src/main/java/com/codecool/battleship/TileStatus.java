package com.codecool.battleship;

import javafx.scene.paint.Color;

public enum TileStatus {
    INTACT(Color.GREY),
    HIT(Color.YELLOW),
    SUNK(Color.RED),
    WATER(Color.BLUE),
    UNKNOWN(Color.DARKGREY);

    Color color;

    TileStatus(Color color) {
        this.color = color;
    }
}
