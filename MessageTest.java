import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.*;
/**
 * Team Project -- MessageTest
 *
 * This file tests the Message class.
 * For more in depth documentation see Docs/MessageDataStorage.md
 *
 * @author Mukund Venkatesh, Jai Menon, William Boulton
 *
 * @version November 1, 2024
 *
 */
public class MessageTest {
    //junit test case with the format given below
    /*
    @Test(timeout = 1000)
public void testExpectedOne() {
    // Set the input
    // Separate each input with a newline (\n).
    String input = "Line One\nLine Two\n";

    // Pair the input with the expected result
    String expected = "Insert the expected output here"

    // Runs the program with the input values
    // Replace TestProgram with the name of the class with the main method
    receiveInput(input);
    TestProgram.main(new String[0]);

    // Retrieves the output from the program
    String stuOut = getOutput();

    // Trims the output and verifies it is correct.
    stuOut = stuOut.replace("\r\n", "\n");
    assertEquals("Error message if output is incorrect, customize as needed",
                    expected.trim(), stuOut.trim());

}
The test cases should do the following:

Verify all fields, constructors, and methods function properly.
Methods and constructors should have error tests to verify they do not crash when receiving invalid input, where applicable.
Data that persists should be validated with appropriate test cases.
When designing your implementation, be sure to use methods appropriately. It will be challenging to design test cases for overly complex methods.


     */
    @Test(timeout = 1000)
    public void testMessage() {
        User sender = new User("sender", "password", "first", "last", "false");
        User reciever = new User("reciever", "password", "first", "last", "false");
        Message m = new Message("0|sender|reciever|content|false");
        assertEquals("sender", m.getSender());
        assertEquals("reciever", m.getReciever());
        assertEquals("content", m.getContent());
        assertEquals(0, m.getMessageID());
        assertEquals(false, m.hasPicture());
        m.editMessage("new content");
        assertEquals("new content", m.getContent());
        m.addPicture(new byte[0]);
        assertEquals(true, m.hasPicture());
        m.editPicture(new byte[0]);
        assertEquals(true, m.hasPicture());
        assertEquals(0, m.getMessageID());
        assertEquals("0|sender|reciever|new content|true|0.jpg", m.toString());
        m.setMessageID(85);
        assertEquals(85, m.getMessageID());
        //check equals() method
        Message a = new Message("0|sender|reciever|content|false");
        Message b = new Message("0|sender|reciever|content|false");
        Message c = new Message("1|sender|reciver|content|false");
        assertEquals(true, a.equals(b));
        assertEquals(false, a.equals(c));
    }

    public void testID() {
        User sender = new User("sender", "password", "first", "last", "false");
        User reciever = new User("reciever", "password", "first", "last", "false");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("MessageIDCounter.txt"))) {
            writer.write("0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Message m = new Message(sender, reciever, "content here");
        Message n = new Message(reciever, sender, "different content");
        assertEquals(0, m.getMessageID());
        assertEquals(1, n.getMessageID());
    }

    public void testString() {
        User sender = new User("sender", "password", "first", "last", "false");
        User reciever = new User("reciever", "password", "first", "last", "false");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("MessageIDCounter.txt"))) {
            writer.write("0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Message m = new Message(sender, reciever, "content");
        String expected = "0|sender|reciever|content|false";
        assertTrue(expected.equals(m.toString()));
    }

    public static void main(String[] args) throws BadDataException {
        MessageTest mt = new MessageTest();
        mt.testMessage();

        mt.testID();
    }
}
