package com.codecool.battleship.Tile;

import com.codecool.battleship.Ship;

public class ShipTile extends GridTile {
    private Ship ship;

    public ShipTile(int x, int y, Ship ship) {
        super(x, y, TileStatus.INTACT);
        this.ship = ship;
    }

    void hit() {

    }
}
