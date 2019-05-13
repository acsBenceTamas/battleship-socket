package com.codecool.battleship;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Tile extends Rectangle {
    protected TileStatus status;

    Tile(Color color) {
        setWidth(40);
        setHeight(40);
        setFill(color);
        setStroke(Color.BLACK);
    }

    Tile(TileStatus status) {
        this(status.color);
        this.status = status;
    }
}
