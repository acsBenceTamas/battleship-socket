package com.codecool.battleship;

import com.codecool.battleship.Tile.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Game extends Pane {
    private static int GRID_SIZE = 10;
    private GridTile[][] playerGrid = new GridTile[GRID_SIZE][GRID_SIZE];
    private UnknownTile[][] enemyGrid = new UnknownTile[GRID_SIZE][GRID_SIZE];
    private List<Ship> ships = new ArrayList<Ship>();
    private Stack<ShipLayout> shipLayouts = new Stack<ShipLayout>();

    Game() {
        addShipLayouts();
        fillWater();
        drawPlayerGrid();
    }

    void addShipLayouts() {
        shipLayouts.push(new ShipLayout(2));
        shipLayouts.push(new ShipLayout(2));
        shipLayouts.push(new ShipLayout(3));
        shipLayouts.push(new ShipLayout(3));
        shipLayouts.push(new ShipLayout(4));
    }

    public ShipLayout getShipLayout() {
        if (!shipLayouts.empty()) {
            ShipLayout shipLayout = shipLayouts.peek();
            return shipLayout;
        }
        return null;
    }

    public void removeShipLayout() {
        shipLayouts.pop();
        if(shipLayouts.empty())
            Globals.gameState = GameState.PLAYER_TURN;
    }

    void fillWater() {
        for (int i = 0; i<10; i++) {
            for (int j = 0; j<10; j++) {
                GridTile tile = new WaterTile(i,j);
                tile.setX(i*40);
                tile.setY(j*40);
                playerGrid[i][j] = tile;
            }
        }
    }

    public void addShipPart(ShipTile shipTile) {
        int x = shipTile.getGridX();
        int y = shipTile.getGridY();
        getChildren().remove(playerGrid[x][y]);
        shipTile.setX(x*40);
        shipTile.setY(y*40);
        playerGrid[x][y] = shipTile;
        getChildren().add(shipTile);
    }

    public void addShipToGrid(Ship ship) {
        for (ShipTile shipTile:ship.shipTiles) {
            addShipPart(shipTile);
        }
    }

    void clearScreen() {
        getChildren().clear();
    }

    void drawPlayerGrid() {
        clearScreen();
        for (int i = 0; i<10; i++) {
            for (int j = 0; j<10; j++) {
                getChildren().add(playerGrid[i][j]);
            }
        }
    }

    public void shipPlacementMarker(int x, int y) {
        int length = shipLayouts.peek().getLength();
        Direction direction = Globals.getPlacementDirection();
        Color color = Color.RED;
        if(isValidPlacement(x, y, length)) {
            color = Color.GREEN;
        }
        for(int i = 0; i < length; i++){
            int xPos = x+i*direction.x;
            int yPos = y+i*direction.y;
            if(xPos >= 0 && xPos < GRID_SIZE && yPos >= 0 && yPos < GRID_SIZE) {
                playerGrid[xPos][yPos].setFill(color);
            }
        }
    }

    public boolean isValidPlacement(int x, int y, int length) {
        Direction direction = Globals.getPlacementDirection();
        int xMax = x+(length-1)*direction.x;
        int yMax = y+(length-1)*direction.y;
        if(xMax < 0 || xMax >= GRID_SIZE || yMax < 0 || yMax >= GRID_SIZE) {
            return false;
        }
        for(int i = 0; i < length; i++){
            int xPos = x+i*direction.x;
            int yPos = y+i*direction.y;
            if(xPos >= 0 && xPos < GRID_SIZE && yPos >= 0 && yPos < GRID_SIZE) {
                if(!(playerGrid[xPos][yPos] instanceof WaterTile)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void shipPlacementMarkerRemove() {
        for (int i = 0; i<10; i++) {
            for (int j = 0; j<10; j++) {
                playerGrid[i][j].setFill(playerGrid[i][j].getStatus().color);
            }
        }
    }
}
