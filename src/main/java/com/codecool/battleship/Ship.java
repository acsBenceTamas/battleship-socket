package com.codecool.battleship;

import com.codecool.battleship.Tile.ShipTile;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    List<ShipTile> shipTiles = new ArrayList<ShipTile>();
    Direction direction;
    int length;
    
    public Ship(int x, int y, int length, Direction direction) {
        this.length = length;
        this.direction = direction;
        for(int i = 0; i < length; i++){
            shipTiles.add(new ShipTile(x+direction.x*i,y+direction.y*i,this));
        }
        Globals.game.addShipToGrid(this);
    }
    
    int countIntactParts() {
        return 0;
    }
}