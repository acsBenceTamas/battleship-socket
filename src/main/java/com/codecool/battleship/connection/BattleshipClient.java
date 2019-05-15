package com.codecool.battleship.connection;

import com.codecool.battleship.GameState;
import com.codecool.battleship.Globals;
import com.codecool.battleship.tile.TileStatus;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class BattleshipClient implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(BattleshipClient.class);
    private static BattleshipClient client;
    private Thread thread;
    private String serverAddress;
    private int serverPort;

    private BattleshipClient(){
        logger.debug("Creating BattleshipClient singleton");
    }

    public static BattleshipClient getInstance() {
        logger.trace("Getting client instance");
        if (client == null) {
            client = new BattleshipClient();
        }
        return client;
    }

    public void setServerAddress(String serverAddress) {
        logger.debug("Remote address set to " + serverAddress);
        this.serverAddress = serverAddress;
    }

    public void setServerPort(int serverPort) {
        logger.debug("Remote port set to " + serverPort);
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        while (true) {
            processServerMessages();
        }
    }

    private void processServerMessages() {
        logger.debug("Client thread running");
        System.out.println(serverAddress + ":" + serverPort);
        try (Socket socket = new Socket(serverAddress, serverPort);){
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            if(!Globals.CLIENT_CONNECTED){
                logger.debug("Sending connection information to remote client");
                out.println("CONNECTION " + socket.getLocalAddress().toString().substring(1) + " " + Globals.LOCAL_PORT);
                Globals.CLIENT_CONNECTED = true;
            }
            while (Globals.GAME_IS_RUNNING) {
                try {
                    String line = in.nextLine();
                    logger.debug("Received command from remove client: "+line);
                    if(line.startsWith("CONNECTION_SUCCESS")){
                        logger.debug("Remote client received connection information successfully");
                        Platform.runLater(() -> {
                            Globals.game.setStartingPlayer();
                            Globals.game.startGame();
                        });
                    }
                    if(line.startsWith("PLACEMENT_FINISHED")){
                        logger.debug("Enemy finished with placement");
                        Platform.runLater(() -> {
                            Globals.game.setEnemyReady();
                            if(Globals.gameState == GameState.PLACEMENT_FINISHED){
                                logger.debug("Our placement is finished");
                                if(Globals.game.startingPlayer){
                                    Globals.gameState = GameState.PLAYER_TURN;
                                } else {
                                    Globals.gameState = GameState.ENEMY_TURN;
                                }
                            }
                        });
                    }
                    if(line.startsWith("ATTACK_SEND")){
                        logger.debug("Position: X: "+line.split(" ")[1]+", Y: "+line.split(" ")[2]);
                        Platform.runLater(() -> Globals.game.resolveEnemyTurn(line.split(" ")));
                    }
                    if(line.startsWith("ATTACK_RESPONSE")){
                        String[] args = line.split(" ");
                        int x = Integer.parseInt(args[1]);
                        int y = Integer.parseInt(args[2]);
                        TileStatus status = TileStatus.valueOf(args[3]);
                        Platform.runLater(() -> Globals.game.markEnemyTile(x,y,status));
                    }
                    if(line.startsWith("GAME_WON")) {
                        Platform.runLater(() -> Globals.game.handleGameOver(true));
                    }
                    logger.debug("Sending confirmation of received message from remote client");
                    out.println("RESPONSE");
                } catch (NoSuchElementException e) {
                    logger.trace("No element found");
                }
            }
        } catch (ConnectException e) {
            try {
                // e.printStackTrace();
                logger.debug("Connection attempt failed. Retrying in 2 seconds");
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        logger.debug("Initiating client thread");
        if (thread == null) {
            thread = new Thread (this, "Client");
            thread.start ();
        }
    }
}
