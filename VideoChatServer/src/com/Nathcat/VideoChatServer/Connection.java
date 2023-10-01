package com.Nathcat.VideoChatServer;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.*;

public class Connection extends Thread {
    public static class Frame implements Serializable {
        public final String destination;
        public final byte[] contents;

        public Frame(String destination, byte[] contents) {
            this.destination = destination;
            this.contents = contents;
        }
    }

    public final String name;
    private final Socket s;
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;
    private final Main server;

    public Connection(Main server, Socket s) {
        this.s = s;
        this.server = server;

        try {
            oos = new ObjectOutputStream(s.getOutputStream());
            ois = new ObjectInputStream(s.getInputStream());
            name = (String) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(Object obj) {
        try {
            oos.writeObject(obj);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object receive() {
        try {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (s.isConnected()) {
            send(server.forwardFrame((Frame) receive()));
        }
    }
}
