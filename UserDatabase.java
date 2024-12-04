import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Files;
/**
 * Team Project -- UserDatabase
 *
 * This file handles the user data for each user.
 * For more in depth documentation see Docs/UserDataStorage.md
 *
 * @author William Boulton, Mukund Venkatesh
 *
 * @version November 1, 2024
 *
 */
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
    private static ArrayList<User> users;
    private static final String OUTPUT_FILE = "resources/users.txt";
    private static final Object LOCK = new Object();
    public UserDatabase() {
        // This is a constructor
        //if the files do not exist, create them
        try {
            File file = new File(OUTPUT_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        users = new ArrayList<User>();
        //if users file is not empty, load the database
        load();

    }

    public static String byteArrayToString(byte[] picture) {
        // This method converts a byte array to a string
        StringBuilder sb = new StringBuilder();
        for (byte b : picture) {
            sb.append(b);
            sb.append(",");
        }
        return sb.toString();
    }

    public User createUser(String username, String password, String firstName, String lastName, String profilePicture)
        throws BadDataException {
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
            throw new BadDataException("Password is not legal. It must be at least 8 characters, contain at least one"
                + " number, at least one Capital letter and at least one lowercase letter. '|' and ',' not allowed");
        }
        //if the profile picture is not found, throw exception
        //split the profile pic string by comma, and check if the file exists
        if (profilePicture != null && !profilePicture.equals("false")) {
            try {
                File imageFile = new File(profilePicture);
                byte[] imageData = Files.readAllBytes(imageFile.toPath());
            } catch (Exception e) {
                throw new BadDataException("Profile picture not found");
            }
        }
        User user = new User(username, password, firstName, lastName, profilePicture);
        writeDB(user);
        //create a new user
        return user;
        
    }
    //add to database
    public void writeDB(User user) {
        // append user to database
        synchronized (LOCK) {
            try {
                FileWriter writer = new FileWriter(OUTPUT_FILE, true);
                writer.write(user.toString() + "\n");
                //if the user is not already in the database, add it
                if (!users.contains(user)) {
                    users.add(user);
                }
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    //get users list
    public ArrayList<User> getUsers() {
        // This method gets the list of users
        return users;
    }
    //re-write entire DB
    public static void updateDB() {
        synchronized (LOCK) {
            try (BufferedWriter bwr = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
                for (User user : users) {
                    bwr.write(user.toString());
                    bwr.newLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static boolean addFriend(User user, User friend) {
        // This method adds a friend to a user
        synchronized (LOCK) {
            //if the user is not already a friend, add the friend
            if (!user.getFriends().contains(friend.getUsername())) {
                user.addFriend(friend.getUsername());
            } else {
                return false;
            }
            updateDB();
            //if the friend is added, return true, else return false
            return user.getFriends().contains(friend.getUsername());
        }
    }
    public static boolean removeFriend(User user, User friend) {
        //if the user is a friend, remove the friend
        synchronized (LOCK) {
            if (user.getFriends().contains(friend.getUsername())) {
                user.removeFriend(friend.getUsername());
                updateDB();
                return true;
            }
            return false;
        }
    }
    public static void blockUser(User user, User blockedUser) {
        // This method blocks a user
        synchronized (LOCK) {
            if (!user.getBlockedUsers().contains(blockedUser.getUsername())) {
                user.blockUser(blockedUser.getUsername());
                updateDB();
            }
            updateDB();
        }
    }
    
    public static boolean unblockUser(User user, User blockedUser) {
        // This method unblocks a user
        synchronized (LOCK) {
            if (user.getBlockedUsers().contains(blockedUser.getUsername())) {
                user.unblockUser(blockedUser.getUsername());
                updateDB();
                return true;
            }
            return false;
        }
    }

    public static User getUser(String username) {
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
        synchronized (LOCK) {
            User user = getUser(username);
            if (user == null) {
                return false;
            }
            return user.verifyLogin(password);
        }
    }
    //change username
    public boolean changeUsername(User user, String newUsername) {
        //if the username is not taken, change the username
        synchronized (LOCK) {
            if (getUser(newUsername) == null) {
                user.changeUsername(newUsername);
                updateDB();
                return true;
            }
            updateDB();
        }
        return false;
    }
    //change password
    public boolean changePassword(User user, String newPassword) {
        //if the password is legal, change the password
        synchronized (LOCK) {
            if (legalPassword(newPassword)) {
                user.changePassword(newPassword);
                return true;
            }
            return false;
        }
    }
    //legal password
    public static boolean legalPassword(String password) {

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
        synchronized (LOCK) {
            users.clear();
            try {
                Scanner scanner = new Scanner(new File(OUTPUT_FILE));
                while (scanner.hasNextLine()) {
                    User user = new User(scanner.nextLine());
                    //if the user isnt already in the database, add it
                    if (!users.contains(user)) {
                        users.add(user);
                    }
                }
                scanner.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public boolean validateUser(String username) {
        // This method validates a user
        //if username contains comma return false
        return !username.contains("|");
    }
    //add user
    public void addUser(User user) {
        // This method adds a user
        synchronized (LOCK) {
            if (!users.contains(user)) {
                users.add(user);
                updateDB();
            }
        }
    }
    private byte[] stringToByteArray(String string) {
        String[] stringArray = string.split(",");
        byte[] byteArray = new byte[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            byteArray[i] = Byte.parseByte(stringArray[i]);
        }
        return byteArray;
    }
    public static void changePicture(User user, String picture) {
        // This method changes a user's profile picture
        synchronized (LOCK) {
            byte[] pictureBytes = user.getProfilePicture();
            user.setProfilePicture(pictureBytes);
            updateDB();
        }
    }
}
