import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.*;
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
//run method used to control each thread that is made. There is some weird stuff going on with 
//atomic references and atomic integers to make sure these threads work properly with final values
//and lambda functions. It seems atomic types are effectively final so we can use them in more threaded
//formats. 
    private static void run(Socket client, ServerSocket server) {
        Timer timer = new Timer();
        User user = null;
        AtomicInteger recentID = new AtomicInteger(0);
        MessageDatabase messageDatabase;
        AtomicReference<User> currentlyViewing = new AtomicReference<>(null); 
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
                    System.out.println(username);
                    System.out.println(password);
                    boolean allowed = database.verifyLogin(username, password);
                    if (allowed) {
                        user = UserDatabase.getUser(username);
                        messageDatabase = new MessageDatabase(user);
                        messageDatabase.recoverMessages();
                        writer.println("Logged in!");
                        writer.flush();
                        break;
                    } else {
                        writer.println("could not log in");
                        writer.flush();
                        return;
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
//this uses the TimerTask object included in the java.util package. Essentially what this does is 
//it creates a timer on another thread that waits a set amount of time then runs the function after each interval
//with how we have it set up this timertask will run recover messages then write new messages to the user every
//three seconds. This allows "real time messaging". 
            TimerTask task = new TimerTask() {
                public void run() {
                    //update messages
                    messageDatabase.recoverMessages();
                    ArrayList<Message> recievedMessages = messageDatabase.getRecievedMessages();
                    for (Message message: recievedMessages) {
                        if (message.getMessageID() > recentID.get()) {
                            if (currentlyViewing.get() != null && 
                                message.getSender().equals(currentlyViewing.get().getUsername())) {
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
                reader.readLine();
                line = reader.readLine();
                System.out.println(line);
                //random numbers for kill message, this should not be vulnerable because all other lines
                //should have some other function name/code in front when sent by the client
                if (line.equals("77288937499272")) {
                    break;
                }
                System.out.printf("Recieved '%s' from %s\n", line, client.toString());
                System.out.println(line.split("\\|")[0]);
                if (line.split("\\|")[0].equals("user")) {
                    userHandling(writer, line.substring(line.indexOf("\\|")));
                } else if (line.split("\\|")[0].equals("message")) {
                    currentlyViewing.set(messageHandling(writer, line.substring(line.indexOf("|") + 1),
                        messageDatabase, currentlyViewing.get()));
                }
            }
            System.out.printf("Client %s disconnected\n", client);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//handle all message related functions sent by the client
    public static User messageHandling(PrintWriter writer, String line, MessageDatabase messageDatabase, User viewing) {
//GET_SENT_MESSAGES, GET_RECIEVED_MESSAGES, RECOVER_MESSAGES, SEND_MESSAGE, DELETE_MESSAGE, EDIT_MESSAGE
        try {
            String temp = line.split("\\|")[0];
            String information = line.replace(temp, "").substring(1);
            Action task = Action.valueOf(temp);
            switch (task) {
                case GET_CONVERSATION:
                    messageDatabase.recoverMessages();
                    ArrayList<Message> messages = messageDatabase.getSentMessages();
                    for (Message message : messageDatabase.getRecievedMessages()) {
                        messages.add(message);
                    }
                    messages.sort(Comparator.comparingInt(Message::getMessageID));
                    for (Message item : messages) {
                        if (viewing.getUsername().equals(item.getSender()) || 
                            viewing.getUsername().equals(item.getReciever())) {
                            writer.write(item.toString());
                            writer.println();
                            writer.flush();
                        }
                    }
                    return viewing;
//public Message(User sender, User reciever, String content)
                case SEND_MESSAGE:
                    String[] messageInfo = information.split("\\|");
                    User sender = UserDatabase.getUser(messageInfo[0]);
                    User reciever = UserDatabase.getUser(messageInfo[1]);
                    Message message = new Message(sender, reciever, messageInfo[2]);
                    messageDatabase.sendMessage(message);
                    if (messageInfo.length > 3) {
                        String[] pictureStrings = messageInfo[4].split(",");
                        byte[] picture = new byte[pictureStrings.length];
                        for (int i = 0; i < pictureStrings.length; i++) {
                            picture[i] = Byte.parseByte(pictureStrings[i]);
                        }
                        message.addPicture(picture);
                    }
                    return viewing;
                case DELETE_MESSAGE:
                    ArrayList<Message> sent = messageDatabase.getSentMessages();
                    int id = Integer.parseInt(information.split("\\|")[0]);
                    for (Message item : sent) {
                        if (item.getMessageID() == id) {
                            messageDatabase.deleteMessage(item);
                        }
                    }
                    return viewing;
                case EDIT_MESSAGE:
                    ArrayList<Message> sentMessages = messageDatabase.getSentMessages();
                    Message editedMessage = new Message(information);
                    for (Message item : sentMessages) {
                        if (item.getMessageID() == editedMessage.getMessageID()) {
                            messageDatabase.editMessage(item, editedMessage);
                        }
                    }
                    return viewing;
                case SET_VIEWING:
                    viewing = UserDatabase.getUser(information);
                    return viewing;
                default:
                    return viewing;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return viewing;
        }
    }

//handle all user related functions sent by the client.
    public static void userHandling(PrintWriter writer, String line) {
        try {
            String[] inputs = line.split("\\|");
            Action action = Action.valueOf(inputs[0]);

            switch (action) {
                case SEARCH: 
                    User userFound = UserDatabase.getUser(inputs[1]);
                    writer.write("USER\\|" + userFound.toString());
                    writer.println();
                    writer.flush();   
                    break;
                case ADD_FRIEND:
                    User user = UserDatabase.getUser(inputs[1]);
                    User otherUser = UserDatabase.getUser(inputs[2]);
                    UserDatabase.addFriend(user, otherUser);
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
                case CHANGE_PASSWORD:
                    User userToModify2 = UserDatabase.getUser(inputs[1]);
                    database.changePassword(userToModify2, inputs[2]);
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
