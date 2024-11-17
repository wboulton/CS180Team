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
 * Team Project -- ServerInterfaceTest
 *
 * This file tests the ServerInterface class.
 *
 * @author Jai Menon
 *
 * @version November 8, 2024
 *
 */

public class ServerInterfaceTest {
    @Test
    public void testMediaServerImplementsServerInterface() {
        Class<?>[] interfaces = ServerInterface.class.getInterfaces();
        boolean implementsMData = Arrays.asList(interfaces).contains(ServerInterface.class);
        assertTrue("MediaServer class should implement ServerInterface interface", implementsMData);
    }
}
