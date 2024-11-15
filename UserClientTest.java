import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.Assert.*;
/**
 * Team Project -- UserClientTest
 *
 * This file tests the UserClient class.
 *
 * @author Jai Menon
 *
 * @version November 8, 2024
 *
 */

public class UserClientTest {

    private static UserDatabase userDatabase = new UserDatabase();
    private user = new User("johnDoe", "Password1", "John", "Doe", "false");
    private static final String OUTPUT_FILE = "users.txt";

    @Test(timeout = 1000)
    public void testLogin() throws BadDataException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream originalOut = System.out;
        System.setOut(ps);
        UserClient someone = new UserClient("johnDoe", "Password1");
        System.setOut(originalOut);
        String output = baos.toString();
        boolean checker = false;
        // Checks if the desired text was printed
        if (output.contains("Login successful.")) {
            checker = true;
        }
        assertTrue(checker);
        
    }

    @Test(timeout = 1000)
    public void testBadLogin() throws BadDataException, IOException {
        assertThrows(BadDataException.class, () -> {
            UserClient some1 = new UserClient("johnDoe", "Password1");
        });
    }

    @Test(timeout = 1000)
    public void testNewUser() throws BadDataException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream originalOut = System.out;
        System.setOut(ps);
        UserClient someone = new UserClient("johnDoe", "Password1", "John", "Doe", "false");
        System.setOut(originalOut);
        String output = baos.toString();
        boolean checker = false;
        // Checks if the desired text was printed
        if (output.contains("New user created.")) {
            checker = true;
        }
        assertTrue(checker);
    }

    @Test(timeout = 1000)
    public void testSendMessage() throws BadDataException, IOException {
        
        sendMessage("johnDoe|Password1|John|Doe|null|null|null|true", "Hi, how are you?", "false");
        
    }

/*
User user = userDatabase.getUser("johnDoe");
        assertNotNull(user);
        assertEquals("johnDoe", user.getUsername());
        // Tests to make sure no 2 UserClient objects have the same first name
        assertThrows(BadDataException.class, () -> {
            UserClient some1 = new UserClient("johnDoe", "Password1", "John", "Doe", "false");
            UserClient some2 = new UserClient("johnDoe", "Password2", "Johnny", "Doe", "false");
        });
        // Tests to make sure UserClient passwors is valid
        assertThrows(BadDataException.class, () -> {
            UserClient some3 = new UserClient("janeDoe", "short", "Jane", "Doe", "false");
        });
        // Tests to make sure pipes aren't used in any of the constructor field
        assertThrows(BadDataException.class, () -> {
            userDatabase.createUser("johnDoe", "Password|123", "John", "Doe", "false");
        });
        boolean invalidUser = false;
        try {
            UserClient client = new UserClient("test", "Testers123!", "test", "test", "false");
        } catch (BadDataException e) {
            invalidUser = true;
        }
    */









    
    public static void main(String[] args) throws Exception {
        userDatabase.createUser("johnDoe", "Password1", "John", "Doe", "false");
        // I guess what we can do is make a client socket here and make a server socket in server tests and then run
        // them simultaneously to test both at once
        ServerSocket server = new ServerSocket(1010);
        Socket socket = serverSocket.accept();
        serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        serverWriter = new PrintWriter(socket.getOutputStream(), true);
        testLogin();
        testBadLogin();
        serverWriter.println("could not log in");
        serverWriter.flush();
        testNewUser();
        testSendMessage();
        String confirmation = serverReader.readLine();
        if (confirmation.equals()) {
            serverWriter.println();
            serverWriter.flush();
        } else {
            serverWriter.println("false");
            serverWriter.flush();
        }
    }


}
