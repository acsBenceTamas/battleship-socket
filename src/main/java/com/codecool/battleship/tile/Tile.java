package com.codecool.battleship.tile;

import com.codecool.battleship.Globals;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Tile extends Rectangle {
    private static final Logger logger = LoggerFactory.getLogger(Tile.class);
    private final int x;
    private final int y;
    private TileStatus status;

    private Tile(int x, int y, Image image) {
        this.x = x;
        this.y = y;
        setWidth(Globals.TILE_WIDTH);
        setHeight(Globals.TILE_HEIGHT);
        setFill(new ImagePattern(image));
        setStroke(Color.BLACK);
        logger.trace("Tile created at: " + x + "-" + y + " with color " + image);
    }

    Tile(int x, int y, TileStatus status) {
        this(x, y, status.image);
        this.status = status;
    }

    public TileStatus getStatus() {
        return status;
    }

    void setStatus(TileStatus status) {
        this.status = status;
        setFill(new ImagePattern(status.image));
    }

    public int getGridX() {
        return x;
    }
    public int getGridY() {
        return y;
    }
}
