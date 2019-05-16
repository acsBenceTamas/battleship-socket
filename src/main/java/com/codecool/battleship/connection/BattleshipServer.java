package com.codecool.battleship.connection;

import com.codecool.battleship.Globals;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class BattleshipServer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(BattleshipServer.class);
    private static BattleshipServer server;
    private Thread thread;
    private int port = 0;
    private Socket socket;
    private Deque<String> commands = new LinkedList<>();

    public static BattleshipServer getInstance() {
        if (server == null) {
            server = new BattleshipServer();
        }
        return server;
    }

    private BattleshipServer() {
        logger.debug("Creating BattleshipServer singleton");
    }

    public void setPort(int port) {
        logger.debug("Setting server port to " + port);
        this.port = port;
    }

    public void sendCommand(String command) {
        logger.debug("Adding command to command stack: " + command);
        commands.addLast(command);
    }

    public void run() {
        logger.debug("Server is running");
        try (ServerSocket listener = new ServerSocket(port)) {
            logger.debug("Server socket initiated");
            socket = listener.accept();
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            while (Globals.GAME_IS_RUNNING) {
                logger.trace("Starting server while loop");
                if (!Globals.CLIENT_CONNECTED) {
                    try {
                        logger.trace("Inside try");
                        String response = in.nextLine();
                        logger.trace("Response: "+response);
                        if (response.startsWith("CONNECTION")) {
                            logger.debug("Received connection information from remote client: " + response);
                            String[] responseSeparated = response.split(" ");
                            BattleshipClient client = BattleshipClient.getInstance();
                            client.setServerAddress(responseSeparated[1]);
                            client.setServerPort(Integer.parseInt(responseSeparated[2]));
                            client.start();
                            Globals.CLIENT_CONNECTED = true;
                            Platform.runLater(() -> Globals.game.startGame());
                            sendCommand("CONNECTION_SUCCESS");
                            logger.debug("Responding to remote client with CONNECTION_SUCCESS");
                        }
                    } catch (NoSuchElementException e) {
                        logger.trace("No next line");
                    }
                }
                if (commands.size() > 0) {
                    logger.debug("Top command: "+commands.peekFirst());
                    String command = commands.pollFirst();
                    logger.debug("Sending command to remote client: " + command);
                    out.println(command);
                    String response = in.nextLine();
                    logger.debug("Received response from remote client: " + response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        logger.debug("Initiating server thread");
        if (thread == null) {
            thread = new Thread (this, "Server");
            thread.start ();
        }
    }
}
