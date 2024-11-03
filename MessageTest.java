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
 * @author Mukund Venkatesh, Jai Menon
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
        Message a = new Message("0|sender|reciever|content|false");
        Message b = new Message("0|sender|reciever|content|false");
        Message c = new Message("1|sender|stranger|something else|false");
        assertEquals(true, a.equals(b));
        assertEquals(false, a.equals(c));
    }
    @Test(timeout = 1000)
    public void testMessageDatabase() throws BadDataException {
        UserDatabase db = new UserDatabase();
        User you = db.createUser("you", "password", "first", "last", "false");
        User me = db.createUser("me", "password", "first", "last", "false");
        MessageDatabase y = new MessageDatabase(you);
        MessageDatabase m = new MessageDatabase(me);
        User u = new User("sender", "password", "first", "last", "false");
        MessageDatabase md = new MessageDatabase(u);
        assertEquals(0, md.getRecievedMessages().size());
        assertEquals(0, md.getSentMessages().size());
       // I don't know if this would work since sendMessage() checks if the sender and receiver exist in the UserDatabase, and u wasn't added to any database
        /*
        md.sendMessage(new Message("0|sender|reciever|content|false"));
        assertEquals(1, md.getSentMessages().size());
        */
        Message tester = new Message("0|you|me|content|false");
        y.sendMessage(tester);
        assertEquals(1, y.getSentMessages().size());
        assertEquals(1, m.getRecievedMessages().size());
        m.deleteMessage(tester);
        assertEquals(0, m.getRecievedMessages().size());
        assertEquals(0, md.getRecievedMessages().size());
        md.recoverMessages();
       // assertEquals(1, md.getSentMessages().size());
        assertEquals(0, md.getRecievedMessages().size());
        assertEquals(true, md.getSentMessages().contains(tester));
        try {
            y.sendMessage(new Message("0|you|stranger|content|false"));
        } catch (Exception e) {
            Assert.assertEquals("Make sure BadDataException is thrown by sendMessage()",BadDataException.class, e.getClass());
        }
        
    }

    public static void main(String[] args) throws BadDataException {
        MessageTest mt = new MessageTest();
        mt.testMessage();
        mt.testMessageDatabase();
    }
}
