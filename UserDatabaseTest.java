import org.junit.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.nio.file.*;

import static org.junit.Assert.*;

public class UserDatabaseTest {

    private UserDatabase userDatabase;
    private static final String OUTPUT_FILE = "test_users.txt";
    private static final String MESSAGE_FILE = "test_messages.txt";
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() throws BadDataException, IOException {
        // Create a new UserDatabase instance before each test
        userDatabase = new UserDatabase();
        user1 = new User("user1", "Password123", "First1", "Last1", "false");
        user2 = new User("user2", "Password456", "First2", "Last2", "false");

        // Add users to the user database to simulate a real scenario
        userDatabase.createUser(user1.getUsername(), user1.getPassword(), user1.getFirstName(), user1.getLastName(), "profile1.jpg");
        userDatabase.createUser(user2.getUsername(), user2.getPassword(), user2.getFirstName(), user2.getLastName(), "profile2.jpg");
        // Clear the output file for a clean slate
        Files.deleteIfExists(Paths.get(OUTPUT_FILE));
    }

    @AfterEach
    void tearDown() throws Exception {
        // Clean up by deleting the output file after each test
        Files.deleteIfExists(Paths.get(OUTPUT_FILE));
    }

    @Test
    public void testCreateUser() throws BadDataException {
        userDatabase.createUser("johnDoe", "Password1", "John", "Doe", "path/to/profile.jpg");
        User user = userDatabase.getUser("johnDoe");
        assertNotNull(user);
        assertEquals("johnDoe", user.getUsername());
    }

    @Test
    public void testCreateUserWithExistingUsername() {
        assertThrows(BadDataException.class, () -> {
            userDatabase.createUser("johnDoe", "Password1", "John", "Doe", "path/to/profile.jpg");
            userDatabase.createUser("johnDoe", "Password2", "Johnny", "Doe", "path/to/profile2.jpg");
        });
    }

    @Test
    public void testCreateUserWithInvalidPassword() {
        assertThrows(BadDataException.class, () -> {
            userDatabase.createUser("janeDoe", "short", "Jane", "Doe", "path/to/profile.jpg");
        });
    }

    @Test
    public void testVerifyLoginSuccess() throws BadDataException {
        userDatabase.createUser("loginUser", "Password1", "Login", "User", "path/to/profile.jpg");
        assertTrue(userDatabase.verifyLogin("loginUser", "Password1"));
    }

    @Test
    public void testVerifyLoginFailure() throws BadDataException {
        userDatabase.createUser("wrongUser", "Password1", "Wrong", "User", "path/to/profile.jpg");
        assertFalse(userDatabase.verifyLogin("wrongUser", "WrongPassword"));
    }

    @Test
    public void testLegalPassword() {
        assertTrue(userDatabase.legalPassword("Valid1Password"));
        assertFalse(userDatabase.legalPassword("invalid")); // no uppercase, no number
        assertFalse(userDatabase.legalPassword("NoNumber!")); // no number
    }

    @Test
    public void testBlockUser() {
        // Add user2 as a friend to user1
        user1.addFriend(user2);
        assertTrue("User2 should be in User1's friends.", user1.getFriends().contains(user2));

        // Block user2
        assertTrue("User2 should be blocked successfully.", user1.blockUser(user2));
        assertTrue("User2 should be in User1's blocked users.", user1.getBlockedUsers().contains(user2));
        assertFalse("User2 should have been removed from User1's friends after blocking.", user1.getFriends().contains(user2));
    }

    @Test
    public void testUnblockUser() {
        // Block user2 first
        user1.blockUser(user2);
        assertTrue("User2 should be blocked.", user1.getBlockedUsers().contains(user2));

        // Unblock user2
        assertTrue("User2 should be unblocked successfully.", user1.unblockUser(user2));
        assertFalse("User2 should no longer be blocked.", user1.getBlockedUsers().contains(user2));
    }
}
