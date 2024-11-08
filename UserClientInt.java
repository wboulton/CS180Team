import java.util.ArrayList;

public interface UserClientInt {
    void sendMessage(String receiver, String content, String picture) throws BadDataException;
    void deleteMessage(Message m) throws BadDataException;
    void editMessage(Message m, Message n) throws BadDataException;
    void recoverMessages();
    ArrayList getSentMessages();
    ArrayList getRecievedMessages();
    User getUser();
    void blockUser(User u);
    boolean unblockUser(User u);
    boolean addFriend(User u);
    boolean removeFriend(User u);
    String getUserName();
    boolean setUserName(String name);
    boolean setPassword(String password);

}
