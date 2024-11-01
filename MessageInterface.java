/**
 * Team Project -- MessageInterface
 *
 * This is simply the interface for Message.java
 *
 * @author William Boulton, 7
 *
 * @version November 1, 2024
 * 
 */

public interface MessageInterface {
    String getSender();

    void setMessageID(int id);

    String getReciever();
    String getContent();
    byte[] getPicture();
    boolean hasPicture();
    String toString();
    int getMessageID();
    void editMessage(String content); //these void messages need signatures
    void addPicture(byte[] pictureContent);
    void editPicture(byte[] pictureContent);
    void readMessage();
}
