import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Files;

import java.lang.*;
import java.util.Scanner;
import java.nio.file.Files;

public class UserDatabase {
    /*
     * User profiles.
     * New user account creation.
     * Password protected login.
     * User search.
     * User viewer.
     * Add, block, and remove friend features.
     * Extra credit opportunity – Add support to upload and display profile
     * pictures.
     */
    private ArrayList<User> users;
    private String outputFile;
    private String messageFile;
    public static final Object lock = new Object();

    public UserDatabase(String outputFile, String messageFile) {
        // This is a constructor
        this.outputFile = outputFile;
        this.messageFile = messageFile;
        load();
        users = new ArrayList<User>();

    }

    public void createUser(String username, String password, String firstName, String lastName, String profilePicture)
            throws BadDataException {
        // This method creates a new user
        // if the username is not taken, create a new user
        // if user already exists, throw exception
        if (getUser(username) != null) {
            throw new BadDataException("Username already exists");
        }
        // if password is not legal, throw exception
        if (!legalPassword(password)) {
            throw new BadDataException(
                    "Password is not legal. It must be at least 8 characters, contain at least one number, at least one Capital letter and at least one lowercase letter");
        }

        // if the profile picture is not found, throw exception
        try {
            File imageFile = new File(profilePicture);
            byte[] imageData = Files.readAllBytes(imageFile.toPath());
        } catch (Exception e) {
            throw new BadDataException("Profile picture not found");
        }
        synchronized (lock) {
            // create a new user
            User user = new User(username, password, firstName, lastName, profilePicture);
            users.add(user);
            writeDB(user);
        }

    }

    // add to database
    public void writeDB(User user) {
        // append user to database
        synchronized (lock) {
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
        synchronized (lock) {
            user.removeFriend(friend);
        }
    }

    public void blockUser(User user, User blockedUser) {
        // This method blocks a user
        synchronized (lock) {
            user.blockUser(blockedUser);
        }
    }

    public boolean unblockUser(User user, User blockedUser) {
        // This method unblocks a user

        synchronized (lock) {
            return user.unblockUser(blockedUser);
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

    // change username
    public void changeUsername(User user, String newUsername) {
        // if the username is not taken, change the username
        synchronized (lock) {
            if (getUser(newUsername) == null) {
                user.changeUsername(newUsername);
            }
        }
    }

    // legal password
    public boolean legalPassword(String password) {

        // This method checks if a password is legal - at least 8 characters,
        // at least one number, at least one Capital letter and at least one lowercase
        // letter
        return password.length() >= 8 && password.matches(".*[0-9].*")
                && password.matches(".*[A-Z].*") && password.matches(".*[a-z].*");
    }

    // load function for when the program starts
    public void load() {
        // This method loads the database
        try {
            synchronized (lock) {
                Scanner scanner = new Scanner(new File(outputFile));
                while (scanner.hasNextLine()) {
                    String[] data = scanner.nextLine().split(", ");
                    User user = new User(data[0], data[1], data[2], data[3], data[4]);
                    users.add(user);
                }
                scanner.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
