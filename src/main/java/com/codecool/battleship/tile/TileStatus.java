package com.codecool.battleship.tile;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public enum TileStatus {
    INTACT(Color.GREY,"images/ship_intact.png"),
    HIT(Color.YELLOW,"images/ship_hit.png"),
    SUNK(Color.RED,"images/ship_destroyed.png"),
    WATER(Color.BLUE,"images/water.png"),
    MISS(Color.DARKBLUE,"images/miss.png"),
    UNKNOWN(Color.DARKGREY,"images/water.png");

    public Color color;
    public Image image;

    TileStatus(Color color, String image) {
        this.color = color;
        this.image = new Image(image);
    }
}
