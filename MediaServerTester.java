import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import java.io.*;
import java.nio.file.*;
import java.util.Objects;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.*;
import java.net.*;
import java.util.*;
/**
 * Team Project -- MediaServerTester
 *
 * This file tests the MessageServer class.
 * For more in depth documentation see Docs
 *
 * @author Jai Menon
 *
 * @version November 10, 2024
 *
 */
public class MediaServerTester {
    //Checks if "connected" is printed to ensure that server an client are connecting
    @Test
    public void testSendMessageHandling() {
        try {
            UserDatabase udb = new UserDatabase();
            User sender = UserDatabase.getUser("sender");
            User sender2 = UserDatabase.getUser("sender2");
            User reciever = UserDatabase.getUser("reciever");
            MessageDatabase db = new MessageDatabase(sender2);
            PrintWriter pw = null;
            String line = "SEND_MESSAGE|" + "sender2" + "|" + "reciever" + "|" + "content";
            MediaServer.messageHandling(pw, line, db, udb.getUser("sender2"));
            BufferedReader send = new BufferedReader(new FileReader("reciever.txt"));
            String proof = send.readLine();
            String[] parts = proof.split("\\|");
            System.out.println(parts);
            boolean checker = false;
            if (parts[3].equals("content")) checker = true;
            assertTrue(checker);
            send.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //wait for the message to be sent and then check if the message is in the reciever's file
    //this runs fine on most machines, make sure your files are in the right spot
    @Test
    public void testEditMessageHandling() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        try {
            UserDatabase udb = new UserDatabase();
            User sender = UserDatabase.getUser("sender");
            User sender2 = UserDatabase.getUser("sender2");
            User reciever = UserDatabase.getUser("reciever");
            MessageDatabase db = new MessageDatabase(sender);
            System.out.println(db.getSentMessages());
            PrintWriter pw = null;
            String line = "EDIT_MESSAGE|" + "21" + "|content2";
            MediaServer.messageHandling(pw, line, db, udb.getUser("sender2"));
            BufferedReader send = new BufferedReader(new FileReader("reciever.txt"));
            String proof = send.readLine();
            System.out.println(proof);
            String[] parts = proof.split("\\|");
            System.out.println(parts);
            boolean checker = false;
            if (parts[3].equals("content2")) checker = true;
            assertTrue(checker);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBlockUserServer() {
        UserDatabase userDatabase = new UserDatabase();
        MediaServer.userHandling(null, "BLOCK|username|name");
        User username = userDatabase.getUser("username");
        assertTrue(username.getBlockedUsers().contains("name"));
    }
}
