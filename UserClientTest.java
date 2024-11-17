//import org.junit.*;
//import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Objects;
//import org.junit.Test;
//import static org.junit.Assert.*;
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

   // @Test(timeout = 1000)
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
        //assertTrue(checker);
        System.out.println(checker);
    }

    //@Test(timeout = 1000)
    public void testBadLogin() throws BadDataException, IOException {
        try {
            UserClient some1 = new UserClient("johnDoe", "Password1");
        } catch (BadDataException e) {
            System.out.println(true);
        }
        /* 
        assertThrows(BadDataException.class, () -> {
            UserClient some1 = new UserClient("johnDoe", "Password1");
        });
        */
    }

    //@Test(timeout = 1000)
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
        //assertTrue(checker);
        System.out.println(checker);
    }

    //@Test(timeout = 1000)
    public void testClientOutput() throws BadDataException, IOException {
        UserClient usc = new UserClient("johnDoe", "Password1", "John", "Doe", "false");
        usc.sendMessage("johnDoe|Password1|John|Doe|null|null|null|true", "Hi, how are you?", "false");
        usc.deleteMessage("johnDoe|Password1|John|Doe|null|null|null|true", m);
        usc.editMessage(m.getMessageID(), "new content");
        usc.blockUser("johnDoe");
        usc.unblockUser("johnDoe");
        usc.addFriend("johnDoe");
        usc.removeFriend("johnDoe");
        usc.setUserName("johnDoe");
        usc.setPassword("newPassword");
    } // Figure out how to get messages back from server

    
    
    public static void main(String[] args) throws Exception {
        try {
            UserClientTest uct = new UserClientTest();
            // I guess what we can do is make a client socket here and make a server socket in server tests and then run
            // them simultaneously to test both at once
            Socket socket = new Socket("localhost", 8080);


            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter serverWriter = new PrintWriter(socket.getOutputStream(), true);
            uct.testLogin();
            uct.testBadLogin();
            serverWriter.println("could not log in");
            serverWriter.flush();
            uct.testNewUser();
            uct.testClientOutput();
            
            // Checks if sendMessage works
            String thingy = serverReader.readLine();
            boolean outputChecker = Boolean.parseBoolean(thingy);
            if (outputChecker) {
                System.out.println("All test cases passed successfully!");
            } else {
                System.out.println("Something went wrong in the client output test cases");
            }
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
