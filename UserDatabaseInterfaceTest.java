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
 * Team Project -- UserDatabaseInterfaceTest
 *
 * This file tests the UserDBInt class.
 *
 * @author Kush Kodiya
 *
 * @version November 3, 2024
 *
 */

public class UserDatabaseInterfaceTest {
    @Test
    public void testUserDatabaseImplementsUserDBInt() {
        Class<?>[] interfaces = UserDatabase.class.getInterfaces();
        boolean implementsUserDBInt = Arrays.asList(interfaces).contains(UserDBInt.class);
        assertTrue("UserDatabase class should implement UserDBInt interface", implementsUserDBInt);
    }
}
