package com.codecool.battleship;

public class ShipPart extends GridElement{
    private Ship ship;

    ShipPart(Ship ship) {
        super(TileStatus.INTACT);
        this.ship = ship;
    }

    void hit() {

    }
}
