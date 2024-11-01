import org.junit.*;

import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UserTest {
    private User user1;
    private User user2;

    @Before
    public void setUp() {
        user1 = new User("user1", "User1Password@", "User", "One", "pfp");
        user2 = new User("user2", "User2Password@", "User", "Two", "pfp");

        // Assuming user1 has a blockedUsers and friends list
    }

    @Test
    public void testAddFriendNotBlocked() {
        // Test adding a user that is not blocked
        boolean result = user1.addFriend(user2);

        assertTrue(result, "Expected addFriend to return true when user is not blocked");
        assertTrue(user1.getFriends().contains(user2), "Expected friends list to contain the new user");
    }

    @Test
    public void testAddFriendBlocked() {
        // Add user2 to blocked list
        user1.getBlockedUsers().add(user2);

        // Try adding a blocked user
        boolean result = user1.addFriend(user2);

        assertFalse(result, "Expected addFriend to return false when user is blocked");
        assertFalse(user1.getFriends().contains(user2), "Expected friends list not to contain the blocked user");
    }

    @Test
    public void testRemoveFriend() {
        user1.addFriend(user2);

        boolean result = user1.removeFriend(user2);

        assertTrue(result, "Expected removeFriend to return true when user removed friend");
        assertFalse(user1.getFriends().contains(user2), "Expected friends list not to contain the removed user");

    }

    @Test
    public void testBlockUser() {

        boolean result = user1.blockUser(user2);

        assertTrue(result, "Expected user to be added to block list");
    }

    @Test
    public void testUnblockUser() {
        user1.blockUser(user2);

        boolean result = user1.unblockUser(user2);

        assertTrue(result, "Expected user to be in blocked users");
    }

    @Test
    public void testEquals() {
        User user3 = new User("user1", "User1Password@", "User", "One", "pfp");

        assertEquals("Expected user1 to be equal to user3", TRUE, user1.equals(user3));
    }

    @Test
    public void testCheckPassword() {
        assertEquals("Expected user1 password to be equal to User1Password@", TRUE, new Boolean[]{user1.checkPassword("User1Password@")});
    }

    @Test
    public void testVerifyLogin() {
        assertEquals("Expected user1 password to be User1Password@", TRUE, new Boolean[]{user1.verifyLogin("User1Password@")});
    }

    

}