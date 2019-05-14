package com.codecool.battleship.tile;

public class WaterTile extends PlayerTile {
    public WaterTile(int x, int y) {
        super(x, y, TileStatus.WATER);
    }

    public void hit() {
        setStatus(TileStatus.MISS);
    }
}
