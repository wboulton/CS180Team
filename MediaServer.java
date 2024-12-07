import java.util.*;
import java.util.concurrent.atomic.*;
import java.io.*;
import java.net.*;
import java.awt.image.BufferedImage;
import crypto.*;

/**
 * Team Project -- MediaServer
 *
 * This is server for handling multiple clients with threading
 *
 * @author William Boulton, Alan Yi, Mukund Venkatesh
 *
 * @version November 15, 2024
 * 
 */

public class MediaServer extends Thread implements ServerInterface {
    private static UserDatabase database;
    private static RSAKey privateKey;
    public static final Object LOCK = new Object();

    private static void RSASend(String send, PublicKey publicKey, PrintWriter writer) {
        String encryptedMessage = publicKey.encryptText(send);
        writer.println(encryptedMessage);
        writer.flush();
    }

    private static String RSARead(RSAKey privateKey, BufferedReader reader) {
        try {
            String read = reader.readLine();
            String decryptedRead = privateKey.decryptCiphertext(read);
            return decryptedRead;
        } catch (IOException e) {
            e.printStackTrace();
            return "failed to read";
        }
    }

    // run method used to control each thread that is made. There is some weird
    // stuff going on with
    // atomic references and atomic integers to make sure these threads work
    // properly with final values
    // and lambda functions. It seems atomic types are effectively final so we can
    // use them in more threaded
    // formats.
    private static void run(Socket client, ServerSocket server) {
        Timer timer = new Timer();
        User user = null;
        AtomicInteger recentID = new AtomicInteger(0);
        MessageDatabase messageDatabase;
        AtomicReference<User> currentlyViewing = new AtomicReference<>(null);
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            final PrintWriter writer = new PrintWriter(client.getOutputStream());
            final ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
            final ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
            String keyString = reader.readLine();
            PublicKey publicKey = new PublicKey(keyString);
            writer.println(privateKey.getPublicKey());
            writer.flush();

            String line = reader.readLine();
            if (line.equals("login")) {
                while (true) {
                    // login
                    String username = RSARead(privateKey, reader);
                    String password = RSARead(privateKey, reader);
                    boolean allowed = database.verifyLogin(username, password);
                    if (allowed) {
                        user = UserDatabase.getUser(username);
                        messageDatabase = new MessageDatabase(user);
                        messageDatabase.recoverMessages();
                        writer.println("Logged in!");
                        writer.flush();
                        oos.writeObject(user);
                        break;
                    } else {
                        writer.println("could not log in");
                        writer.flush();
                        return;
                    }
                }
            } else if (line.equals("new user")) {
                String username = RSARead(privateKey, reader);
                String password = RSARead(privateKey, reader);
                String firstname = RSARead(privateKey, reader);
                String lastname = RSARead(privateKey, reader);
                byte[] pfp = (byte[]) ois.readObject();
                try {
                    user = database.createUser(username, password, firstname, lastname, pfp);
                    System.out.println("user created");
                    writer.println("user created");
                    writer.flush();
                } catch (Exception e) {
                    writer.println(e.getMessage());
                    writer.flush();
                    return;
                }
                oos.writeObject(user);
                messageDatabase = new MessageDatabase(user);
            } else {
                return;
            }

            // this will order the list of users to display friends first
            ArrayList<User> users = database.getUsers();
            ArrayList<String> friends = user.getFriends();
            ArrayList<User> toRemove = new ArrayList<>();
            for (User item : users) {
                String username = item.getUsername();
                if (friends.contains(username)) {
                    writer.write(username);
                    writer.println();
                    writer.flush();
                    toRemove.add(item);
                }
            }
            toRemove.add(user);
            for (User item : users) {
                if (toRemove.contains(item)) {
                    continue;
                }
                // the user list will not display blocked users
                if (user.getBlockedUsers().contains(item.getUsername())) {
                    continue;
                }
                writer.write(item.getUsername());
                writer.println();
                writer.flush();
            }
            writer.println("|ENDED HERE 857725|");
            writer.flush();

            System.out.println(client.toString() + " connected.");
            // update DMs periodically
            final int updateDelay = 3000; // 3 seconds
            // this uses the TimerTask object included in the java.util package. Essentially
            // what this does is
            // it creates a timer on another thread that waits a set amount of time then
            // runs the function after each interval
            // with how we have it set up this timertask will run recover messages then
            // write new messages to the user every
            // three seconds. This allows "real time messaging".
            /* TimerTask task = new TimerTask() {
                public void run() {
                    // update messages
                    messageDatabase.recoverMessages();
                    ArrayList<Message> recievedMessages = messageDatabase.getRecievedMessages();
                    for (Message message : recievedMessages) {
                        if (message.getMessageID() > recentID.get()) {
                            if (currentlyViewing.get() != null &&
                                message.getSender().equals(currentlyViewing.get().getUsername())) {
                                RSASend("INCOMING|" + message.toString(), publicKey, writer);
                                recentID.set(message.getMessageID());
                            }
                        }
                    }
                }
            }; 
            timer.scheduleAtFixedRate(task, updateDelay, updateDelay);
*/
            // this stuff is just in testing state rn
            while (true) {
                line = RSARead(privateKey, reader);
                // random numbers for kill message, this should not be vulnerable because all
                // other lines
                // should have some other function name/code in front when sent by the client
                if (line.equals("77288937499272")) {
                    System.out.println(client.toString() + " disconnected.");
                    break;
                }
                // System.out.printf("Recieved '%s' from %s\n", line, client.toString());
                // System.out.println(line.split("\\|")[0]);
                if (line.split("\\|")[0].equals("user")) {
                    userHandling(ois, oos, writer, line.substring(line.indexOf("|") + 1));
                } else if (line.split("\\|")[0].equals("message")) {
                    currentlyViewing.set(messageHandling(publicKey, writer, line.substring(line.indexOf("|") + 1),
                            messageDatabase, currentlyViewing.get(), ois));
                }
            }
            // System.out.printf("Client %s disconnected\n", client);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // handle all message related functions sent by the client
    public static User messageHandling(PublicKey publicKey, PrintWriter writer, String line, MessageDatabase messageDatabase, User viewing,
            ObjectInputStream ois) {
        // GET_SENT_MESSAGES, GET_RECIEVED_MESSAGES, RECOVER_MESSAGES, SEND_MESSAGE,
        // DELETE_MESSAGE, EDIT_MESSAGE
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
                    ArrayList<Message> sortedMessages = new ArrayList<>(messages);
                    sortedMessages.sort(Comparator.comparingInt(Message::getMessageID));
                    for (Message item : sortedMessages) {
                        if (viewing.getUsername().equals(item.getSender()) ||
                            viewing.getUsername().equals(item.getReciever())) {
                            String itemInString = item.toString();
                            if (item.hasPicture()) {
                                itemInString += "|" + item.getPicture();
                            }
                            RSASend(itemInString, publicKey, writer);
                        }
                    }
                    RSASend("|ENDED HERE 857725|", publicKey, writer);
                    return viewing;
                // public Message(User sender, User reciever, String content)
                case SEND_MESSAGE:
                    String[] messageInfo = information.split("\\|");
                    User sender = UserDatabase.getUser(messageInfo[0]);
                    User reciever = UserDatabase.getUser(messageInfo[1]);
                    Message message = new Message(sender, reciever, messageInfo[2]);
                    if (messageInfo.length > 3) {
                        String[] pictureStrings = messageInfo[3].split(",");
                        byte[] picture = new byte[pictureStrings.length];
                        for (int i = 0; i < pictureStrings.length; i++) {
                            picture[i] = Byte.parseByte(pictureStrings[i]);
                        }
                        message.addPicture(picture);
                    }
                    messageDatabase.sendMessage(message);
                    RSASend(message.toString(), publicKey, writer);
                    return viewing;
                case DELETE_MESSAGE:
                    ArrayList<Message> sent = messageDatabase.getSentMessages();
                    int id = Integer.parseInt(information.split("\\|")[1]);
                    for (Message item : sent) {
                        if (item.getMessageID() == id) {
                            messageDatabase.deleteMessage(item);
                        }
                    }
                    return viewing;
                case EDIT_MESSAGE:
                    ArrayList<Message> sentMessages = messageDatabase.getSentMessages();
                    for (Message item : sentMessages) {
                        if (item.getMessageID() == Integer.parseInt(information.split("\\|")[0])) {
                            Message editedMessage = new Message(UserDatabase.getUser(item.getSender()),
                                    UserDatabase.getUser(item.getReciever()), information.split("\\|")[1]);
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

    // handle all user related functions sent by the client.
    public static void userHandling(ObjectInputStream ois, ObjectOutputStream oos, PrintWriter writer, String line) {
        try {
            String[] inputs = line.split("\\|");
            Action action = Action.valueOf(inputs[0]);

            switch (action) {
                case SEARCH:
                    User userFound = UserDatabase.getUser(inputs[1]);
                    if (userFound == null) {
                        writer.write("USER|" + null);
                    } else {
                        writer.write("USER|" + userFound.getUsername());
                    }
                    writer.println();
                    writer.flush();
                    break;
                case ADD_FRIEND:
                    User user = UserDatabase.getUser(inputs[1]);
                    User otherUser = UserDatabase.getUser(inputs[2]);
                    boolean value = UserDatabase.addFriend(user, otherUser);
                    writer.write(String.valueOf(value));
                    writer.println();
                    writer.flush();
                    break;
                case REMOVE_FRIEND:
                    User user2 = UserDatabase.getUser(inputs[1]);
                    User otherUser2 = UserDatabase.getUser(inputs[2]);
                    boolean response = UserDatabase.removeFriend(user2, otherUser2);
                    writer.write(String.valueOf(response));
                    writer.println();
                    writer.flush();
                    break;
                case GET_FRIEND:
                    User thisUser = UserDatabase.getUser(inputs[1]);
                    User friendUser = UserDatabase.getUser(inputs[2]);
                    writer.write(String.valueOf(thisUser.getFriends().contains(friendUser.getUsername())));
                    writer.println();
                    writer.flush();
                    break;
                case CHANGE_USERNAME:
                    User userToModify = UserDatabase.getUser(inputs[1]);
                    boolean v = database.changeUsername(userToModify, inputs[2]);
                    writer.write(String.valueOf(v));
                    writer.println();
                    writer.flush();
                    break;
                case BLOCK:
                    User user3 = UserDatabase.getUser(inputs[1]);
                    User otherUser3 = UserDatabase.getUser(inputs[2]);
                    UserDatabase.blockUser(user3, otherUser3);
                    break;
                case UNBLOCK:
                    User user4 = UserDatabase.getUser(inputs[1]);
                    User otherUser4 = UserDatabase.getUser(inputs[2]);
                    boolean val = UserDatabase.unblockUser(user4, otherUser4);
                    writer.write(String.valueOf(val));
                    writer.println();
                    writer.flush();
                    break;
                case CHANGE_PASSWORD:
                    User user5 = UserDatabase.getUser(inputs[1]);
                    boolean success = database.changePassword(user5, inputs[2]);
                    writer.write(String.valueOf(success));
                    writer.println();
                    writer.flush();
                    break;
                case CHANGE_PICTURE:
                    User user6 = UserDatabase.getUser(inputs[1]);
                    // get the byte array from the input stream
                    byte[] picture = (byte[]) ois.readObject();
                    UserDatabase.changePicture(user6, picture);
                    break;
                case ALLOW_ALL:
                    User user7 = UserDatabase.getUser(inputs[1]);
                    boolean allowAll = user7.isAllowAll();
                    user7.setAllowAll(!allowAll);
                    writer.write(String.valueOf(!allowAll));
                    writer.println();
                    writer.flush();
                    break;
                case GET_PROFILEPICTURE:
                    User user8 = UserDatabase.getUser(inputs[1]);
                    byte[] image = user8.getProfilePicture();
                    oos.writeObject(image);
                    break;
                default:
                    break;
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            System.out.println("OUT OF BOUNDS, CHECK PARAMETERS");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Server started");
        database = new UserDatabase();
        privateKey = new RSAKey();

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
            // this shutdown hook will handle when the Server is force closed
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    UserDatabase.updateDB();
                    for (Socket s : socket) {
                        s.close();
                    }
                    server.close();
                    System.out.println("Server shut down");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
            // this will run forever
            while (true) {
                final Socket newSocket = server.accept();
                if (!socket.contains(newSocket)) {
                    socket.add(newSocket);
                    // so this uses lambda functions to create a new thread with the run method
                    // dynamically set to the method above named
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
