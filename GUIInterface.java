import java.util.*;
//all other methods implemented by GUIClient.java are private and are not typically implemented in an interface. 
public interface GUIInterface {
    void setMessages(ArrayList<String> messages);
    void run();
    void startMessageUpdater();
}