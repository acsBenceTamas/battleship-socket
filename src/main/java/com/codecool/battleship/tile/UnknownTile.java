package com.codecool.battleship.tile;

import com.codecool.battleship.GameState;
import com.codecool.battleship.Globals;
import com.codecool.battleship.Ship;
import com.codecool.battleship.ShipLayout;
import com.codecool.battleship.connection.BattleshipServer;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.scene.paint.Color;

public class UnknownTile extends Tile {
    private static final Logger logger = LoggerFactory.getLogger(UnknownTile.class);
    private TileStatus status = TileStatus.UNKNOWN;

    public UnknownTile(int x, int y) {
        super(x, y, TileStatus.UNKNOWN);
        this.setOnMouseEntered(onMouseEnterHandler);
        this.setOnMouseExited(onMouseLeaveHandler);
        this.setOnMousePressed(onMouseClickHandler);
    }

    public void reveal(TileStatus status) {
        logger.debug("Revealing " + this);
        this.status = status;
    }


    private EventHandler<MouseEvent> onMouseClickHandler = e -> {
        logger.trace("Mouse pressed on " + this);
        if(e.getButton() == MouseButton.PRIMARY && Globals.gameState == GameState.PLAYER_TURN && status == TileStatus.UNKNOWN) {
            Globals.gameState = GameState.ENEMY_TURN;
            BattleshipServer.getInstance().sendCommand("ATTACKED "+getGridX()+" "+getGridY());
        }
    };

    private EventHandler<MouseEvent> onMouseEnterHandler = e -> {
        if(Globals.gameState == GameState.PLAYER_TURN && status == TileStatus.UNKNOWN){
            setFill(Color.WHITE);
        }
    };

    private EventHandler<MouseEvent> onMouseLeaveHandler = e -> {
        if(Globals.gameState == GameState.PLAYER_TURN && status == TileStatus.UNKNOWN){
            setFill(status.color);
        }
    };
}
