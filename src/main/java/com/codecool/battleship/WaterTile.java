package com.codecool.battleship;

import javafx.scene.paint.Color;

public class WaterTile extends GridElement {
    WaterTile() {
        super(Color.BLUE);
    }

    void hit() {
        status = TileStatus.MISS;
    }
}
