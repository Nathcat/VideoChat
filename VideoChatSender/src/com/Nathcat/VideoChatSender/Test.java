package com.Nathcat.VideoChatSender;

import com.Nathcat.VideoChatClient.CameraStream;
import com.Nathcat.VideoChatServer.Connection;
import org.opencv.core.Core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import static com.Nathcat.VideoChatServer.Main.SERVER_HOSTNAME;
import static com.Nathcat.VideoChatServer.Main.SERVER_PORT;

public class Test {
    public static void main(String[] args) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        System.out.print("Your contact name > ");
        String name = new Scanner(System.in).nextLine();
        System.out.print("Destination > ");
        String destination = new Scanner(System.in).nextLine();

        Socket s = new Socket(SERVER_HOSTNAME, SERVER_PORT);
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        oos.writeObject(name);
        oos.flush();

        CameraStream cameraStream = new CameraStream();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                cameraStream.close();
                oos.close();
                s.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));

        while (true) {
            Image frame = (Image) cameraStream.readObject();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write((RenderedImage) frame, "jpg", bos );

            oos.writeObject(new Connection.Frame(destination, bos.toByteArray()));
            oos.flush();
        }
    }
}
