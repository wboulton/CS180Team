import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.util.ArrayList;
import java.io.*;
import java.util.Arrays;
public class User implements UserInt {
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
    public User(String username, String password, String firstName, String lastName, String profile) {
        //username rules - no commas, doesn't already exist, not empty
        //if userDatabase is null, create a new userDatabase
        if (userDatabase == null) {
            userDatabase = new UserDatabase();
        }
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.friends = new ArrayList<User>();
        this.blockedUsers = new ArrayList<User>();
        //if there is no comma in the profile, there is no profile picture
        if (!profile.contains(",")) {
            this.profilePicture = null;
            return;
        }
        //parse profile picture
        String[] profileInfo = profile.split(",");
        boolean containsPicture = Boolean.parseBoolean(profileInfo[0]);
        if (containsPicture) {
            try {
                File imageFile = new File(profileInfo[1]);
                profilePicture = Files.readAllBytes(imageFile.toPath());
            } catch (Exception e) {
                this.profilePicture = null;
            }
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

    public void changeUsername(String newUsername) {
        this.username = newUsername;
    }
}
