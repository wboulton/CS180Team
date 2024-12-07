import java.util.*;

/**
 * Team Project -- GUIInterface
 *
 * This is the interface for the main GUI class, it does not contain many methods because 
 * most methods in the GUI are private
 *
 * @author William Boulton
 *
 * @version November 1, 2024
 *
 */
public interface GUIInterface {
    void setMessages(ArrayList<String> messages);
    void run();
    void startMessageUpdater();
}