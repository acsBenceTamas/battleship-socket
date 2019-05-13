package com.codecool.battleship;

public abstract class GridElement extends Tile {

    GridElement(TileStatus status) {
        super(status);
    }

    abstract void hit();
}
