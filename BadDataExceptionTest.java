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
 * @version November 3, 2024
 *
 */


public class BadDataExceptionTest {
    @Test
    public void testUserImplementsUserInt() {
        Class<?>[] interfaces = User.class.getInterfaces();
        boolean implementsUserInt = Arrays.asList(interfaces).contains(MessageInterface.class);
        assertTrue("User class should implement UserInt interface", implementsUserInt);
    }

    @Test
    public void testThrowingError() {
        BadDataException e = new BadDataException("failure");
        Assert.assertEquals("Exception of wrong type",BadDataException.class, e.getClass());
    }
}
