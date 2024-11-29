import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    public void testID() throws BadDataException {
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

    public void testString() throws BadDataException {
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
