import java.io.*;

public class Message implements MessageInterface {
    private String sender; //usernames of sender and reciever
    private String reciever;
    private String content;
    private byte[] pictureContent;
    private boolean containsPicture;
    private static int pictureLocation;
    private String pictureFile;
    private final String PICTURE_NUMBERS = "picture.txt";
    //the file containing all sent and recieved messages for each user is just username.txt
//this should parse csv of some format, probably: sender,reciever,content,containsPicture,pictureContent,pictureFile
    public Message(String data) { 
        String[] info = data.split(",");
        sender = info[0];
        reciever = info[1];
        content = info[2]; //content will be null if there is not text in the message and only a picture presumably
        containsPicture = Boolean.parseBoolean(info[3]);
        if (containsPicture) {
            pictureFile = info[5];
        }
    }
//This will be the direct creation of messages
    public Message(User sender, User reciever, String content) { 
        this.sender = sender.getUsername();
        this.reciever = reciever.getUsername();
        this.content = content;
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
        this.content = content;
    }
    @Override
    public void sendMessage() {
        String senderFile = String.format("%s.txt", sender);
        String recieverFile = String.format("%s.txt", reciever);
        try (PrintWriter out = new PrintWriter(new FileWriter(senderFile, true))) {
            out.println(this.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (BufferedReader bfr = new BufferedReader(new FileReader(recieverFile))) {
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void deleteMessage() {
        
    }
    @Override
    public void addPicture() {
        try (BufferedReader bfr = new BufferedReader(new FileReader(PICTURE_NUMBERS))) {
            pictureLocation = Integer.parseInt(bfr.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
        pictureFile = String.format("%d.jpg", pictureLocation);
        try (BufferedWriter bwr = new BufferedWriter(new FileWriter(PICTURE_NUMBERS))) {
            bwr.write(pictureLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void readMessage() {
        //this is intentionally left blank, we have the get content this would be used in the
        //future for gui's possibly
    }
    @Override
    public void editPicture(byte[] pictureContent) {
        this.pictureContent = pictureContent;
    }
    @Override
    public String toString() {
        String result = String.format("%s,%s,%s,%b,%s", sender, reciever, content,
            containsPicture, pictureFile);
        return result;
    }
}
