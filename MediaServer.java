import java.util.*;
import java.io.*;
import java.net.*;

public class MediaServer extends Thread {
    private static UserDatabase database;
    private static MessageDatabase messageDatabase;
    public static final Object lock = new Object();
    private static void run(Socket client, ServerSocket server) {
        BufferedReader reader = null;
        PrintWriter writer = null;
        Timer timer = new Timer();
        User user = null;
        int recentID = 0;
        try {
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            writer = new PrintWriter(client.getOutputStream()); 
            System.out.println("connected");
            String line = reader.readLine();
            switch (line) {
                case "login":
                    while (true) {
                        //login
                        String username = reader.readLine();
                        String password = reader.readLine();
                        boolean allowed = database.verifyLogin(username, password);
                        if (allowed) {
                            user = UserDatabase.getUser(username);
                            break;
                        } else {
                            writer.println("could not log in");
                            writer.flush();
                            continue;
                        }
                    }
                    break;
                case "new user":
                    String username = reader.readLine();
                    String password = reader.readLine();
                    String firstname = reader.readLine();
                    String lastname = reader.readLine();
                    String pfp = reader.readLine();
                    user = database.createUser(username, password, firstname, lastname, pfp);
                    break;
                default:
                    throw new BadDataException(line + " Was not a valid action");
            }

            messageDatabase = new MessageDatabase(user);
            TimerTask task = new TimerTask() {
                public void run() {
                    //update messages
                    ArrayList<Message> recievedMessages = messageDatabase.getRecievedMessages();
                    for (Message message: recievedMessages){
                        if (message.getMessageID() > recentID){
                            writer.write(message.toString());
                            writer.println();
                            writer.flush();   
                            recentID = message.getMessageID();
                        }
                    }
                }
            };
            // this stuff is just in testing state rn
            while (true) {
                line = reader.readLine();
                timer.schedule(null, MAX_PRIORITY);
                if (line.equals("77288937499272")) {
                    break;
                }
                System.out.printf("Recieved '%s' from %s\n", line, client.toString());
            }
            System.out.printf("Client %s disconnected\n", client);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        database = new UserDatabase();
        int port;
        try {
        port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.out.println("Enter a valid 4 digit port number");
            return;
        }
        try {
            ServerSocket server = new ServerSocket(port);
            final ArrayList<Socket> socket = new ArrayList<>();
            while (true) {
                final Socket newSocket = server.accept();
                if (!socket.contains(newSocket)) {
                    socket.add(newSocket);
                    Thread clientThread = new Thread(() -> run(newSocket, server));
                    clientThread.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
