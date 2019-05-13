package com.codecool.battleship;

public class WaterTile extends GridElement {
    WaterTile() {
        super(TileStatus.WATER);
    }

    void hit() {
        status = TileStatus.WATER;
    }
}
