import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.util.ArrayList;
import java.io.*;
import java.util.Arrays;

public class User {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private ArrayList<User> friends;
    private ArrayList<User> blockedUsers;
    public static final Object lock = new Object();
    //profile picture
    private byte[] profilePicture;
// you probably want a constructor which can take in a csv line from the database and make a user based on that
    public User(String username, String password, String firstName, String lastName, String profilePicture) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.friends = new ArrayList<User>();
        this.blockedUsers = new ArrayList<User>();
        try {
            File imageFile = new File(profilePicture);
            byte[] imageData = Files.readAllBytes(imageFile.toPath());
        } catch (Exception e) {
            this.profilePicture = null;
        }
    }
    public void addFriend(User user) {
        synchronized(lock){
            friends.add(user);
        }
    }
    public void removeFriend(User user) {
        synchronized(lock){
            friends.remove(user);
        }
    }
    public void blockUser(User user) {
        synchronized(lock){
            blockedUsers.add(user);
        }
    }
    public void unblockUser(User user) {
        synchronized(lock){
            blockedUsers.remove(user);
        }
    }
    public String getUsername() {
        return username;
    }
    private boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    public boolean verifyLogin(String password) {
        return checkPassword(password);
    }
    public boolean equals(User user) {
        return this.username.equals(user.username);
    }
    public String toString() {
        return username + ", " + firstName + ", " + lastName + ", " + Arrays.toString(profilePicture);
    }
    public BufferedImage getProfilePicture() {
        try {
            return ImageIO.read(new ByteArrayInputStream(profilePicture));
        } catch (Exception e) {
            return null;
        }
    }

    public void changeUsername(String newUsername) {
        this.username = newUsername;
    }
}
