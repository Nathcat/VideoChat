package com.Nathcat.VideoChatServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Main {
    public static final String SERVER_HOSTNAME = "localhost";
    public static final int SERVER_PORT = 1234;
    public HashMap<String, Connection> connections = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Main server = new Main();
    }

    public Main() throws IOException {
        // Create the server socket
        ServerSocket ss = new ServerSocket(1234);

        // Create a shutdown hook to close the server socket
        Thread shutdownHook = new Thread(() -> {
            try {
                ss.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Runtime.getRuntime().addShutdownHook(shutdownHook);

        // Wait for connections
        while (true) {
            Socket s = ss.accept();
            Connection connection = new Connection(this, s);
            connection.setDaemon(true);
            connection.start();
            connections.put(connection.name, connection);
        }
    }

    public boolean forwardFrame(Connection.Frame frame) {
        Connection target = connections.get(frame.destination);
        if (target == null) {
            return false;
        }

        target.send(frame);
        return true;
    }
}