public interface MessageInterface {
    String getSender();
    String getReciever();
    String getContent();
    byte[] getPicture();
    boolean hasPicture();
    String toString();
    void editMessage(); //these void messages need signatures
    void sendMessage();
    void deleteMessage();
    void sendPicture();
    void readMessage(); //idk if we are going to use this, the point is to actually open the message for a reciever. May be of type String
}
