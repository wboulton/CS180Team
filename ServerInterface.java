import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * Team Project -- ServerInterface
 *
 * This is the interface for the server
 *
 * @author William Boulton, Alan Yi
 *
 * @version November 15, 2024
 * 
 */

public interface ServerInterface {
    static UserDatabase DATABASE = new UserDatabase();
    static final Object LOCK = new Object();

    static void userHandling(PrintWriter writer, String line) {
    };

    static void messageHandling(PrintWriter writer, String line, MessageDatabase database, User viewing) {
    };

    static void run(Socket client, ServerSocket server) {
    };
}
