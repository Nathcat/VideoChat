package com.Nathcat.VideoChatClient;

import org.opencv.core.Core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Receives data from a com.Nathcat.VideoChatClient.NetworkStream and
 * displays in a window.
 */
public class Client {
    public static void main(String[] args) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //com.Nathcat.VideoChatClient.CameraStream stream = new com.Nathcat.VideoChatClient.CameraStream();
        ServerSocket ss = new ServerSocket(1234);
        Socket s = ss.accept();
        NetworkStream stream = new NetworkStream(new ObjectInputStream(s.getInputStream()));

        VideoDisplay app = new VideoDisplay(stream);
    }
}