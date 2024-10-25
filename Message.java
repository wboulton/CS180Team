public class Message {
    private User sender;
    private User reciever;
    private String content;
    private byte[] pictureContent;
    private boolean containsPicture;
//this should parse csv of some format, probably: sender,reciever,content,containsPicture,pictureContent, 
    public Message(String data) { 

    }

    public User getReciever() {
        return reciever;
    }
    public User getSender() {
        return sender;
    }
}
