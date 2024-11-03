import java.util.*;
import java.io.*;

/**
 * Team Project -- MData
 *
 * This is simply the interface for MessageDatabase.java
 *
 * @author William Boulton
 *
 * @version November 1, 2024
 * 
 */

public interface MData {
    ArrayList getSentMessages();
    ArrayList getRecievedMessages();
    void recoverMessages();
    User getUser();
    String getFilePath();
    void sendMessage(Message m) throws BadDataException;
    void deleteMessage(Message m) throws BadDataException;
    void editMessage(Message m, Message n) throws BadDataException;
}
