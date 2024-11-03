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
    boolean addFriend(String user);
    boolean removeFriend(String user);
    boolean blockUser(String user);
    boolean unblockUser(String user);
    String getUsername();
    boolean verifyLogin(String password);
    boolean equals(User user);
    String toString();
    byte[] getProfilePicture();
    void changeUsername(String newUsername);
    void changePassword(String newPassword);
    String stringListToString(ArrayList<String> list);
    boolean isAllowAll();
    void setAllowAll(boolean newBoolean);
}
