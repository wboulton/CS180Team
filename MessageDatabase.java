import java.util.*;
import java.io.*; 

/**
 * Team Project -- MessageDatabase
 *
 * This file handles the message data for each user. 
 * For more in depth documentation see Docs/MessageDataStorage.md
 *
 * @author William Boulton, 7
 *
 * @version November 1, 2024
 * 
 */

public class MessageDatabase extends Thread implements MData {
    protected ArrayList<Message> recievedMessages;
    protected ArrayList<Message> sentMessages;
    private User user; // this will be the user who has sent or recieved the messages stored in said database object
    private String filePath;
    public static final Object lock = new Object();

//this creates a message database for the specified user, which is a csv file with name "username.txt"
    public MessageDatabase(User user) {
        this.user = user;
        filePath = String.format("%s.txt", user.getUsername());
        recievedMessages = new ArrayList<>();
        sentMessages = new ArrayList<>();
    }
    
    public ArrayList<Message> getSentMessages() {
        return sentMessages;
    }
    public ArrayList<Message> getRecievedMessages() {
        return recievedMessages;
    }
//this reads all of the messages in a user's file and adds them to the correct sent/recieved arraylist
    public void recoverMessages() { 
        try (BufferedReader bfr = new BufferedReader(new FileReader(this.filePath))) {
            String line;
            synchronized (lock) {
                while ((line = bfr.readLine()) != null) {
                    Message newMessage = new Message(line);
                    if (newMessage.getSender().equals(user.getUsername())) {
                        //if the message isnt already in the sent messages arraylist, add it
                        if (!sentMessages.contains(newMessage)) {
                            sentMessages.add(newMessage);
                            System.out.println(newMessage);
                        }
                    } else if (newMessage.getReciever().equals(user.getUsername())){
                        //if the message isnt already in the recieved messages arraylist, add it
                        if (!recievedMessages.contains(newMessage)) {
                            recievedMessages.add(newMessage);
                        }
                    } else {
                        System.out.println("This message shouldn't be here");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//this handles sending messages by adding it immediately to the arraylist and the file for this user, it also adds it
// to the file for the recieving user. I do this immediately so that if the program crashes later the messages are
// not lost. 
    public void sendMessage(Message m) throws BadDataException {
        //Here I simply check if they are blocked before sending the message
        //Carefull, before this function can be called the database must be loaded and running
        User rUser = UserDatabase.getUser(m.getReciever());
        User sUser = UserDatabase.getUser(m.getSender());
        if (rUser == null || sUser == null) {
            throw new BadDataException("One of the users did not exist");
        }
        if (rUser.getBlockedUsers().contains(m.getSender())) {
            throw new BadDataException("This user is blocked");
        }
        if (!rUser.isAllowAll()) {
            if (!rUser.getFriends().contains(m.getSender())) {
                throw new BadDataException("This user only allows messages from friends");
            }
        }
        //this system allows illegal messages to be created but never sent, that is subject to change. 
        synchronized (lock) {
            sentMessages.add(m);
            String senderFile = String.format("%s.txt", m.getSender());
            String recieverFile = String.format("%s.txt", m.getReciever());
            try (PrintWriter out = new PrintWriter(new FileWriter(senderFile, true))) {
                out.println(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try (PrintWriter out = new PrintWriter(new FileWriter(recieverFile, true))) {
                out.println(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

// this removes a specified message from both the sender and reciever's files
    @Override
    public void deleteMessage(Message m) {
        synchronized(lock) {
            int id = m.getMessageID();
            try {
                sentMessages.remove(m);
            } catch (Exception e) {
                System.out.println("This message did not exist");
                return;
            }
            //here I use the arraylist to generate the new list of messages after the delete, this means 
            //the array list must be up to date before deleting.
            try (BufferedWriter sender = new BufferedWriter(new FileWriter(filePath))) {
                for (Message message : sentMessages) {
                    sender.write(message.toString());
                    sender.newLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String receiver = m.getReciever();
            String receiveFile = String.format("%s.txt", receiver);
            ArrayList<Message> messagesToWrite = new ArrayList<>();
            try (BufferedReader get = new BufferedReader(new FileReader(receiveFile))) {
                String line;
                while ((line = get.readLine()) != null) {
                    messagesToWrite.add(new Message(line));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } 
            try (BufferedWriter put = new BufferedWriter(new FileWriter(receiveFile))) {
                for (Message message : messagesToWrite) {
                    if (message.getMessageID() == id) {
                        continue;
                    }
                    put.write(message.toString());
                    put.newLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public User getUser() {
        return user;
    }
    @Override
    public String getFilePath() {
        return filePath;
    }
    //edits the message by creating a new one witht the same messageID and resending it. 
    @Override
    public void editMessage(Message m, Message n) throws BadDataException {
        synchronized(lock) {
            n.setMessageID(m.getMessageID());
            deleteMessage(m);
            sendMessage(n);
        }
    }
}
