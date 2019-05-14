package com.codecool.battleship;

import com.codecool.battleship.tile.ShipTile;

import java.util.ArrayList;
import java.util.List;

public class Ship {
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

    public void checkIfShipSunk() {
        intactParts--;
        if(countIntactParts() == 0){
            for (ShipTile shipParts:shipTiles) {
                shipParts.sunk();
            }
        }
    }
}