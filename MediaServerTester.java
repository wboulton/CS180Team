import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import java.io.*;
import java.nio.file.*;
import java.util.Objects;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.*;
import java.net.*;
/**
 * Team Project -- MediaServerTester
 *
 * This file tests the MessageServer class.
 * For more in depth documentation see Docs
 *
 * @author Jai Menon
 *
 * @version November 10, 2024
 *
 */
public class MediaServerTester {
    //Checks if "connected" is printed to ensure that server an client are connecting
    public void testRunConnection(Socket client, ServerSocket server) {
        try {
            PrintWriter writer  = new PrintWriter(server.getOutputStream()); 
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            MediaServer ms = new MediaServer();
            PrintStream originalOut = System.out;
            System.setOut(ps);
            ms.run(client, server);
            writer.println("new user");
            writer.println("johnDoe");
            writer.println("Password1");
            writer.println("John");
            writer.println("Doe");
            writer.println("false");
            writer.flush();
            System.setOut(originalOut);
            writer.close();
            String output = baos.toString();
            boolean checker = false;
            // Checks if the desired text was printed
            if (output.contains("connected")) {
                checker = true;
            }
            assertTrue(checker);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(4242);
        Socket socket = serverSocket.accept();
        testRunConnection(socket, serverSocket);
    }
}
