import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

/**
 * Team Project -- Message
 *
 * This file creates the message objects for when each user sends a message
 *
 * @author William Boulton, 7
 *
 * @version November 1, 2024
 *
 */

public class Message implements MessageInterface {
    private String sender; //usernames of sender and reciever
    private String reciever;
    private String content;
    private byte[] pictureContent;
    private boolean containsPicture;
    private static int pictureLocation;
    private int messageID;
    private String pictureFile;
    private final String PICTURE_NUMBERS = "picture.txt";
    private final String MESSAGE_ID = "MessageIDCounter.txt";
    public final static Object lock = new Object();
    //the file containing all sent and recieved messages for each user is just username.txt
//this should parse psv of some format, probably: messageID,sender,reciever,content,containsPicture,pictureFile
    public Message(String data) { 
        synchronized(lock){
            String[] info = data.split("|");
            messageID = Integer.parseInt(info[0]);
            sender = info[1];
            reciever = info[2];
            content = info[3]; //content will be null if there is not text in the message and only a picture presumably
            containsPicture = Boolean.parseBoolean(info[4]);
            if (containsPicture) {
                pictureFile = info[5]; //picture file only included if containsPicture
            }
        }
    }
//This will be the direct creation of messages
    public Message(User sender, User reciever, String content) throws BadDataException {
        if (content.contains("|")) {
            throw new BadDataException("Message content cannont conatain '|'");
        }
        synchronized(lock){
            this.sender = sender.getUsername();
            this.reciever = reciever.getUsername();
            this.content = content;
            try (BufferedReader bfr = new BufferedReader(new FileReader(MESSAGE_ID))) {
                messageID = Integer.parseInt(bfr.readLine());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try (BufferedWriter bwr = new BufferedWriter(new FileWriter(MESSAGE_ID))) {
                bwr.write(messageID + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            containsPicture = false;
        }
    }
    public int getMessageID() {
        return messageID;
    }
    @Override
    public void setMessageID(int id) {
        this.messageID = id;
    }
    public String getReciever() {
        return reciever;
    }
    public String getSender() {
        return sender;
    }
    @Override
    public boolean hasPicture() {
        return this.containsPicture;
    }
    @Override
    public byte[] getPicture() {
        return this.pictureContent;
    }
    @Override
    public String getContent() {
        return this.content;
    }
    @Override
    public void editMessage(String content) {
        synchronized(lock){
            this.content = content;
        }
    }
    //I removed the send message and delete message functions. I think these would be
    //better placed in the Message Database file
    
    @Override
    public void addPicture(byte[] pictureContent) {
        synchronized(lock){
            try (BufferedReader bfr = new BufferedReader(new FileReader(PICTURE_NUMBERS))) {
                pictureLocation = Integer.parseInt(bfr.readLine());
            } catch (Exception e) {
                System.out.println("Error reading picture number");
            }
            pictureFile = String.format("%d.jpg", pictureLocation);
            try (BufferedWriter bwr = new BufferedWriter(new FileWriter(PICTURE_NUMBERS))) {
                bwr.write(pictureLocation);
            } catch (Exception e) {
                System.out.println("Error writing picture number");
            }
            try {
                ByteArrayInputStream streamObj = new ByteArrayInputStream(pictureContent);
                BufferedImage newImage = ImageIO.read(streamObj);
                ImageIO.write(newImage, "jpg", new File(pictureFile)); 
            } catch (Exception e) {
                System.out.println("Error writing picture");
            }
            containsPicture = true;
        }
    }
    @Override
    public void readMessage() {
        //this is intentionally left blank, we have the get content this would be used in the
        //future for gui's possibly
    }
    @Override
    public void editPicture(byte[] pictureContent) {
        synchronized(lock){
            this.pictureContent = pictureContent;
        }
    }
    @Override
    public String toString() {
        String result = String.format("%d|%s|%s|%s|%b", messageID, sender, reciever, content,
            containsPicture);
        if (containsPicture) {
            result += "|" + pictureFile;
        }
        return result;
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof Message) {
            Message m = (Message) o;
            return m.getMessageID() == this.messageID;
        }
        return false;
    }
}
