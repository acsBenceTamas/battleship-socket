package com.codecool.battleship.connection;

import com.codecool.battleship.GameState;
import com.codecool.battleship.Globals;
import javafx.application.Platform;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class BattleshipClient implements Runnable {
    private static BattleshipClient client;
    private Thread thread;
    private String serverAddress;
    private int serverPort;

    private BattleshipClient(){
        // Singleton
    }

    public static BattleshipClient getInstance() {
        if (client == null) {
            client = new BattleshipClient();
        }
        return client;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        while (true) {
            processServerMessages();
        }
    }

    private void processServerMessages() {
        boolean running = true;
        System.out.println(serverAddress + ":" + serverPort);
        try (Socket socket = new Socket(serverAddress, serverPort);){
            Globals.CLIENT_CONNECTED = true;
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println("CONNECTION " + socket.getLocalAddress().toString().substring(1) + " " + Globals.LOCAL_PORT);
            while (in.hasNextLine() && running) {
                String line = in.nextLine();
                if(line.startsWith("CONNECTION_SUCCESS")){
                    Platform.runLater(() -> {
                        Globals.game.setStartingPlayer();
                        Globals.game.startGame();
                    });
                }
                if(line.startsWith("PLACEMENT_FINISHED")){
                    Platform.runLater(() -> {
                        Globals.game.setEnemyReady();
                        if(Globals.gameState == GameState.PLACEMENT_FINISHED){
                            if(Globals.game.startingPlayer){
                                Globals.gameState = GameState.PLAYER_TURN;
                            } else {
                                Globals.gameState = GameState.ENEMY_TURN;
                            }
                        }
                    });
                }
                if(line.startsWith("ATTACK")){
                    System.out.println(line.split(" ")[1]+" "+line.split(" ")[2]);
                    Platform.runLater(() -> Globals.game.resolveEnemyTurn(line.split(" ")));
                }
                System.out.println(line);
                out.println("RESPONSE");
            }
        } catch (ConnectException e) {
            try {
                // e.printStackTrace();
                System.out.println("Connection attempt failed. Retrying in 2 seconds");
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (thread == null) {
            thread = new Thread (this, "Client");
            thread.start ();
        }
    }
}
