import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class UserDatabase {
    /*
    * User profiles.
New user account creation.
Password protected login.
User search.
User viewer.
Add, block, and remove friend features.
Extra credit opportunity – Add support to upload and display profile pictures.
*/
    private ArrayList<User> users;
    private String outputFile;
    private String messageFile;

    public UserDatabase(String outputFile, String messageFile) {
        // This is a constructor
        this.outputFile = outputFile;
        this.messageFile = messageFile;
        users = new ArrayList<User>();

    }
    public void createUser(String username, String password, String firstName, String lastName, String profilePicture) {
        // This method creates a new user
        User newUser = new User(username, password, firstName, lastName, profilePicture);
        users.add(newUser);
    }
    //add to database
    public void writeDB(User user) {
        // append user to database
        try {
            FileWriter writer = new FileWriter(outputFile, true);
            writer.write(user.toString() + "\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void removeFriend(User user, User friend) {
        // This method removes a friend from a user
        user.removeFriend(friend);
    }
    public void blockUser(User user, User blockedUser) {
        // This method blocks a user
        user.blockUser(blockedUser);
    }
    public void unblockUser(User user, User blockedUser) {
        // This method unblocks a user
        user.unblockUser(blockedUser);
    }
    public User getUser(String username) {
        // This method gets a user by their username
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    public boolean verifyLogin(String username, String password) {
        // This method verifies a user's login
        User user = getUser(username);
        if (user == null) {
            return false;
        }
        return user.verifyLogin(password);
    }
    //view user

}
