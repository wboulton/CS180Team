package temp;

import java.util.*;
import java.util.concurrent.atomic.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.image.BufferedImage;

import java.io.*;
import java.net.*;

public class imageNetwork {
    public static void main(String[] args) {
        int port = 8282;
        byte[] imageBytes = {};
        try {
            ServerSocket server = new ServerSocket(port);
            Socket socket = server.accept();
            ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
             try {
                imageBytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("image.png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            stream.writeObject(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}