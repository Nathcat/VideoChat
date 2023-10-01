package com.Nathcat.VideoChatClient;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInput;
import java.util.Date;

/**
 * Window class to display images in a window at a specified framerate, from a stream
 */
public class VideoDisplay {
    private class CaptureThread extends Thread {
        public boolean running = true;
        public ObjectInput inputStream;

        public CaptureThread(ObjectInput inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            long time;
            while (running) {
                time = new Date().getTime();

                // Get an image from the stream
                Image frame;
                try {
                    frame = (Image) inputStream.readObject();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Change the size of the window and canvas to match the received image, then draw the image
                VideoDisplay.this.root.setSize(frame.getWidth(VideoDisplay.this.canvas), frame.getHeight(VideoDisplay.this.canvas));
                VideoDisplay.this.canvas.setSize(frame.getWidth(VideoDisplay.this.canvas), frame.getHeight(VideoDisplay.this.canvas));
                VideoDisplay.this.canvas.getGraphics().drawImage(frame, 0, 0, VideoDisplay.this.canvas);

                // Determine the required wait time in order to meet the specified framerate
                try {
                    long waitTime = (1000 / VideoDisplay.FRAMERATE) - (new Date().getTime() - time);
                    if (waitTime >= 0) {
                        Thread.sleep(waitTime);
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static final int FRAMERATE = 60;
    public static final String WINDOW_NAME = "Video Chat";
    public JFrame root;
    public Canvas canvas;
    public CaptureThread cThread;

    public VideoDisplay(ObjectInput inputStream) {
        root = new JFrame();
        canvas = new Canvas();

        root.setTitle(WINDOW_NAME);
        root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        root.getContentPane().add(canvas);
        root.setVisible(true);

        cThread = new CaptureThread(inputStream);
        cThread.setDaemon(true);
        cThread.start();
    }
}