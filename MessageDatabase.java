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
    //I imagine this function will be used to read through the message database and assign all messages to their assigned ArrayList
    private void recoverMessages() { 
        try (BufferedReader bfr = new BufferedReader(new FileReader(this.filePath))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                Message newMessage = new Message(line);
                if (newMessage.getSender() == user.getUsername()) {
                    sentMessages.add(newMessage);
                } else if (newMessage.getReciever() == user.getUsername()){
                    recievedMessages.add(newMessage);
                } else {
                    System.out.println("This message shouldn't be here");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
