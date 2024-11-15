import java.io.IOException;
public interface UserClientInt {
    void sendMessage(String receiver, String content, String picture) throws BadDataException, IOException;
    void deleteMessage(String sender, Message m) throws BadDataException, IOException;
    void editMessage(Message m, String newContent) throws IOException;
    void blockUser(String u) throws IOException;
    void unblockUser(String u) throws IOException;
    void addFriend(String u) throws IOException;
    void removeFriend(String u) throws IOException;
    void setUserName(String name);
    void setPassword(String password);

}
