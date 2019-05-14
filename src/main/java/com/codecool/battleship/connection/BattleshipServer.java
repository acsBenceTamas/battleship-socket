package com.codecool.battleship.connection;

import com.codecool.battleship.Globals;
import javafx.application.Platform;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;

public class BattleshipServer implements Runnable {
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
        // singleton
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void sendCommand(String command) {
        commands.addLast(command);
    }

    public void run() {
        try (ServerSocket listener = new ServerSocket(port)) {
            socket = listener.accept();
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                if (in.hasNextLine()) {
                    String response = in.nextLine();
                    if (response.startsWith("CONNECTION") && !Globals.CLIENT_CONNECTED) {
                        System.out.println(response);
                        String[] responseSeparated = response.split(" ");
                        BattleshipClient client = BattleshipClient.getInstance();
                        client.setServerAddress(responseSeparated[1]);
                        client.setServerPort(Integer.parseInt(responseSeparated[2]));
                        client.start();
                        Platform.runLater(() -> Globals.game.startGame());
                        sendCommand("CONNECTION_SUCCESS");
                    }
                }
                if (commands.size() > 0) {
                    out.println(commands.pollFirst());
                    String response = in.nextLine();
                    System.out.println(response);
                    System.out.println("Client responded to command");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (thread == null) {
            thread = new Thread (this, "Server");
            thread.start ();
        }
    }
}
