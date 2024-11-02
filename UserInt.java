import java.awt.image.BufferedImage;

import java.util.*;


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
    void changePassword(String newPassword);
    String listToString(ArrayList<User> list);
    boolean isAllowAll();
    void setAllowAll(boolean newBoolean);
}
