package com.codecool.battleship;

import javafx.scene.paint.Color;

public abstract class GridElement extends Tile {
    TileStatus status = TileStatus.INTACT;

    GridElement(Color color) {
        super(color);
    }

    abstract void hit();
}
