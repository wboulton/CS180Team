import org.junit.Assert;
import org.junit.Test;

import java.beans.Transient;
import java.io.*;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Team Project -- UserInterfaceTest.java
 *
 * this file creates test cases for the user interface
 *
 * @author William Boulton
 *
 * @version November 1, 2024
 *
 */

public class MessageInterfaceTest {
//this test case pulls the interfaces that the User class implements and stores them in the interfaces array
//it then checks if it implements userInt and uses assertTrue
    @Test
    public void testUserImplementsUserInt() {
        Class<?>[] interfaces = User.class.getInterfaces();
        boolean implementsUserInt = Arrays.asList(interfaces).contains(MessageInterface.class);
        assertTrue("User class should implement UserInt interface", implementsUserInt);
    }
}
