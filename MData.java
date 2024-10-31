import java.util.*;
import java.io.*;

public interface MData {
    ArrayList getSentMessages();
    ArrayList getRecievedMessages();
    void recoverMessages();
    void setFilePath(String filePath);
    void setUser(User user);
    User getUser();
    String getFilePath();
    void sendMessage(Message m);
    void deleteMessage(Message m);
    void editMessage(Message m, Message n);
}
