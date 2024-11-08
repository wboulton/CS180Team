import java.util.*;
import java.io.*;
import java.net.*;

public class MediaServer extends Thread {
    
    private static void run(Socket client, ServerSocket server) {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            writer = new PrintWriter(client.getOutputStream()); 
            System.out.println("connected");
            String line = reader.readLine();
            // this stuff is just in testing state rn
            System.out.printf("Received '%s' from server\n", line);
            while (true) {
                line = reader.readLine();
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        int port;
        try {
        port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.out.println("Enter a valid 4 digit port number");
            return;
        }
        try {
            ServerSocket server = new ServerSocket(port);
            final ArrayList<Socket> socket = new ArrayList<>();
            while (true) {
                final Socket newSocket = server.accept();
                if (!socket.contains(newSocket)) {
                    socket.add(newSocket);
                    Thread clientThread = new Thread(() -> run(newSocket, server));
                    clientThread.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
