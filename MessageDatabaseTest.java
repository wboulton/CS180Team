import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.*;

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
          User you = db.createUser("you", "password", "first", "last", "false");
          User me = db.createUser("me", "password", "first", "last", "false");
          MessageDatabase y = new MessageDatabase(you);
          MessageDatabase m = new MessageDatabase(me);
          User u = new User("sender", "password", "first", "last", "false");
          MessageDatabase md = new MessageDatabase(u);
          assertEquals(0, md.getRecievedMessages().size());
          assertEquals(0, md.getSentMessages().size());
          Message tester = new Message("0|you|me|content|false");
          y.sendMessage(tester);
          assertEquals(1, y.getSentMessages().size());
          assertEquals(1, m.getRecievedMessages().size());
          m.deleteMessage(tester);
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

    public static void main(String[] args) throws BadDataException {
        MessageDatabaseTest mdt = new MessageDatabaseTest();
        mdt.testMessageDatabase();
    }
    
}
