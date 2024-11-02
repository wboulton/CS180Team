import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.nio.file.*;

import static org.junit.Assert.*;

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
        user1.blockUser(user2);
        assertTrue("User2 should be blocked by user1.", user1.getBlockedUsers().contains(user2));

    }

    @Test
    public void testUnblockUser() {
        // Block user2 first

    }
}
