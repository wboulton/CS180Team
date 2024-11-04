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
 * Team Project -- UserDatabaseInterfaceTest
 *
 * This file tests the UserDBInt class.
 *
 * @author Kush Kodiya
 *
 * @version November 3, 2024
 *
 */

public class MessageDatabaseInterfaceTest {
    @Test
    public void testMessageDatabaseImplementsMData() {
        Class<?>[] interfaces = MessageDatabase.class.getInterfaces();
        boolean implementsMData = Arrays.asList(interfaces).contains(MData.class);
        assertTrue("MessageDatabase class should implement MData interface", implementsMData);
    }
}