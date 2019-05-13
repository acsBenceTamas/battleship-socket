package com.codecool.battleship;

import javafx.scene.layout.Pane;

public class Game extends Pane {
    Game() {
        for (int i = 0; i<10; i++) {
            for (int j = 0; j<10; j++) {
                Tile tile = new UnknownTile();
                tile.setX(i*40);
                tile.setY(j*40);
                getChildren().add(tile);
            }
        }
    }
}
