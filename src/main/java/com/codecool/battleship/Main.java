package com.codecool.battleship;

import com.codecool.battleship.connection.BattleshipClient;
import com.codecool.battleship.connection.BattleshipServer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args){
        setupGlobals(args);
        startServer(args);

        if (args.length > 3) if (args[3].equals("true")) clientTest();

        launch();
    }

    private static void setupGlobals(String[] args) {
        if (args.length > 0) Globals.LOCAL_PORT = Integer.parseInt(args[0]);
        if (args.length > 1) Globals.REMOTE_ADDRESS = args[1];
        if (args.length > 2)Globals.REMOTE_PORT = Integer.parseInt(args[2]);
    }

    private static void clientTest() {
        BattleshipClient client = BattleshipClient.getInstance();
        client.setServerPort(Globals.REMOTE_PORT);
        client.setServerAddress("localhost");
        client.start();
        BattleshipServer server = BattleshipServer.getInstance();
        server.sendCommand("Do my bidding");
    }

    public void start(Stage primaryStage) {
        Game game = new Game();
        Globals.game = game;
        Scene scene = new Scene(game, Globals.WINDOW_WIDTH,Globals.WINDOW_HEIGHT);
        game.setupEventHandlers();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static void startServer(String[] args) {
        BattleshipServer server = BattleshipServer.getInstance();
        int port = Globals.LOCAL_PORT;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        server.setPort(port);
        server.start();
    }
}
