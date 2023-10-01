import com.Nathcat.VideoChatClient.CameraStream;
import org.opencv.core.Core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        System.out.print("Hostname > ");
        String host = new Scanner(System.in).nextLine();
        System.out.print("Port > ");
        int port = new Scanner(System.in).nextInt();

        Socket s = new Socket(host, port);
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        CameraStream cameraStream = new CameraStream();

        while (true) {
            Image frame = (Image) cameraStream.readObject();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write((RenderedImage) frame, "jpg", bos );

            oos.writeObject(bos.toByteArray());
            oos.flush();
        }
    }
}
