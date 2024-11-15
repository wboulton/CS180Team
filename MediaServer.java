import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.*;
import java.net.*;
/**
 * Team Project -- MediaServer
 *
 * This is server for handling multiple clients with threading
 *
 * @author William Boulton, Alan Yi
 *
 * @version November 15, 2024
 * 
 */

public class MediaServer extends Thread implements ServerInterface {
    private static UserDatabase database;
    public static final Object lock = new Object();

    private static void run(Socket client, ServerSocket server) {
        Timer timer = new Timer();
        User user = null;
        AtomicInteger recentID = new AtomicInteger(0);
        MessageDatabase messageDatabase;
        User currentlyViewing = null; 
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            final PrintWriter writer  = new PrintWriter(client.getOutputStream()); 
            System.out.println("connected");
            String line = reader.readLine();
            if (line.equals("login")) {
                while (true) {
                    //login
                    String username = reader.readLine();
                    String password = reader.readLine();
                    boolean allowed = database.verifyLogin(username, password);
                    if (allowed) {
                        user = UserDatabase.getUser(username);
                        messageDatabase = new MessageDatabase(user);
                        messageDatabase.recoverMessages();
                        break;
                    } else {
                        writer.println("could not log in");
                        writer.flush();
                        continue;
                    }
                }
            } else if (line.equals("new user")) {
                String username = reader.readLine();
                String password = reader.readLine();
                String firstname = reader.readLine();
                String lastname = reader.readLine();
                String pfp = reader.readLine();
                user = database.createUser(username, password, firstname, lastname, pfp);
                messageDatabase = new MessageDatabase(user);
            } else {
                return;
            }
            
            //update DMs periodically
            final int updateDelay = 3000; // 3 seconds
            TimerTask task = new TimerTask() {
                public void run() {
                    //update messages
                    messageDatabase.recoverMessages();
                    ArrayList<Message> recievedMessages = messageDatabase.getRecievedMessages();
                    for (Message message: recievedMessages) {
                        if (message.getMessageID() > recentID.get()) {
                            if (message.getSender().equals(currentlyViewing.getUsername())) {
                                writer.write("INCOMING\\|" + message.toString());
                                writer.println();
                                writer.flush();   
                                recentID.set(message.getMessageID());
                            }
                        }
                    }
                }
            };
            timer.scheduleAtFixedRate(task, updateDelay, updateDelay);

            // this stuff is just in testing state rn
            while (true) {
                line = reader.readLine();
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

    public void messageHandling(PrintWriter writer, String line, MessageDatabase database, User viewing) {
//GET_SENT_MESSAGES, GET_RECIEVED_MESSAGES, RECOVER_MESSAGES, SEND_MESSAGE, DELETE_MESSAGE, EDIT_MESSAGE
        try {
            String temp = line.split("\\|")[0];
            String information = line.replace(temp, "").substring(1);
            Action task = Action.valueOf(temp);
            switch (task) {
                case GET_CONVERSATION:
                    database.recoverMessages();
                    ArrayList<Message> messages = database.getSentMessages();
                    for (Message message : database.getRecievedMessages()) {
                        messages.add(message);
                    }
                    messages.sort(Comparator.comparingInt(Message::getMessageID));
                    for (Message item : messages) {
                        writer.write(item.toString());
                        writer.println();
                        writer.flush();
                    }
                    break;
//public Message(User sender, User reciever, String content)
                case SEND_MESSAGE:
                    String[] messageInfo = information.split("\\|");
                    User sender = UserDatabase.getUser(messageInfo[0]);
                    User reciever = UserDatabase.getUser(messageInfo[1]);
                    Message message = new Message(sender, reciever, messageInfo[2]);
                    database.sendMessage(message);
                    if (messageInfo.length > 3) {
                        String[] pictureStrings = messageInfo[4].split(",");
                        byte[] picture = new byte[pictureStrings.length];
                        for (int i = 0; i < pictureStrings.length; i++) {
                            picture[i] = Byte.parseByte(pictureStrings[i]);
                        }
                        message.addPicture(picture);
                    }
                    break;
                case DELETE_MESSAGE:
                    ArrayList<Message> sent = database.getSentMessages();
                    int id = Integer.parseInt(information.split("\\|")[0]);
                    for (Message item : sent) {
                        if (item.getMessageID() == id) {
                            database.deleteMessage(item);
                        }
                    }
                    break;
                case EDIT_MESSAGE:
                    ArrayList<Message> sentMessages = database.getSentMessages();
                    Message editedMessage = new Message(information);
                    for (Message item : sentMessages) {
                        if (item.getMessageID() == editedMessage.getMessageID()) {
                            database.editMessage(item, editedMessage);
                        }
                    }
                default:
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void userHandling(PrintWriter writer, String line) {
        try{
            String[] inputs = line.split("\\|");
            Action action = Action.valueOf(inputs[0]);

            switch (action){
                case SEARCH: 
                    User userFound = UserDatabase.getUser(inputs[1]);
                    writer.write("USER\\|"+userFound.toString());
                    writer.println();
                    writer.flush();   
                    break;
                case ADD_FRIEND:
                    User user = UserDatabase.getUser(inputs[1]);
                    User otherUser = UserDatabase.getUser(inputs[2]);
                    UserDatabase.addFriend(user,otherUser);
                    break;
                case REMOVE_FRIEND:
                    User user2 = UserDatabase.getUser(inputs[1]);
                    User otherUser2 = UserDatabase.getUser(inputs[2]);
                    UserDatabase.removeFriend(user2, otherUser2);
                    break;
                case CHANGE_USERNAME:
                    User userToModify = UserDatabase.getUser(inputs[1]);
                    database.changeUsername(userToModify, inputs[2]);
                    break;
                case BLOCK:
                    User user3 = UserDatabase.getUser(inputs[1]);
                    User otherUser3 = UserDatabase.getUser(inputs[2]);
                    UserDatabase.blockUser(user3, otherUser3);
                    break;
                case UNBLOCK:
                    User user4 = UserDatabase.getUser(inputs[1]);
                    User otherUser4 = UserDatabase.getUser(inputs[2]);
                    UserDatabase.unblockUser(user4, otherUser4);
                    break;
                default:
                    break;
            }            

        }catch (IndexOutOfBoundsException e) {
            System.out.println("OUT OF BOUNDS, CHECK PARAMETERS");
        }catch(Exception e){
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
// so this uses lambda functions to create a new thread with the run method dynamically set to the method above named
// run. This way we can create threads without needing to create another class. 
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
