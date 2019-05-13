package com.codecool.battleship;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args){
        launch();

    }

    public void start(Stage primaryStage) {
        Game game = new Game();
        Globals.game = game;
        Scene scene = new Scene(game, 800,600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
