import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.awt.*;
import java.net.Socket;

//here is an example of how to display pictures in a gui. This takes a bytearray and converts it to a displayable picture. 
//What you will have TODO:
//make the network store images in png files, when there is a picture to read, it will send the byte array and the client will
//store that array in an array list of byte arrays and display that picture in the corresponding message location. If you need, you can 
//edit it so that messages that contain pictures can only display pictures and not be able to contain text content. Users should 
//have their own png with their profile picture, when someone starts viewing a new user, send that byte array and display it in the 
//corresponding location for viewing user profile picutre. 
public class imageGUI implements Runnable {
    private byte[] imageBytes;

    public imageGUI(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public void run() {
        
        JFrame frame = new JFrame("testing");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
            BufferedImage myPicture = ImageIO.read(bais);
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            frame.add(picLabel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Example usage with a byte array
        byte[] imageBytes = {}; // Replace with actual byte array
        try {
            Socket client = new Socket("localhost", 8282);
            ObjectInputStream input = new ObjectInputStream(client.getInputStream());
            imageBytes = (byte[]) input.readObject();
        } catch (java.io.IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new imageGUI(imageBytes));
    }
}