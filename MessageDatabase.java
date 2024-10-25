import java.util.*;
import java.io.*;

public class MessageDatabase {
    protected ArrayList<Message> recievedMessages;
    protected ArrayList<Message> sentMessages;
    private User user; // this will be the user who has sent or recieved the messages stored in said database object
    private String filePath;

    public MessageDatabase(User user, String filePath) {
        this.user = user;
        this.filePath = filePath;
        recievedMessages = new ArrayList<>();
        sentMessages = new ArrayList<>();
    }
    
    private ArrayList getSentMessages() {
        return sentMessages;
    }
    private ArrayList getRecievedMessages() {
        return recievedMessages;
    }
    
}
