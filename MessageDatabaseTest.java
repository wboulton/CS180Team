import org.junit.Assert;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

/**
 * Team Project -- MessageDatabaseTest
 *
 * This file tests methods found in MessageDatabase.java 
 *
 * @author William Boulton, Jai Menon, Alan Yi, Mukund Venkatesh
 *
 * @version November 2, 2024
 *
 */

public class MessageDatabaseTest {

   @Test(timeout = 1000)
      public void testMessageDatabase() throws BadDataException {
          UserDatabase db = new UserDatabase();
          User you = db.createUser("you", "Password12", "first", "last", "false");
          User me = db.createUser("me", "Password12", "first", "last", "false");
          MessageDatabase y = new MessageDatabase(you);
          MessageDatabase m = new MessageDatabase(me);
          User u = new User("sender", "Password12", "first", "last", "false");
          MessageDatabase md = new MessageDatabase(u);
          assertEquals(0, md.getRecievedMessages().size());
          assertEquals(0, md.getSentMessages().size());
          Message tester = new Message("0|you|me|content|false");
          y.sendMessage(tester);
          assertEquals(1, y.getSentMessages().size());
          m.recoverMessages();
          assertEquals(1, m.getRecievedMessages().size());
          m.deleteMessage(tester); // delete message is tested once here
          assertEquals(0, m.getRecievedMessages().size());
          assertEquals(0, md.getRecievedMessages().size());
          md.recoverMessages();
          assertEquals(0, md.getRecievedMessages().size());
          assertEquals(true, md.getSentMessages().contains(tester));
          try {
              y.sendMessage(new Message("0|you|stranger|content|false"));
          } catch (Exception e) {
              Assert.assertEquals("Make sure BadDataException is thrown by sendMessage()",BadDataException.class, e.getClass());
          }
          
      }
// if this test case is not working it may be because the files for the users is not created yet
    // try creating that file if you are getting io exceptions when sending messages.
    public void messageSendingRules() {
       User sender = new User("sender|password|first|last|reciever|sender2|null|true");
       User sender2 = new User("sender2|password|first|last|sender|null|null|false");
       User reciever = new User("reciever|password|first|last|sender2|null|null|false");
       MessageDatabase db = new MessageDatabase(sender);
       MessageDatabase db2 = new MessageDatabase(sender2);
       try {
           Message notFriends = new Message("0|sender|reciever|content|false");
           db.sendMessage(notFriends);
           fail("this message should not be sent");
       } catch (Exception e) {
           Assert.assertEquals("Make sure BadDataException is thrown by sendMessage()",BadDataException.class, e.getClass());
       }
        try {
            Message blocked = new Message("0|sender2|sender|content|false");
            db2.sendMessage(blocked);
            fail("this message should not be sent");
        } catch (Exception e) {
            Assert.assertEquals("Make sure BadDataException is thrown by sendMessage()",BadDataException.class, e.getClass());
        }
        try {
            Message works = new Message("0|sender2|reciever|content|false");
            db2.sendMessage(works);
            assertTrue("this message should be sent", true);
        } catch (Exception e) {
            fail("this message should work, but threw an exception: " + e.getMessage());
        }
    }

    public void messageDeletionTest() throws IOException {
        User sender = new User("sender|password|first|last|reciever|null|null|true");
        User reciever = new User("reciever|password|first|last|sender|null|null|true");
        Message sentMessage = new Message("0|sender|reciever|content|false");
        Message otherSentMessage = new Message("1|sender|reciever|content|false");
        try (BufferedWriter send = new BufferedWriter(new FileWriter("sender.txt"));
             BufferedWriter recieve = new BufferedWriter(new FileWriter("reciever.txt"))) {
            send.write(sentMessage.toString());
            send.newLine();
            send.write(otherSentMessage.toString());
            recieve.write(sentMessage.toString());
            recieve.newLine();
            recieve.write(otherSentMessage.toString());
        }
        MessageDatabase sd = new MessageDatabase(sender);
        sd.recoverMessages();
        MessageDatabase rd = new MessageDatabase(reciever);
        rd.recoverMessages();
        Message fakeMessage = new Message("2|sender|reciever|content|false");
        try {
            sd.deleteMessage(fakeMessage);
            fail("this message should not have been deleted because it does not exist");
        } catch (Exception e) {
            Assert.assertEquals("Make sure BadDataException is thrown by sendMessage()",BadDataException.class, e.getClass());
        }
        try {
            sd.deleteMessage(sentMessage);
        } catch (Exception e) {
            e.printStackTrace();
            fail("this message should be able to be deleted");
        }
        try (BufferedReader send = new BufferedReader(new FileReader("sender.txt"));
             BufferedReader recieve = new BufferedReader(new FileReader("reciever.txt"))) {
            String sent = send.readLine();
            String recieved = recieve.readLine();
            assertEquals("make sure messages are acutally being deleted from files", sent, otherSentMessage.toString());
            assertEquals("make sure messages are acutally being deleted from files", recieved, otherSentMessage.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws BadDataException, IOException {
        MessageDatabaseTest mdt = new MessageDatabaseTest();
        mdt.testMessageDatabase();

        mdt.messageSendingRules();
        mdt.messageDeletionTest();
    }
    
}
