package com.codecool.battleship;

import javafx.scene.paint.Color;

public class ShipPart extends GridElement{
    private Ship ship;

    ShipPart(Ship ship) {
        super(Color.GREY);
        this.ship = ship;
    }

    void hit() {

    }
}
