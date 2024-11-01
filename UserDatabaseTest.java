import org.junit.*;
import java.io.*;
import java.nio.file.*;

public class UserDatabaseTest {

    private UserDatabase userDatabase;
    private static final String OUTPUT_FILE = "test_users.txt";
    private static final String MESSAGE_FILE = "test_messages.txt";

    @BeforeEach
    void setUp() throws BadDataException {
        // Create a new UserDatabase instance before each test
        userDatabase = new UserDatabase(OUTPUT_FILE, MESSAGE_FILE);
        user1 = new User("user1", "Password123", "First1", "Last1", "profile1.jpg");
        user2 = new User("user2", "Password456", "First2", "Last2", "profile2.jpg");

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
    void testCreateUser() throws BadDataException {
        userDatabase.createUser("johnDoe", "Password1", "John", "Doe", "path/to/profile.jpg");
        User user = userDatabase.getUser("johnDoe");
        assertNotNull(user);
        assertEquals("johnDoe", user.getUsername());
    }

    @Test
    void testCreateUserWithExistingUsername() {
        assertThrows(BadDataException.class, () -> {
            userDatabase.createUser("johnDoe", "Password1", "John", "Doe", "path/to/profile.jpg");
            userDatabase.createUser("johnDoe", "Password2", "Johnny", "Doe", "path/to/profile2.jpg");
        });
    }

    @Test
    void testCreateUserWithInvalidPassword() {
        assertThrows(BadDataException.class, () -> {
            userDatabase.createUser("janeDoe", "short", "Jane", "Doe", "path/to/profile.jpg");
        });
    }

    @Test
    void testVerifyLoginSuccess() throws BadDataException {
        userDatabase.createUser("loginUser", "Password1", "Login", "User", "path/to/profile.jpg");
        assertTrue(userDatabase.verifyLogin("loginUser", "Password1"));
    }

    @Test
    void testVerifyLoginFailure() throws BadDataException {
        userDatabase.createUser("wrongUser", "Password1", "Wrong", "User", "path/to/profile.jpg");
        assertFalse(userDatabase.verifyLogin("wrongUser", "WrongPassword"));
    }

    @Test
    void testLegalPassword() {
        assertTrue(userDatabase.legalPassword("Valid1Password"));
        assertFalse(userDatabase.legalPassword("invalid")); // no uppercase, no number
        assertFalse(userDatabase.legalPassword("NoNumber!")); // no number
    }

    @Test
    void testBlockUser() {
        // Add user2 as a friend to user1
        user1.addFriend(user2);
        assertTrue(user1.getFriends().contains(user2), "User2 should be in User1's friends.");

        // Block user2
        assertTrue(user1.blockUser(user2), "User2 should be blocked successfully.");
        assertTrue(user1.getBlockedUsers().contains(user2), "User2 should be in User1's blocked users.");
        assertFalse(user1.getFriends().contains(user2), "User2 should have been removed from User1's friends after blocking.");
    }

    @Test
    void testUnblockUser() {
        // Block user2 first
        user1.blockUser(user2);
        assertTrue(user1.getBlockedUsers().contains(user2), "User2 should be blocked.");

        // Unblock user2
        assertTrue(user1.unblockUser(user2), "User2 should be unblocked successfully.");
        assertFalse(user1.getBlockedUsers().contains(user2), "User2 should no longer be blocked.");
    }
}
