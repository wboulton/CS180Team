import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Files;

public class UserDatabase implements UserDBInt {
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
    private static final String outputFile = "users.txt";
    private static final String messageFile = "messages.txt";

    public UserDatabase() {
        // This is a constructor
        //if the files do not exist, create them
        try {
            File file = new File(outputFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            file = new File(messageFile);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        users = new ArrayList<User>();
        load();

    }
    public void createUser(String username, String password, String firstName, String lastName, String profilePicture)  throws BadDataException {
        // This method creates a new user
        //if the username is not taken, create a new user
        //if user already exists, throw exception
        if (getUser(username) != null) {
            throw new BadDataException("Username already exists");
        }
        //if the username is not valid, throw exception
        if (!validateUser(username)) {
            throw new BadDataException("Username is not valid. It cannot contain a comma");
        }
        //if password is not legal, throw exception
        if (!legalPassword(password)) {
            throw new BadDataException("Password is not legal. It must be at least 8 characters, contain at least one" +
                    " number, at least one Capital letter and at least one lowercase letter. '|' not allowed");
        }
        //if the profile picture is not found, throw exception
        try {
            File imageFile = new File(profilePicture);
            byte[] imageData = Files.readAllBytes(imageFile.toPath());
        } catch (Exception e) {
            throw new BadDataException("Profile picture not found");
        }
        //create a new user
        User user = new User(username, password, firstName, lastName, profilePicture);
        users.add(user);
        writeDB(user);
        
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
    public void addFriend(User user, User friend) {
        // This method adds a friend to a user
        user.addFriend(friend);
    }
    public void removeFriend(User user, User friend) {
        //if the user is a friend, remove the friend
        if (user.getFriends().contains(friend)) {
            user.removeFriend(friend);
        }
    }
    public void blockUser(User user, User blockedUser) {
        // This method blocks a user
        user.blockUser(blockedUser);
    }
    public boolean unblockUser(User user, User blockedUser) {
        // This method unblocks a user
        return user.unblockUser(blockedUser);
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
        if (getUser(newUsername) == null) {
            user.changeUsername(newUsername);
        }
    }
    //legal password
    public boolean legalPassword(String password) {

        // This method checks if a password is legal - at least 8 characters, 
        //at least one number, at least one Capital letter and at least one lowercase letter
        //if password contains comma return false
        if (password.contains("|")) {
            return false;
        }
        return password.length() >= 8 && password.matches(".*[0-9].*") 
        && password.matches(".*[A-Z].*") && password.matches(".*[a-z].*");
    }

    //load function for when the program starts
    public void load() {
        // This method loads the database
        try {
            Scanner scanner = new Scanner(new File(outputFile));
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split("|");
                User user = new User(data[0], data[1], data[2], data[3], data[6]);
                ArrayList<User> friends = new ArrayList<>();
                //go through the data[4], split it by comma, and add to friends the users from getUser
                for (String friend : data[4].split(",")) {
                    User friendUser = getUser(friend);
                    if (friendUser != null) {
                        friends.add(friendUser);
                    }
                }
                //same for blocked, data[5]
                ArrayList<User> blocked = new ArrayList<>();
                for (String blockedUser : data[5].split(",")) {
                    User blockedUserUser = getUser(blockedUser);
                    if (blockedUserUser != null) {
                        blocked.add(blockedUserUser);
                    }
                }
                users.add(user);
            }
            scanner.close();
        } catch (Exception e) {
            return;
        }
    }
    public boolean validateUser(String username) {
        // This method validates a user
        //if username contains comma return false
        return !username.contains("|");
    }
}
