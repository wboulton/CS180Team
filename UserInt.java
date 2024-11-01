import java.awt.image.BufferedImage;
import java.util.ArrayList;

public interface UserInt {
    boolean addFriend(User user);
    boolean removeFriend(User user);
    boolean blockUser(User user);
    boolean unblockUser(User user);
    String getUsername();
    boolean verifyLogin(String password);
    boolean equals(User user);
    String toString();
    BufferedImage getProfilePicture();
    void changeUsername(String newUsername);
    String listToString(ArrayList<User> list);
}
