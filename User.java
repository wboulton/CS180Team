import javax.imageio.ImageIO;
import java.nio.file.Files;
import java.util.ArrayList;
import java.io.*;
import java.util.Arrays;

/**
 * Team Project -- User
 *
 * This file handles the user data for each user.
 * For more in depth documentation see Docs/UserDataStorage.md
 *
 * @author William Boulton, Mukund Venkatesh, Alan Yi, Jai Menon, Kush Kodiya
 *
 * @version November 1, 2024
 *
 */
public class User implements UserInt {
    private static UserDatabase userDatabase = new UserDatabase();
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private ArrayList<String> friends;
    private ArrayList<String> blockedUsers;
    public static final Object LOCK = new Object();
    //profile picture
    private byte[] profilePicture;
    private boolean allowAll;

//line format: username|password|firstName|lastName|friends|blockedUsers|profilePicture|allowAll
    public User(String line) {
        String[] info = line.split("\\|");
        username = info[0];
        password = info[1];
        firstName = info[2];
        lastName = info[3];
        this.friends = new ArrayList<String>();
        this.blockedUsers = new ArrayList<String>();
        //add friends
        String[] friendGroup = info[4].split(",");
        for (String friend : friendGroup) {
            if (friend != null) {
                this.friends.add(friend);
            }
        }
        System.out.println("Friends for" + username + ":" + this.friends);
        //add blocked users
        String[] blockedPeople = info[5].split(",");
        for (String blockedUser : blockedPeople) {
            if (blockedUser != null) {
                this.blockedUsers.add(blockedUser);
            }
        }
        System.out.println("Blocked Users for " + username + ":"  + this.blockedUsers);
        if (!info[6].contains(",")) {
            this.profilePicture = null;
        } else {
            //parse profile picture
            String[] profileInfo = info[6].split(",");
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
        allowAll = Boolean.parseBoolean(info[7]);
        userDatabase.addUser(this);
    }
    // you probably want a constructor which can take in a csv line from the database and make a user based on that
    public User(String username, String password, String firstName, String lastName, String profile) {
        //username rules - no commas, doesn't already exist, not empty
        //if userDatabase is null, create a new userDatabase
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.friends = new ArrayList<String>();
        this.blockedUsers = new ArrayList<String>();
        //if there is no comma in the profile, there is no profile picture
        if (!profile.contains(",") || profile.equals("null")) {
            this.profilePicture = null;
        } else {
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
        // allow all is set to true by default
        allowAll = true;
        userDatabase.addUser(this);
    }

    public boolean addFriend(String user) {
        //if user is not blocked, add to friends
        synchronized (LOCK) {
            if (!blockedUsers.contains(user)) {
                friends.add(user);
                return true;
            }
            return false;
        }
    }
    public boolean removeFriend(String user) {
        synchronized (LOCK) {
            return friends.remove(user);
        }
    }
    public boolean blockUser(String user) {
        //if user doesnt exist at all, return false
        synchronized (LOCK) {
            blockedUsers.add(user);
          //if user is a friend, remove from friends
            if (friends.contains(user)) {
                friends.remove(user);
            }
            return true;
        }
    }
    public boolean unblockUser(String user) {
        //if user doesnt exist in blocked users or in the user database, return false
        synchronized (LOCK) {
            if (!blockedUsers.contains(user)) {
                return false;
            }
            blockedUsers.remove(user);
            return true;
        }
    }
    public String getUsername() {
        return username;
    }
    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
    public boolean verifyLogin(String enteredPassword) {
        return checkPassword(enteredPassword);
    }
    public boolean equals(User user) {
        return this.username.equals(user.username);
    }
    public String toString() {
        return String.format("%s|%s|%s|%s|%s|%s|%s|%b", username, password, firstName,
            lastName, stringListToString(friends), stringListToString(blockedUsers),
            Arrays.toString(profilePicture), allowAll);
    }
    
    public String stringListToString(ArrayList<String> list) {
        //only take the usernames and put it in a list which is comma separated
        StringBuilder sb = new StringBuilder();
        for (String user : list) {
            sb.append(user).append(",");
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
    }
    
    public byte[] getProfilePicture() {
        try {
            return profilePicture;
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

    public ArrayList<String> getFriends() {
        return friends;
    }
    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }
    public void setBlockedUsers(ArrayList<String> blockedUsers) {
        this.blockedUsers = blockedUsers;
    }

    public ArrayList<String> getBlockedUsers() {
        return blockedUsers;
    }

    public void changeUsername(String newUsername) {
        this.username = newUsername;
    }
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
    public boolean isAllowAll() {
        return allowAll;
    }
    public void setAllowAll(boolean newBoolean) {
        allowAll = newBoolean;
    }
}
