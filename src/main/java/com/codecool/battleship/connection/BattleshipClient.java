package com.codecool.battleship.connection;

import com.codecool.battleship.Globals;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
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
        boolean running = true;
        System.out.println(serverAddress + ":" + serverPort);
        try (Socket socket = new Socket(serverAddress, serverPort);){
            Globals.CLIENT_CONNECTED = true;
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            logger.debug("Sending connection information to remote client");
            out.println("CONNECTION " + socket.getLocalAddress().toString().substring(1) + " " + Globals.LOCAL_PORT);
            while (in.hasNextLine() && running) {
                String line = in.nextLine();
                if(line.startsWith("CONNECTION_SUCCESS")){
                    logger.debug("Remote client received connection information successfully");
                    Platform.runLater(() -> Globals.game.startGame());
                }
                logger.debug("Sending confirmation of received message from remote client");
                out.println("RESPONSE");
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
