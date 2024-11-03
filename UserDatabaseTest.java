import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.nio.file.*;

import static org.junit.Assert.*;
/**
 * Team Project -- UserDatabaseTest
 *
 * This file tests the UserDatabase class.
 * For more in depth documentation see Docs/UserDataStorage.md
 *
 * @author Kush Kodiya, Mukund Venkatesh
 *
 * @version November 1, 2024
 *
 */
public class UserDatabaseTest {

    private static UserDatabase userDatabase = new UserDatabase();
    private static final String OUTPUT_FILE = "users.txt";

    @AfterAll
    static void tearDown() throws Exception {
        // Clean up by deleting the output file after each test
        Files.deleteIfExists(Paths.get(OUTPUT_FILE));
    }


    private void testCreateUser() throws BadDataException {
        userDatabase.createUser("johnDoe", "Password1", "John", "Doe", "false");
        User user = userDatabase.getUser("johnDoe");
        assertNotNull(user);
        assertEquals("johnDoe", user.getUsername());
    }

    //do this only after testCreateUser
    private void testCreateUserWithExistingUsername() {
        assertThrows(BadDataException.class, () -> {
            userDatabase.createUser("johnDoe", "Password1", "John", "Doe", "false");
            userDatabase.createUser("johnDoe", "Password2", "Johnny", "Doe", "false");
        });
    }
    @Test (timeout = 1000)
    public void testUserCreation() throws BadDataException {
        testCreateUser();
        testCreateUserWithExistingUsername();
    }
    @Test (timeout = 1000)
    public void testCreateUserWithInvalidPassword() {
        assertThrows(BadDataException.class, () -> {
            userDatabase.createUser("janeDoe", "short", "Jane", "Doe", "false");
        });

        assertThrows(BadDataException.class, () -> {
            userDatabase.createUser("johnDoe", "Password|123", "John", "Doe", "false");
        });
    }

    @Test (timeout = 1000)
    public void testVerifyLoginSuccess() throws BadDataException {
        userDatabase.createUser("loginUser", "Password1", "Login", "User", "false");
        assertTrue(userDatabase.verifyLogin("loginUser", "Password1"));
    }

    @Test (timeout = 1000)
    public void testVerifyLoginFailure() throws BadDataException {
        userDatabase.createUser("wrongUser", "Password1", "Wrong", "User", "false");
        assertFalse(userDatabase.verifyLogin("wrongUser", "WrongPassword"));
    }

    @Test (timeout = 1000)
    public void testLegalPassword() {
        assertTrue(userDatabase.legalPassword("Valid1Password"));
        assertFalse(userDatabase.legalPassword("invalid")); // no uppercase, no number
        assertFalse(userDatabase.legalPassword("NoNumber!")); // no number
    }

    @Test (timeout = 1000)
    public void testBlockUser() throws BadDataException {
        // Add user2 as a friend to user1
        User user1 = userDatabase.createUser("user1", "Password1", "User", "One", "false");
        User user2 = userDatabase.createUser("user2", "Password2", "User", "Two", "false");
        UserDatabase.blockUser(user1, user2);
        assertTrue("User2 should be blocked by user1.", user1.getBlockedUsers().contains(user2));

    }

    @Test (timeout = 1000)
    public void testSaveAndLoadUsers() {
        try {
            // Create users and add to file
            userDatabase.createUser("johnDoe", "Password123", "John", "Doe", "false");
            userDatabase.createUser("janeDoe", "Password456", "Jane", "Doe", "false");

            // Reload the database from the file
            userDatabase = new UserDatabase();
            assertNotNull(userDatabase.getUser("johnDoe"));
            assertNotNull(userDatabase.getUser("janeDoe"));
        } catch (BadDataException e) {
            fail("Unexpected exception during save/load test: " + e.getMessage());
        }
    }

    @Test (timeout = 1000)
    public void testAddAndRemoveFriends() {
        try {
            User user1 = userDatabase.createUser("johnDoe", "Password123", "John", "Doe", "false");
            User user2 = userDatabase.createUser("janeDoe", "Password456", "Jane", "Doe", "false");

            // Add friend
            userDatabase.addFriend(user1, user2);
            assertTrue(user1.getFriends().contains(user2));

            // Remove friend
            userDatabase.removeFriend(user1, user2);
            assertFalse(user1.getFriends().contains(user2));
        } catch (BadDataException e) {
            fail("Friend addition/removal test failed: " + e.getMessage());
        }
    }

    @Test (timeout = 1000)
    public void testBlockAndUnblockUser() {
        try {
            User user1 = userDatabase.createUser("johnDoe", "Password123", "John", "Doe", "false");
            User user2 = userDatabase.createUser("janeDoe", "Password456", "Jane", "Doe", "false");

            // Block user
            userDatabase.blockUser(user1, user2);
            assertTrue(user1.getBlockedUsers().contains(user2));

            // Unblock user
            boolean unblocked = userDatabase.unblockUser(user1, user2);
            assertTrue(unblocked);
            assertFalse(user1.getBlockedUsers().contains(user2));
        } catch (BadDataException e) {
            fail("Block/unblock test failed: " + e.getMessage());
        }
    }

    @Test (timeout = 1000)
    public void testChangeUsername() {
        try {
            User user = userDatabase.createUser("johnDoe", "Password123", "John", "Doe", "false");
            userDatabase.changeUsername(user, "johnSmith");

            assertNull(userDatabase.getUser("johnDoe"));
            assertNotNull(userDatabase.getUser("johnSmith"));
            assertEquals("johnSmith", user.getUsername());

            // Attempt to change to an existing username
            userDatabase.createUser("janeDoe", "Password456", "Jane", "Doe", "false");
            userDatabase.changeUsername(user, "janeDoe");
            assertEquals("johnSmith", user.getUsername(), "Username should not change to an existing username");
        } catch (BadDataException e) {
            fail("Unexpected exception during username change test: " + e.getMessage());
        }
    }

    @Test
    public void testUnblockUser() {
        // Block user2 first

    }


}
