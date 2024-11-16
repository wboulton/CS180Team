/**
 * Interface for UserDB
 * @author Mukund Venkatesh
 * @version November 1, 2024
 */
public interface UserDBInt {
    User createUser(String username, String password, String firstName, String lastName, String profilePicture)
        throws BadDataException;

    static void removeFriend(User user, User friend) {

    }

    static void addFriend(User user, User friend) {

    }

    static void blockUser(User user, User blockedUser) {

    }

    static boolean unblockUser(User user, User blockedUser) {
        return false;
    }

    static User getUser(String username) {
        return null;
    }

    boolean verifyLogin(String username, String password);
    void changeUsername(User user, String newUsername);

    static boolean legalPassword(String password) {
        return false;
    }

    void writeDB(User user);

    static void updateDB() {

    }

    void load();
}
