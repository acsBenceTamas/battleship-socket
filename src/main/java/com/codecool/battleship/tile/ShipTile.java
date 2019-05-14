package com.codecool.battleship.tile;

import com.codecool.battleship.Ship;

public class ShipTile extends PlayerTile {
    private Ship ship;

    public ShipTile(int x, int y, Ship ship) {
        super(x, y, TileStatus.INTACT);
        this.ship = ship;
    }

    public void hit() {
        setStatus(TileStatus.HIT);
        ship.checkIfShipSunk();
    }

    public void sunk() {
        setStatus(TileStatus.SUNK);
    }
}
