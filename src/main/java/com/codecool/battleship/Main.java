package com.codecool.battleship;

import com.codecool.battleship.connection.BattleshipClient;
import com.codecool.battleship.connection.BattleshipServer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    final static int DEFAULT_SERVER_PORT = 55655;

    public static void main(String[] args){
        startServer(args);
        launch();
    }

    public void start(Stage primaryStage) {
        Game game = new Game();
        Scene scene = new Scene(game, 800,600);
        primaryStage.setScene(scene);
        primaryStage.show();
        Globals.game = game;
    }

    private static void startServer(String[] args) {
        BattleshipServer server = BattleshipServer.getInstance();
        int port = DEFAULT_SERVER_PORT;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        server.setPort(port);
        server.start();
    }
}
