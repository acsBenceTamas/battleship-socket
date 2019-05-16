package com.codecool.battleship.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public enum SoundEffect {
    MISS (new Media("sounds/water.mp3")),
    HIT (new Media("sounds/explosion.mp3"));

    public MediaPlayer mediaPlayer;

    SoundEffect(Media media) {
        mediaPlayer = new MediaPlayer(media);
    }
}
