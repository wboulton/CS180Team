import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.util.ArrayList;
import java.io.*;
import java.util.Arrays;
public class User {
    private static UserDatabase userDatabase;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private ArrayList<User> friends;
    private ArrayList<User> blockedUsers;
    //profile picture
    private byte[] profilePicture;

    // you probably want a constructor which can take in a csv line from the database and make a user based on that
    public User(String username, String password, String firstName, String lastName, String profilePicture) {
        //username rules - no commas, doesn't already exist, not empty
        //if userDatabase is null, create a new userDatabase
        if (userDatabase == null) {
            userDatabase = new UserDatabase("users.txt", "messages.txt");
        }
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
    public boolean addFriend(User user) {
        //if user is not blocked, add to friends
        if (!blockedUsers.contains(user)) {
            friends.add(user);
            return true;
        }
        return false;
    }
    public boolean removeFriend(User user) {
        return friends.remove(user);
    }
    public boolean blockUser(User user) {
        //if user doesnt exist at all, return false
        if(!userDatabase.getUser(user.username).equals(user))
            return false;
        blockedUsers.add(user);
        //if user is a friend, remove from friends
        if (friends.contains(user)) {
            friends.remove(user);
        }
        return true;
    }
    public boolean unblockUser(User user) {
        //if user doesnt exist in blocked users or in the user database, return false
        if (!blockedUsers.contains(user) || !userDatabase.getUser(user.username).equals(user)) {
            return false;
        }
        blockedUsers.remove(user);
        return true;
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

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public ArrayList<User> getBlockedUsers() {
        return blockedUsers;
    }

    public void changeUsername(String newUsername) {
        this.username = newUsername;
    }
}
