package com.codecool.battleship.tile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaterTile extends PlayerTile {
    private static final Logger logger = LoggerFactory.getLogger(WaterTile.class);
    public WaterTile(int x, int y) {
        super(x, y, TileStatus.WATER);
    }

    public void hit() {
        logger.debug(this + " has been hit");
        setStatus(TileStatus.MISS);
    }
}
