package com.Nathcat.VideoChatClient;

import org.opencv.core.Core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import static com.Nathcat.VideoChatServer.Main.SERVER_HOSTNAME;
import static com.Nathcat.VideoChatServer.Main.SERVER_PORT;

/**
 * Receives data from a com.Nathcat.com.Nathcat.VideoChatClient.NetworkStream and
 * displays in a window.
 */
public class Client {

    public static void main(String[] args) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Socket s = new Socket(SERVER_HOSTNAME, SERVER_PORT);
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

        System.out.println("Enter your contact name > ");
        oos.writeObject(new Scanner(System.in).nextLine());
        oos.flush();

        NetworkStream stream = new NetworkStream(new ObjectInputStream(s.getInputStream()));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                s.close();
                oos.close();
                stream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));

        VideoDisplay app = new VideoDisplay(stream);
    }
}