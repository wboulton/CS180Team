import java.io.IOException;

/**
 * Team Project -- UserClientInt
 *
 * This is the interface for the UserClient
 *
 * @author Mukund Venkatesh, Kush Kodiya
 *
 * @version November 17, 2024
 */
public interface UserClientInt {
    String sendMessage(String receiver, String content, String picture) throws BadDataException, IOException;
    void deleteMessage(int id) throws BadDataException, IOException;
    void editMessage(int id, String newContent) throws IOException;
    void blockUser(String u) throws IOException;
    boolean unblockUser(String u) throws IOException;
    boolean addFriend(String u) throws IOException;
    boolean removeFriend(String u) throws IOException;
    boolean setUserName(String name) throws IOException;
    void setPassword(String password);
    void addOrRemoveFriend(String username) throws IOException;
    void blockOrUnblock(String username) throws IOException;
}
