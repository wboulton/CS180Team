import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.lang.*;

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
    public static final Object lock = new Object();

    public UserDatabase(String outputFile, String messageFile) {
        // This is a constructor
        this.outputFile = outputFile;
        this.messageFile = messageFile;
        users = new ArrayList<User>();

    }
    public void createUser(String username, String password, String firstName, String lastName, String profilePicture) {
        // This method creates a new user
        //if the username is not taken, create a new user
        synchronized(lock){
            if (getUser(username) == null) {
                User user = new User(username, password, firstName, lastName, profilePicture);
                users.add(user);
                writeDB(user);
            } else {
                System.out.println("Username is taken");
            }
        }
    }
    //add to database
    public void writeDB(User user) {
        // append user to database
        synchronized(lock){
            try {
                FileWriter writer = new FileWriter(outputFile, true);
                writer.write(user.toString() + "\n");
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void removeFriend(User user, User friend) {
        // This method removes a friend from a user
        synchronized(lock){
            user.removeFriend(friend);
        }
    }
    public void blockUser(User user, User blockedUser) {
        // This method blocks a user
        synchronized(lock){
            user.blockUser(blockedUser);
        }
    }
    public void unblockUser(User user, User blockedUser) {
        // This method unblocks a user
        synchronized(lock){
            user.unblockUser(blockedUser);
        }
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
    //change username
    public void changeUsername(User user, String newUsername) {
        //if the username is not taken, change the username
        synchronized(lock){
            if (getUser(newUsername) == null) {
                user.changeUsername(newUsername);
            }
        }
    }
}
