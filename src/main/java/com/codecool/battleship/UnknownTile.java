package com.codecool.battleship;

import javafx.scene.paint.Color;

public class UnknownTile extends Tile {
    TileStatus status = TileStatus.UNKNOWN;

    UnknownTile() {
        super(Color.GREY);
    }

    void reveal(TileStatus status) {
        this.status = status;
    }
}
