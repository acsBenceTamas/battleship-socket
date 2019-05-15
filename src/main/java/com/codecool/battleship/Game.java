package com.codecool.battleship;

import com.codecool.battleship.connection.BattleshipClient;
import com.codecool.battleship.connection.BattleshipServer;
import com.codecool.battleship.tile.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Game extends Pane {
    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    private static int GRID_SIZE = 10;
    private PlayerTile[][] playerGrid = new PlayerTile[GRID_SIZE][GRID_SIZE];
    private UnknownTile[][] enemyGrid = new UnknownTile[GRID_SIZE][GRID_SIZE];
    private Stack<ShipLayout> shipLayouts = new Stack<>();
    private List<Ship> playerShips = new ArrayList<>();
    public boolean startingPlayer = false;
    private boolean enemyReady = false;

    Game() {
        startServer();
    }

    void mainMenu() {
        logger.trace("Initiating Main Menu");

        final TextField ip = new TextField();
        ip.setPromptText("Enter ip.");
        ip.setFont(Font.font(68));
        ip.setMinWidth(800);
        ip.setMaxWidth(800);
        ip.getText();
        ip.setAlignment(Pos.CENTER);
        ip.setLayoutX(250);
        ip.setLayoutY(250);
        getChildren().add(ip);

        Button submit = new Button("Play");
        submit.setFont(Font.font(60));
        submit.setPrefHeight(100);
        submit.setPrefWidth(300);
        submit.setLayoutX(500);
        submit.setLayoutY(420);
        getChildren().add(submit);

        EventHandler<MouseEvent> onStartMouseHandler = e -> {
            setupConnection(ip);
        };
        EventHandler<KeyEvent> onStartKeyHandler = e -> {
            if(e.getCode() == KeyCode.ENTER)
                setupConnection(ip);
        };

        submit.setOnMouseClicked(onStartMouseHandler);
        ip.setOnKeyPressed(onStartKeyHandler);

        logger.trace("Done Initiating Main Menu");
    }

    private void setupConnection(TextField ip) {
        System.out.println(ip.getCharacters());
        String[] address = ip.getCharacters().toString().split(":");
        if (address.length == 1) {
            createConnection(address[0], String.valueOf(Globals.DEFAULT_SERVER_PORT));
        } else {
            createConnection(address[0], address[1]);
        }
    }

    private void createConnection(String ip, String port) {
        logger.trace("Creating client side connection");

        BattleshipClient client = BattleshipClient.getInstance();
        client.setServerAddress(ip);
        client.setServerPort(Integer.parseInt(port));
        client.start();

        logger.trace("Done Creating client side connection");
    }

    private boolean isGameOver() {
        for (Ship playerShip:playerShips) {
            if(!playerShip.isShipSunk()){
                return false;
            }
        }
        return true;
    }

    private void startServer() {
        logger.trace("Initiating Server side connection");

        BattleshipServer server = BattleshipServer.getInstance();
        server.setPort(Globals.LOCAL_PORT);
        server.start();

        logger.trace("Server-side connection running");
    }

    public void setStartingPlayer() {
        startingPlayer = true;
    }

    public void setEnemyReady() {
        enemyReady = true;
    }

    public void startGame() {
        logger.trace("Initiating game startup");

        clearScreen();
        addShipLayouts();
        fillWater();
        drawPlayerGrid();
        fillEnemyGrid();
        drawEnemyGrid();

        logger.trace("Done Initiating game startup");
    }

    public void addPlayerShip(Ship ship) {
        logger.debug("Adding player ship: " + ship);
        playerShips.add(ship);
    }

    private void addShipLayouts() {
        logger.trace("Preparing ship layouts");

        shipLayouts.push(new ShipLayout(2));
        shipLayouts.push(new ShipLayout(2));
        shipLayouts.push(new ShipLayout(3));
        shipLayouts.push(new ShipLayout(3));
        shipLayouts.push(new ShipLayout(4));


        logger.trace("Done Preparing ship layouts");
    }

    public void displayGameOver(boolean won) {
        Text text = new Text(Globals.WINDOW_WIDTH/2-100,Globals.WINDOW_HEIGHT/2-36,"Placeholder");
        if(won){
            text.setText("Won");
        } else {
            text.setText("Lost");
        }
        text.setWrappingWidth(200);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(Font.font(72));
        getChildren().add(text);
    }

    public void resolveEnemyTurn(String[] enemyAction) {
        logger.trace("Resolving Enemy Turn");
        int x = Integer.parseInt(enemyAction[1]);
        int y = Integer.parseInt(enemyAction[2]);
        playerGrid[x][y].hit();
        if(isGameOver()){
            handleGameOver(false);
            BattleshipServer.getInstance().sendCommand("GAME_WON");
            return;
        }
        Globals.gameState = GameState.PLAYER_TURN;
//        BattleshipServer.getInstance().sendCommand("ATTACK_RESPONSE "+x+" "+y+" "+playerGrid[x][y].getStatus());
        logger.trace("Done Resolving Enemy Turn");
    }

    public void markEnemyTile(int x, int y, TileStatus status){
        enemyGrid[x][y].reveal(status);
    }

    public ShipLayout getShipLayout() {
        logger.trace("Getting top ship layout");

        if (!shipLayouts.empty()) {
            return shipLayouts.peek();
        }
        return null;
    }

    public void removeShipLayout() {
        logger.trace("Removing top ship layout");

        shipLayouts.pop();
        if(shipLayouts.empty()) {
            logger.debug("Player finished placement");
            if(enemyReady){
                logger.debug("Enemy player is already ready");
                if(startingPlayer){
                    Globals.gameState = GameState.PLAYER_TURN;
                } else {
                    Globals.gameState = GameState.ENEMY_TURN;
                }
            } else {
                logger.debug("Enemy player is not finished");
                Globals.gameState = GameState.PLACEMENT_FINISHED;
            }
            Platform.runLater(() -> {
                BattleshipServer.getInstance().sendCommand("PLACEMENT_FINISHED");
            });
        }
    }

    private void fillEnemyGrid() {
        logger.trace("Filling Enemy Grid");

        for (int x = 0; x<10; x++) {
            for (int y = 0; y<10; y++) {
                UnknownTile tile = new UnknownTile(x, y);
                tile.setX(enemyGridInitialPosition() + x * Globals.TILE_WIDTH);
                tile.setY(y * Globals.TILE_HEIGHT);
                enemyGrid[x][y] = tile;
            }
        }

        logger.trace("Done Filling Enemy Grid");
    }

    private double enemyGridInitialPosition() {
        double tilewidth = Globals.TILE_WIDTH;
        return Globals.WINDOW_WIDTH - GRID_SIZE * tilewidth;
    }

    private void fillWater() {
        logger.trace("Filling Player Grid with Water Tiles");

        for (int x = 0; x<10; x++) {
            for (int y = 0; y<10; y++) {
                PlayerTile tile = new WaterTile(x,y);
                tile.setX(x * Globals.TILE_WIDTH);
                tile.setY(y * Globals.TILE_HEIGHT);
                playerGrid[x][y] = tile;
            }
        }

        logger.trace("Done Filling Player Grid with Water Tiles");
    }

    private void addShipPart(ShipTile shipTile) {
        logger.trace("Adding ship part " + shipTile + " to the grid");
        int x = shipTile.getGridX();
        int y = shipTile.getGridY();
        getChildren().remove(playerGrid[x][y]);
        shipTile.setX(x * Globals.TILE_WIDTH);
        shipTile.setY(y * Globals.TILE_HEIGHT);
        playerGrid[x][y] = shipTile;
        getChildren().add(shipTile);
    }

    void addShipToGrid(Ship ship) {
        logger.debug("Adding ship to grid: " + ship);
        for (ShipTile shipTile:ship.shipTiles) {
            addShipPart(shipTile);
        }
    }

    private void clearScreen() {
        logger.trace("Clearing screen");

        getChildren().clear();

        logger.trace("Done Clearing screen");
    }

    private void drawPlayerGrid() {
        logger.trace("Drawing Player Grid");

        for (int i = 0; i<10; i++) {
            for (int j = 0; j<10; j++) {
                getChildren().add(playerGrid[i][j]);
            }
        }

        logger.trace("Done Drawing Player Grid");
    }

    private void drawEnemyGrid() {
        logger.trace("Drawing Enemy Grid");

        for (int i = 0; i<10; i++) {
            for (int j = 0; j<10; j++) {
                getChildren().add(enemyGrid[i][j]);
            }
        }

        logger.trace("Done Drawing Enemy Grid");
    }

    public void shipPlacementMarker(int x, int y) {
        logger.trace("Placing ship placement marker at " + x + "-" + y);
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
        logger.trace("Checking placement validity at " + x + "-" + y + " with length " + length + " in direction " + direction);
        int xMax = x+(length-1)*direction.x;
        int yMax = y+(length-1)*direction.y;
        if(xMax < 0 || xMax >= GRID_SIZE || yMax < 0 || yMax >= GRID_SIZE) {
            logger.trace("Can't place due to being out of bounds");
            return false;
        }
        for(int i = 0; i < length; i++){
            int xPos = x+i*direction.x;
            int yPos = y+i*direction.y;
            if(xPos >= 0 && xPos < GRID_SIZE && yPos >= 0 && yPos < GRID_SIZE) {
                if(!(playerGrid[xPos][yPos] instanceof WaterTile)) {
                    logger.trace("Can't place due hitting non-water tiles");
                    return false;
                }
            }
        }
        logger.trace("Placement is valid");
        return true;
    }

    public void shipPlacementMarkerRemove() {
        logger.trace("Removing placement markers");
        for (int i = 0; i<10; i++) {
            for (int j = 0; j<10; j++) {
                playerGrid[i][j].resetDisplay();
            }
        }
    }

    void ingameEventHandlers() {
        this.getScene().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getText().equals("a")){
                for (int i = 0; i<10; i++) {
                    for (int j = 0; j<10; j++) {
                        playerGrid[i][j].hit();
                    }
                }
                logger.debug("a keybress");
            }
            if (event.getText().equals("b")){
                playerGrid[0][0].hit();
                playerGrid[0][1].hit();
                logger.debug("b keybress");
            }
        });
    }

    public void handleGameOver(boolean won) {
        Globals.gameState = GameState.ENDED;
        displayGameOver(won);
    }
}
