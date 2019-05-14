package com.codecool.battleship.Tile;

public class UnknownTile extends Tile {
    TileStatus status = TileStatus.UNKNOWN;

    public UnknownTile(int x, int y) {
        super(x, y, TileStatus.UNKNOWN);
    }

    public void reveal(TileStatus status) {
        this.status = status;
    }
}
