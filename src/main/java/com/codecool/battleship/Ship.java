package com.codecool.battleship;

import com.codecool.battleship.tile.ShipTile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private static final Logger logger = LoggerFactory.getLogger(Ship.class);
    List<ShipTile> shipTiles = new ArrayList<ShipTile>();
    private Direction direction;
    private int length;
    private int intactParts;
    
    public Ship(int x, int y, int length, Direction direction) {
        this.length = length;
        this.intactParts = length;
        this.direction = direction;
        for(int i = 0; i < length; i++){
            shipTiles.add(new ShipTile(x+direction.x*i,y+direction.y*i,this));
        }
        Globals.game.addShipToGrid(this);
    }
    
    private int countIntactParts() {
        return intactParts;
    }

    public boolean isShipSunk() {
        intactParts--;
        logger.debug("Intact parts left: "+intactParts);
        return countIntactParts() == 0;
    }

    public void sendSunkenShipData() {
        for (ShipTile shipPart:shipTiles) {
            shipPart.sendAttackResponse();
        }
    }

    public void sinkShip() {
        for (ShipTile shipParts:shipTiles) {
            shipParts.sunk();
        }
    }
}