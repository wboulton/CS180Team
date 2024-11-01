public interface MessageInterface {
    String getSender();
    String getReciever();
    String getContent();
    byte[] getPicture();
    boolean hasPicture();
    String toString();
    void editMessage(String content); //these void messages need signatures
    void sendMessage();
    void deleteMessage();
    void addPicture(byte[] pictureContent);
    void editPicture(byte[] pictureContent);
    void readMessage(); 
}
