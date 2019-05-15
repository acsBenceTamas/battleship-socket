package com.codecool.battleship;

import com.codecool.battleship.connection.BattleshipClient;
import com.codecool.battleship.connection.BattleshipServer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args){
        setupGlobals(args);

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

    @Override
    public void stop() {
        Globals.GAME_IS_RUNNING = false;
        System.exit(0);
    }

    public void start(Stage primaryStage) {
        Game game = new Game();
        Globals.game = game;
        Scene scene = new Scene(game, Globals.WINDOW_WIDTH,Globals.WINDOW_HEIGHT);
        game.mainMenu();
        primaryStage.setTitle("Battleship game using port: "+Globals.LOCAL_PORT);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
