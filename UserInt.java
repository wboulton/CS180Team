import java.util.*;
/**
 * Team Project -- UserInt
 *
 * This is the interface for User.java
 * @author William Boulton, Mukund Venkatesh
 * @version November 1, 2024
 *
 */

public interface UserInt {
    boolean addFriend(User user);
    boolean removeFriend(User user);
    boolean blockUser(User user);
    boolean unblockUser(User user);
    String getUsername();
    boolean verifyLogin(String password);
    boolean equals(User user);
    String toString();
    byte[] getProfilePicture();
    void changeUsername(String newUsername);
    void changePassword(String newPassword);
    String listToString(ArrayList<User> list);
    boolean isAllowAll();
    void setAllowAll(boolean newBoolean);
}
