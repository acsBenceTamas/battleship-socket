package com.codecool.battleship;

import com.codecool.battleship.Tile.*;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Game extends Pane {
    private static int GRID_SIZE = 10;
    private PlayerTile[][] playerGrid = new PlayerTile[GRID_SIZE][GRID_SIZE];
    private UnknownTile[][] enemyGrid = new UnknownTile[GRID_SIZE][GRID_SIZE];
    private Stack<ShipLayout> shipLayouts = new Stack<ShipLayout>();
    private List<Ship> playerShips = new ArrayList<>();

    Game() {
        addShipLayouts();
        fillWater();
        drawPlayerGrid();
        fillEnemyGrid();
        drawEnemyGrid();
    }

    public void addPlayerShip(Ship ship) {
        playerShips.add(ship);
    }

    void addShipLayouts() {
        shipLayouts.push(new ShipLayout(2));
        shipLayouts.push(new ShipLayout(2));
        shipLayouts.push(new ShipLayout(3));
        shipLayouts.push(new ShipLayout(3));
        shipLayouts.push(new ShipLayout(4));
    }

    public void resolveEnemyTurn() {
        
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

    void fillEnemyGrid() {
        for (int x = 0; x<10; x++) {
            for (int y = 0; y<10; y++) {
                UnknownTile tile = new UnknownTile(x, y);
                tile.setX(enemyGridInitialPosition() + x * Globals.TILE_WIDTH);
                tile.setY(y * Globals.TILE_HEIGHT);
                enemyGrid[x][y] = tile;
            }
        }
    }

    double enemyGridInitialPosition() {
        double tilewidth = 40;
        return Globals.WINDOW_WIDTH - GRID_SIZE * tilewidth;
    }

    void fillWater() {
        for (int x = 0; x<10; x++) {
            for (int y = 0; y<10; y++) {
                PlayerTile tile = new WaterTile(x,y);
                tile.setX(x * Globals.TILE_WIDTH);
                tile.setY(y * Globals.TILE_HEIGHT);
                playerGrid[x][y] = tile;
            }
        }
    }

    public void addShipPart(ShipTile shipTile) {
        int x = shipTile.getGridX();
        int y = shipTile.getGridY();
        getChildren().remove(playerGrid[x][y]);
        shipTile.setX(x * Globals.TILE_WIDTH);
        shipTile.setY(y * Globals.TILE_HEIGHT);
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
        for (int i = 0; i<10; i++) {
            for (int j = 0; j<10; j++) {
                getChildren().add(playerGrid[i][j]);
            }
        }
    }

    void drawEnemyGrid() {
        for (int i = 0; i<10; i++) {
            for (int j = 0; j<10; j++) {
                getChildren().add(enemyGrid[i][j]);
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

    void setupEventHandlers() {
        this.getScene().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getText().equals("a")){
                    for (int i = 0; i<10; i++) {
                        for (int j = 0; j<10; j++) {
                            playerGrid[i][j].hit();
                        }
                    }
                    System.out.println("a keypress");
                }
                if (event.getText().equals("b")){
                    playerGrid[0][0].hit();
                    playerGrid[0][1].hit();
                    System.out.println("b keypress");
                }
            }
        });
    }
}
