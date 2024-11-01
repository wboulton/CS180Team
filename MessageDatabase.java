import java.util.*;
import java.io.*; 

public class MessageDatabase implements MData{
    protected ArrayList<Message> recievedMessages;
    protected ArrayList<Message> sentMessages;
    private User user; // this will be the user who has sent or recieved the messages stored in said database object
    private String filePath;

    public MessageDatabase(User user) {
        this.user = user;
        filePath = String.format("%s.txt", user.getUsername());
        recievedMessages = new ArrayList<>();
        sentMessages = new ArrayList<>();
    }
    
    public ArrayList getSentMessages() {
        return sentMessages;
    }
    public ArrayList getRecievedMessages() {
        return recievedMessages;
    }
    //I imagine this function will be used to read through the message database and assign all messages to their assigned ArrayList
    public void recoverMessages() { 
        try (BufferedReader bfr = new BufferedReader(new FileReader(this.filePath))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                Message newMessage = new Message(line);
                if (newMessage.getSender().equals(user.getUsername())) {
                    sentMessages.add(newMessage);
                } else if (newMessage.getReciever().equals(user.getUsername())){
                    recievedMessages.add(newMessage);
                } else {
                    System.out.println("This message shouldn't be here");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message m) {
        String senderFile = String.format("%s.txt", m.getSender());
        String recieverFile = String.format("%s.txt", m.getReciever());
        try (PrintWriter out = new PrintWriter(new FileWriter(senderFile, true))) {
            out.println(this.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (PrintWriter out = new PrintWriter(new FileWriter(recieverFile, true))) {
            out.println(this.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void deleteMessage(Message m) {
        
    }
}
