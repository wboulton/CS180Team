import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import java.io.*;
import java.nio.file.*;
import java.util.Objects;
import static org.junit.Assert.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.*;
import java.net.*;
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
    public void testMessageHandling() {
        try {
            UserDatabase udb = new UserDatabase();
            User sender = new User("sender|password|first|last|receiver|sender2|null|true");
            User sender2 = new User("sender2|password|first|last|sender|null|null|false");
            User reciever = new User("reciever|password|first|last|sender2|null|null|false");
            MessageDatabase db = new MessageDatabase(sender);
            MessageDatabase db2 = new MessageDatabase(sender2);

            MediaServer ms = new MediaServer();

            PrintWriter pw = null;
            String line = "message|" + "SEND_MESSAGE|" + "sender" + "|" + "reciever" + "|" + "content";
            ms.messageHandling(pw, line, db, udb.getUser("sender"));
            BufferedReader send = new BufferedReader(new FileReader("reciever.txt"));
            String proof = send.readLine();
            boolean checker = false;
            if (proof.equals("content")) checker = true;
            assertTrue(checker);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        MediaServerTester mst = new MediaServerTester();
        mst.testMessageHandling();
    }
}
