package com.codecool.battleship.Tile;

import com.codecool.battleship.GameState;
import com.codecool.battleship.Globals;
import com.codecool.battleship.Ship;
import com.codecool.battleship.ShipLayout;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class WaterTile extends GridTile {
    public WaterTile(int x, int y) {
        super(x, y, TileStatus.WATER);
    }

    void hit() {
        status = TileStatus.MISS;
    }
}
