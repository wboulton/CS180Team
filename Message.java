public class Message {
    private String sender; //usernames of sender and reciever
    private String reciever;
    private String content;
    private byte[] pictureContent;
    private boolean containsPicture;
//this should parse csv of some format, probably: sender,reciever,content,containsPicture,pictureContent
    public Message(String data) { 
        String[] info = data.split(",");
        sender = info[0];
        reciever = info[1];
        content = info[2]; //content will be null if there is not text in the message and only a picture presumably
        containsPicture = Boolean.parseBoolean(info[3]);
        pictureContent = info[4].getBytes();
    }

    public User getReciever() {
        return reciever;
    }
    public User getSender() {
        return sender;
    }
}
