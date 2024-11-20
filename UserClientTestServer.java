
import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Objects;

/**
 * Team Project -- UserClientTestServer
 *
 * This file tests the UserClient class.
 *
 * @author Jai Menon
 *
 * @version November 8, 2024
 *
 */

public class UserClientTestServer {

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(8080);
        Socket socket = server.accept();
        BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        String confirmation = serverReader.readLine();
        boolean outputChecker = true;
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
        writer.println(""+outputChecker);
    }

}
