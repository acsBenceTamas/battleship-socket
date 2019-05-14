package com.codecool.battleship.Tile;

import com.codecool.battleship.GameState;
import com.codecool.battleship.Globals;
import com.codecool.battleship.Ship;
import com.codecool.battleship.ShipLayout;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class UnknownTile extends Tile {
    TileStatus status = TileStatus.UNKNOWN;

    public UnknownTile(int x, int y) {
        super(x, y, TileStatus.UNKNOWN);
    }

    public void reveal(TileStatus status) {
        this.status = status;
    }


    private EventHandler<MouseEvent> onMouseClickHandler = e -> {
        if(Globals.gameState == GameState.PLACEMENT){
            ShipLayout shipLayout = Globals.game.getShipLayout();
            if(shipLayout != null){
                if(e.getButton() == MouseButton.SECONDARY){
                    Globals.changeDirection();
                    Globals.game.shipPlacementMarkerRemove();
                    Globals.game.shipPlacementMarker(getGridX(), getGridY());
                } else if(e.getButton() == MouseButton.PRIMARY && Globals.game.isValidPlacement(getGridX(), getGridY(), shipLayout.getLength())) {
                    Globals.game.removeShipLayout();
                    Globals.game.addPlayerShip(new Ship(getGridX(), getGridY(), shipLayout.getLength(), Globals.getPlacementDirection()));
                }
            }
        }
    };

    private EventHandler<MouseEvent> onMouseEnterHandler = e -> {
        if(Globals.gameState == GameState.PLACEMENT){
            Globals.game.shipPlacementMarker(getGridX(), getGridY());
        }
    };

    private EventHandler<MouseEvent> onMouseLeaveHandler = e -> {
        if(Globals.gameState == GameState.PLACEMENT){
            Globals.game.shipPlacementMarkerRemove();
        }
    };
}
