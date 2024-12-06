import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.security.PrivateKey;
import java.util.*;
import crypto.*;

/**
 * Team Project -- UserClient
 *
 * This file is the client side of the user interface.
 *
 * @author Mukund Venkatesh, William Boulton, Kush Kodiya
 *
 * @version November 17, 2024
 */
public class UserClient implements UserClientInt {
    private User user;
    private ArrayList<String> userList;
    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    private ObjectInputStream input;
    private static final int portNumber = 8080;
    private static final int MAX_LENGTH = 5_000;

    //RSA keys
    private PublicKey publicKey;
    private RSAKey privateKey;

    // Constructor for existing user
    public UserClient(String username, String password) throws IOException, BadDataException {
        connectToServer();
        login(username, password);
        //here we collect a list of usernames
        userList = new ArrayList<String>();
        String line = null;
        while (!(line = reader.readLine()).equals("|ENDED HERE 857725|")) {
            userList.add(line);
        }
    }

    // Constructor for new user
    public UserClient(String username, String password, String firstName, String lastName,
        String profilePicture) throws IOException, BadDataException {
        connectToServer();
        createNewUser(username, password, firstName, lastName, profilePicture);
        //here we collect a list of usernames
        userList = new ArrayList<String>();
        String line = null;
        while (!(line = reader.readLine()).equals("|ENDED HERE 857725|")) {
            userList.add(line);
        }
    }

    private void RSASend(String send) {
        String encryptedMessage = publicKey.encryptText(send);
        writer.println(encryptedMessage);
    }

    private String RSARead() {
        try {
            String read = reader.readLine();
            String decryptedRead = privateKey.decryptCiphertext(read);
            return decryptedRead;
        } catch (IOException e) {
            e.printStackTrace();
            return "failed to read";
        }
    }

    public void kill() {
        RSASend("77288937499272");
    }

    private void connectToServer() throws IOException {
        privateKey = new RSAKey();
        socket = new Socket("localhost", portNumber);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true); 
        input = new ObjectInputStream(socket.getInputStream());
        writer.println(privateKey.getPublicKey());
        publicKey = new PublicKey(reader.readLine());
    }

    private void login(String username, String password) throws IOException, BadDataException {
        writer.println("login");  // Send login command to the server
        RSASend(username);
        RSASend(password);

        String response = reader.readLine();
        if (response.equals("could not log in")) {
            throw new BadDataException("Invalid login credentials.");
        }
        System.out.println("Login successful.");
        try {
            this.user = (User) input.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("the object was not a user");
        }
    }

    private void createNewUser(String username, String password, String firstName, String lastName, 
        String profilePicture) throws IOException, BadDataException {
        writer.println("new user");  // Send new user command to the server
        RSASend(username);
        RSASend(password);
        RSASend(firstName);
        RSASend(lastName);
        writer.println(profilePicture);
        String response = reader.readLine();
        if (response.equals("user created")) {
            try {
                this.user = (User) input.readObject();
            } catch (Exception e) {
                System.out.println("the object was not a user");
            }
        } else {
            throw new BadDataException(response);
        }

    }
