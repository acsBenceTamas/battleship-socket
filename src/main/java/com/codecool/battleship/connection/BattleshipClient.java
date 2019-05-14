package com.codecool.battleship.connection;

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
        boolean running = true;
        while (running) {
            try (Socket socket = new Socket(serverAddress, serverPort);){
                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                while (in.hasNextLine() && running) {
                    String line = in.nextLine();
                    System.out.println(line);
                    out.println("RESPONSE");
                }
            } catch (ConnectException e) {
                try {
                    // e.printStackTrace();
                    System.out.println("Connection attempt failed. Retrying in 10 seconds");
                    Thread.sleep(10000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        if (thread == null) {
            thread = new Thread (this, "Client");
            thread.start ();
        }
    }
}
