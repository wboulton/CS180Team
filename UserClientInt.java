import java.util.ArrayList;

public interface UserClientInt {
    void sendMessage(Message m) throws BadDataException;
    void deleteMessage(Message m) throws BadDataException;
    void editMessage(Message m, Message n) throws BadDataException;
    void recoverMessages();
    ArrayList getSentMessages();
    ArrayList getRecievedMessages();
    User getUser();
    String getFilePath();
    void blockUser(User u);
    void unblockUser(User u);
    void addFriend(User u);
    void removeFriend(User u);
    String getUserName();
    void setUserName(String name);
    void setPassword(String password);
    void setFilePath(String path);


}