//messages that are too long are canceled because when they get sent over network bad things happen.
    @Override
    public String sendMessage(String receiver, String content, String picture) throws BadDataException, IOException {
        // Send SEND_MESSAGE command to the server
        if (content.length() > MAX_LENGTH) {
            return "String too long";
        }
        String message = "message|" + "SEND_MESSAGE|" + user.getUsername() + "|" + receiver + "|" + content;
        RSASend(message);

        if (picture != null && !picture.isEmpty() && !picture.equals("false")) {
            File imageFile = new File(picture);
            try {
                byte[] imageData = Files.readAllBytes(imageFile.toPath());
                RSASend("SEND_PICTURE|" + user.getUsername() + "|" + receiver + "|" + 
                    content + "|" + byteArrayToString(imageData));
            } catch (IOException e) {
                throw new BadDataException("Picture not found");
            }
        }
        String response = reader.readLine();
        return response;
    }

    public ArrayList<String> getUserList() {
        return userList;
    }

    public void deleteMessage(int id) throws IOException {
        // Send DELETE_MESSAGE command
        RSASend("message|DELETE_MESSAGE|" + user.getUsername() + "|" + id);
    }

    public String editMessage(int id, String newContent) throws IOException {
        // Send EDIT_MESSAGE command
        if (newContent.length() > MAX_LENGTH) {
            return "String too long";
        }
        RSASend("message|" + "EDIT_MESSAGE|" + id + "|" + newContent);
        return "success";
    }

    public void blockUser(String usernameToBlock) throws IOException {
        // Send BLOCK command
        RSASend("user|BLOCK|" + user.getUsername() + "|" + usernameToBlock);
    }

    public boolean unblockUser(String usernameToUnblock) throws IOException {
        // Send UNBLOCK command
        RSASend("user|UNBLOCK|" + user.getUsername() + "|" + usernameToUnblock);
        return reader.readLine().equals("true");
    }

    public ArrayList<String> getConversation(String username) throws IOException {
        RSASend("message|SET_VIEWING|" + username);
        RSASend("message|GET_CONVERSATION|" + username);
        ArrayList<String> messagesList = new ArrayList<String>();
        messagesList.clear();
        String line;
        while ((line = reader.readLine()) != null) {
            line = privateKey.decryptCiphertext(line);
            if (line.equals("|ENDED HERE 857725|")) {
                break;
            }
            if (line.contains("INCOMING|")) {
                continue;
            }
            messagesList.add(line);
        }
        //This is a really sketchy fix but it works
        if (messagesList.size() < 1) {
            messagesList = null;
        } else if (messagesList.size() == 1) {
            return messagesList;
        } else if (Integer.parseInt(messagesList.get(0).split("\\|")[0]) > 
            Integer.parseInt(messagesList.get(1).split("\\|")[0])) {
            messagesList.remove(0);
        }
        return messagesList;
    }
    public boolean addFriend(String friendUsername) throws IOException {
        // Send ADD_FRIEND command
        RSASend("user|ADD_FRIEND|" + user.getUsername() + "|" + friendUsername);
        String temp = reader.readLine();
        return temp.equals("true");
    }

    public boolean removeFriend(String friendUsername) throws IOException {
        // Send REMOVE_FRIEND command
        RSASend("user|REMOVE_FRIEND|" + user.getUsername() + "|" + friendUsername);
        return reader.readLine().equals("true");
    }

    public void addOrRemoveFriend(String username) throws IOException {
        boolean something = addFriend(username);
        if (!something) {
            removeFriend(username);
        }
    }

    public void blockOrUnblock(String username) throws IOException {
        boolean canUnblock = unblockUser(username);
        if (!canUnblock) {
            blockUser(username);
        }
    }

    // Helper method to convert byte array to string format for sending to the server
    private String byteArrayToString(byte[] byteArray) {
        StringBuilder sb = new StringBuilder();
        for (byte b : byteArray) {
            sb.append(b).append(",");
        }
        return sb.toString();
    }

    public String search(String username) {
        RSASend("user|SEARCH|" + username);
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public boolean setUserName(String name) throws IOException {
        RSASend("user|CHANGE_USERNAME|" + user.getUsername() + "|" + name);
        return reader.readLine().equals("true");
    }

    @Override
    public boolean setPassword(String password) throws IOException {
        RSASend("user|CHANGE_PASSWORD|" + user.getUsername() + "|" + password);
        return reader.readLine().equals("true");
    }

//this main method is set up for testing, the final app will use a GUI to run all of these functions,
//for now, this uses terminal inputs from the client side so we can manually test the operation of the server/client
    public static void main(String[] args) {      
        Scanner sc = new Scanner(System.in);
        System.out.println("new or existing user?");
        String existance = sc.nextLine();
        UserClient client = null;
        if (existance.equalsIgnoreCase("existing")) {
            System.out.println("Username: ");

            String username = sc.nextLine();
            System.out.println("Password: ");
            String password = sc.nextLine();
            try {
                client = new UserClient(username, password);
            } catch (Exception e) {
                e.printStackTrace();
                client.kill();
            }
        } else if (existance.equalsIgnoreCase("new")) {
            System.out.println("Username: ");
            String username = sc.nextLine();
            System.out.println("Password: ");
            String password = sc.nextLine();
            System.out.println("First Name: ");
            String firstName = sc.nextLine();
            System.out.println("Last Name: ");
            String lastName = sc.nextLine();
            String profilePicture = "false";
            try {
                client = new UserClient(username, password, firstName, lastName, profilePicture);
                client.input = new ObjectInputStream(client.socket.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final PrintWriter newWriter = client.writer;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            newWriter.write("77288937499272"); // random quit code
            newWriter.println();
            newWriter.flush();
        }));

        //have a user or message option
        //if user, have a search, add friend, remove friend, block, unblock, change username, change password
        //if message, have a send, delete, edit
        //dont make a new client, use the existing client
        System.out.println("User or message?");
        String choice = sc.nextLine();
        switch (choice.toLowerCase()) {
            case "user" -> {
                System.out.println("Search, add friend, remove friend, block, unblock, "
                    + "change username, change password?");
                String userChoice = sc.nextLine();
                switch (userChoice.toLowerCase()) {
                    case "search" -> {
                        System.out.println("Who do you want to search for?");
                        String search = sc.nextLine();
                        try {
                            if (client.search(search).equals("null")) {
                                System.out.println("User not found");
                            } else {
                                System.out.println(client.search(search));
                            }
                            client.kill();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case "add friend" -> {
                        System.out.println("Who do you want to add as a friend?");
                        String friend = sc.nextLine();
                        try {
                            boolean value = client.addFriend(friend);
                            if (value) {
                                System.out.println("Friend added");
                            } else {
                                System.out.println("Friend not added because they " + 
                                    "are already a friend or blocked or do not exist");
                            }
                            client.kill();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case "remove friend" -> {
                        System.out.println("Who do you want to remove as a friend?");
                        String friend = sc.nextLine();
                        try {
                            boolean value = client.removeFriend(friend);
                            if (value) {
                                System.out.println("Friend removed");
                            } else {
                                System.out.println("Friend not removed because they are not " +
                                    "a friend or blocked or do not exist");
                            }
                            client.kill();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case "block" -> {
                        System.out.println("Who do you want to block?");

                        String block = sc.nextLine();
                        //check if user exists
                        String search = client.search(block);
                        if (search.equals("null")) {
                            System.out.println("User not found");
                            client.kill();
                        }
                        try {
                            client.blockUser(block);
                            client.kill();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case "unblock" -> {
                        System.out.println("Who do you want to unblock?");
                        String unblock = sc.nextLine();
                        String search = client.search(unblock);
                        if (search.equals("null")) {
                            System.out.println("User not found");
                            client.kill();
                        }
                        try {
                            boolean value = client.unblockUser(unblock);
                            if (value) {
                                System.out.println("User unblocked");
                            } else {
                                System.out.println("User not unblocked because they are not blocked");
                            }
                            client.kill();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case "change username" -> {
                        System.out.println("What do you want your new username to be?");
                        String newUsername = sc.nextLine();
                        try {
                            boolean val = client.setUserName(newUsername);
                            if (val) {
                                System.out.println("Username changed");
                            } else {
                                System.out.println("Username not changed because it already exists");
                            }
                            client.kill();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case "change password" -> {
                        System.out.println("What do you want your new password to be?");
                        String newPassword = sc.nextLine();
                        try {
                            client.setPassword(newPassword);
                            client.kill();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case "exit" -> {
                        client.kill();
                    }
                }
            }
            case "message" -> {
                System.out.println("Send, delete, edit, read?");
                String messageChoice = sc.nextLine();
                switch (messageChoice.toLowerCase()) {
                    case "send" -> {
                        do {
                            System.out.println("Who do you want to send a message to?");
                            String receiver = sc.nextLine();
                            System.out.println(receiver);
                            if (client.search(receiver).equals("null")) {
                                System.out.println("User not found");
                            } else {
                                System.out.println("Write a message");
                                String message = sc.nextLine();
                                try {
                                    client.sendMessage(receiver, message, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        } while (true);
                    }
                    case "delete" -> {
                        System.out.println("What is the id of the message you want to delete?");
                        String idString = sc.nextLine();
                        int id = Integer.parseInt(idString);
                        try {
                            client.deleteMessage(id);
                            client.kill();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case "edit" -> {
                        System.out.println("What message do you want to edit? (id)");
                        String idString = sc.nextLine();
                        int id = Integer.parseInt(idString);
                        try {
                            System.out.println("what do you want to say?");
                            String content = sc.nextLine();
                            client.editMessage(id, content);
                        } catch (Exception e) {
                            System.out.println("put in good id's you bozo");
                        }
                        client.kill();
                    }
                    case "read" -> {
                        System.out.println("who do you want to read from");
                        String person = sc.nextLine();
                        try {
                            client.getConversation(person);
                        } catch (Exception e) {
                            System.out.println("some unknown error occured");
                        }
                    }
                    case "exit" -> client.kill();
                }
            }
        }
    }
}
