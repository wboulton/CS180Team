import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.nio.file.*;
import java.util.Objects;

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
        User user1 = userDatabase.createUser("user3", "Password1", "User", "One", "false");
        User user2 = userDatabase.createUser("user4", "Password2", "User", "Two", "false");
        UserDatabase.blockUser(user1, user2);
        assertTrue("User3 should be blocked by user4.", user1.getBlockedUsers().contains(user2.getUsername()));

    }

    @Test (timeout = 1000)
    public void testSaveAndLoadUsers() {
        try {
            // Create users and add to file
            userDatabase.createUser("joeDoe", "Password123", "John", "Doe", "false");
            userDatabase.createUser("joeyDoe", "Password456", "Jane", "Doe", "false");

            // Reload the database from the file
            userDatabase = new UserDatabase();
            assertNotNull(userDatabase.getUser("joeDoe"));
            assertNotNull(userDatabase.getUser("joeyDoe"));
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
            assertTrue(user1.getFriends().contains(user2.getUsername()));

            // Remove friend
            userDatabase.removeFriend(user1, user2);
            assertFalse(user1.getFriends().contains(user2.getUsername()));
        } catch (BadDataException e) {
            fail("Friend addition/removal test failed: " + e.getMessage());
        }
    }

    @Test (timeout = 1000)
    public void testBlockAndUnblockUser() {
        try {
            User user1 = userDatabase.createUser("jackDoe", "Password123", "John", "Doe", "false");
            User user2 = userDatabase.createUser("jillDoe", "Password456", "Jane", "Doe", "false");

            // Block user
            userDatabase.blockUser(user1, user2);
            assertTrue(user1.getBlockedUsers().contains(user2.getUsername()));

            // Unblock user
            boolean unblocked = userDatabase.unblockUser(user1, user2);
            assertTrue(unblocked);
            assertFalse(user1.getBlockedUsers().contains(user2.getUsername()));
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
            assertEquals("johnSmith", user.getUsername());
        } catch (BadDataException e) {
            fail("Unexpected exception during username change test: " + e.getMessage());
        }
    }

    @Test
    public void testUnblockUser() {
        // Block user2 first

    }

//Here is a test case for file writing as an example
    @Test
    public void testFileWriting() {
        //create a users.txt file that contains the lines of two users, friend them, then write again
        String filepath = "users.txt";
        try (BufferedWriter bwr = new BufferedWriter(new FileWriter(filepath))) {
//line format: username|password|firstName|lastName|friends|blockedUsers|profilePicture|allowAll
            bwr.write("user1|pass|will|youthought|null|null|null|false");
            bwr.newLine();
            bwr.write("user2|password|kush|something|null|null|null|true");
        } catch (Exception e) {
            System.out.println("preparation failed");
        }
        UserDatabase aDatabase = new UserDatabase();
        UserDatabase.addFriend(Objects.requireNonNull(UserDatabase.getUser("user1")), Objects.requireNonNull(UserDatabase.getUser("user2")));
        aDatabase.updateDB();
        try (BufferedReader bfr = new BufferedReader(new FileReader(filepath))) {
            String line1 = bfr.readLine();
            assertEquals("user1|pass|will|youthought|null,user2|null|null|false", line1);
            String line2 = bfr.readLine();
            assertEquals("user2|password|kush|something|null|null|null|true", line2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
