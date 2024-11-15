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
 
    private User user = new User("johnDoe", "Password1", "John", "Doe", "false");
    private static final String OUTPUT_FILE = "users.txt";
    private BufferedReader reader;
    private PrintWriter writer;
    private Message m = new Message("0|sender|reciever|content|false");

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
    public void testClientOutput() throws BadDataException, IOException {
        sendMessage("johnDoe|Password1|John|Doe|null|null|null|true", "Hi, how are you?", "false");
        deleteMessage("johnDoe|Password1|John|Doe|null|null|null|true", m);
        editMessage(m, "new content");
        blockUser("johnDoe");
        unblockUser("johnDoe");
        addFriend("johnDoe");
        removeFriend("johnDoe");
        setUserName("johnDoe");
        setPassword("newPassword");
    } // Figure out how to get messages back from server

    
    
    public static void main(String[] args) throws Exception {
        userDatabase.createUser("johnDoe", "Password1", "John", "Doe", "false");
        // I guess what we can do is make a client socket here and make a server socket in server tests and then run
        // them simultaneously to test both at once
        ServerSocket server = new ServerSocket(1010);

        Socket socket = server.accept();

        Socket socket = serverSocket.accept();

        serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        serverWriter = new PrintWriter(socket.getOutputStream(), true);
        testLogin();
        testBadLogin();
        serverWriter.println("could not log in");
        serverWriter.flush();
        testNewUser();
        testClientOutput();
        boolean outputChecker = true;
        // Checks if sendMessage works
        String confirmation = serverReader.readLine();
        if (!confirmation.equals("SEND_MESSAGE|" + "johnDoe" + "|" + "johnDoe" + "|" + "Hi, how are you?")) {
            outputChecker = false;
        }
        // Checks if deleteMessage works
        confirmation = serverReader.readLine();
        if (!confirmation.equals("DELETE_MESSAGE|" + "johnDoe|Password1|John|Doe|null|null|null|true" + "|" + "0")) {
            outputChecker = false;
        }
        // Checks if editMessage works
        confirmation = serverReader.readLine();
        if (!confirmation.equals("EDIT_MESSAGE|" + "0" + "|" + "new content")) {
            outputChecker = false;
        }
        // Checks if blockUser works
        confirmation = serverReader.readLine();
        if (!confirmation.equals("BLOCK|" + "johnDoe" + "|" + "johnDoe")) {
            outputChecker = false;
        }
        // Checks if unblockUser works
        confirmation = serverReader.readLine();
        if (!confirmation.equals("UNBLOCK|" + "johnDoe" + "|" + "johnDoe")) {
            outputChecker = false;
        }
        // Checks if addFriend works
        confirmation = serverReader.readLine();
        if (!confirmation.equals("ADD_FRIEND|" + "johnDoe" + "|" + "johnDoe")) {
            outputChecker = false;
        }
        // Checks if removeFriend works
        confirmation = serverReader.readLine();
        if (!confirmation.equals("REMOVE_FRIEND|" + "johnDoe" + "|" + "johnDoe")) {
            outputChecker = false;
        }
        // Checks if setUserName works
        confirmation = serverReader.readLine();
        if (!confirmation.equals("CHANGE_USERNAME|" + "johnDoe" + "|" + "johnDoe")) {
            outputChecker = false;
        }
        // Checks if setPassword works
        confirmation = serverReader.readLine();
        if (!confirmation.equals("CHANGE_PASSWORD|" + "johnDoe" + "|" + "newPassword")) {
            outputChecker = false;
        }
        if (outputChecker) {
            System.out.println("All test cases passed successfully!");
        } else {
            System.out.println("Something went wrong in the client output test cases");
        }
    }


}
