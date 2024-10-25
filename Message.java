public class Message implements MessageInterface {
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
//This will be the direct creation of messages
    public Message(User sender, User reciever, String content, byte[] pictureContent) { 
        if (pictureContent == null || pictureContent.length == 0) {
            containsPicture = false;
        } else {
            this.pictureContent = pictureContent;
        }
        this.sender = sender.getUsername();
        this.reciever = reciever.getUsername();
        this.content = content;
    }

    public User getReciever() {
        return reciever;
    }
    public User getSender() {
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
}
