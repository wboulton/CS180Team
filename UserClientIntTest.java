import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.Assert.*;
/**
 * Team Project -- UserClientInterfaceTest
 *
 * This file tests the UserClientInt class.
 *
 * @author Jai Menon
 *
 * @version November 8, 2024
 *
 */

public class UserClientIntTest {
    @Test
    public void testInterfaceExists() {
        try {
            Class.forName("UserClientInt");
        } catch (ClassNotFoundException e) {
            fail("UserClientInt interface should exist");
        }
    }
    @Test
    public void testUserClientImplementsUserClientInt() {
        Class<?>[] interfaces = UserClient.class.getInterfaces();
        boolean implementsMData = Arrays.asList(interfaces).contains(UserClientInt.class);
        assertTrue("UserClient class should implement UserClientInt interface", implementsMData);
    }
}
