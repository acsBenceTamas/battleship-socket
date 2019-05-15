package com.codecool.battleship.tile;

import com.codecool.battleship.Ship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShipTile extends PlayerTile {
    private static final Logger logger = LoggerFactory.getLogger(ShipTile.class);
    private Ship ship;

    public ShipTile(int x, int y, Ship ship) {
        super(x, y, TileStatus.INTACT);
        this.ship = ship;
    }

    public void hit() {
        logger.debug(this + " has been hit");
        setStatus(TileStatus.HIT);
        if(ship.isShipSunk()) {
            ship.sinkShip();
            ship.sendSunkenShipData();
        } else {
            sendAttackResponse();
        }
    }

    public void sunk() {
        setStatus(TileStatus.SUNK);
    }
}
