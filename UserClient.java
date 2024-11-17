import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Scanner;

public class UserClient implements UserClientInt {
    private User user;
    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    private ObjectInputStream input;

    // Constructor for existing user
    public UserClient(String username, String password) throws IOException, BadDataException {
        connectToServer();
        login(username, password);
    }

    // Constructor for new user
    public UserClient(String username, String password, String firstName, String lastName, String profilePicture) throws IOException, BadDataException {
        connectToServer();
        createNewUser(username, password, firstName, lastName, profilePicture);
    }

    private void kill() {
        writer.println("77288937499272");
        writer.flush();
    }

    private void connectToServer() throws IOException {
        socket = new Socket("localhost", 8080);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true); // Auto-flush
        input = new ObjectInputStream(socket.getInputStream());
    }

    private void login(String username, String password) throws IOException, BadDataException {
        writer.println("login");  // Send login command to the server
        writer.println(username);
        writer.println(password);

        String response = reader.readLine();
        if (response.equals("could not log in")) {
            throw new BadDataException("Invalid login credentials.");
        }
        System.out.println("Login successful.");
        try {
            this.user = (User) input.readObject();
            System.out.println(this.user.toString());
            System.out.println("USER CREATION SUCCESSFUL: " + this.user.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("the object was not a user");
        }
    }

    private void createNewUser(String username, String password, String firstName, String lastName, String profilePicture) throws IOException, BadDataException {
        String response = reader.readLine();
        if (response.equals("user created")) {
            this.input = new ObjectInputStream(this.socket.getInputStream());
            try {
                this.user = (User) input.readObject();
            } catch (Exception e) {
                System.out.println("the object was not a user");
            }
        } else {
            throw new BadDataException("User creation failed.");
        }
        writer.println(profilePicture);

        try {
            this.user = (User) input.readObject();
        } catch (Exception e) {
            System.out.println("the object was not a user");
        }
    }

    @Override
    public void sendMessage(String receiver, String content, String picture) throws BadDataException, IOException {
        // Send SEND_MESSAGE command to the server
        writer.println("message|" + "SEND_MESSAGE|" + user.getUsername() + "|" + receiver + "|" + content);

        if (picture != null && !picture.isEmpty() && !picture.equals("false")) {
            File imageFile = new File(picture);
            try {
                byte[] imageData = Files.readAllBytes(imageFile.toPath());
                writer.println("SEND_PICTURE|" + user.getUsername() + "|" + receiver + "|" + content + "|" + byteArrayToString(imageData));
            } catch (IOException e) {
                throw new BadDataException("Picture not found");
            }
        }
    }

    public void deleteMessage(int id) throws IOException {
        // Send DELETE_MESSAGE command
        writer.println("message|DELETE_MESSAGE|" + user.getUsername() + "|" + id);
    }

    public void editMessage(int id, String newContent) throws IOException {
        // Send EDIT_MESSAGE command
        writer.println("message|" + "EDIT_MESSAGE|" + id + "|" + newContent);
    }

    public void blockUser(String usernameToBlock) throws IOException {
        // Send BLOCK command
        writer.println("user|BLOCK|" + user.getUsername() + "|" + usernameToBlock);
    }

    public boolean unblockUser(String usernameToUnblock) throws IOException {
        // Send UNBLOCK command
        writer.println("user|UNBLOCK|" + user.getUsername() + "|" + usernameToUnblock);
        return reader.readLine().equals("true");
    }
    public void getConversation(String username) throws IOException {
        writer.println("message|SET_VIEWING|" + user.getUsername());
        writer.println("message|GET_CONVERSATION|" + username);
    }
    public boolean addFriend(String friendUsername) throws IOException {
        // Send ADD_FRIEND command
        writer.println("user|ADD_FRIEND|" + user.getUsername() + "|" + friendUsername);
        return reader.readLine().equals("true");
    }

    public boolean removeFriend(String friendUsername) throws IOException {
        // Send REMOVE_FRIEND command
        writer.println("user|REMOVE_FRIEND|" + user.getUsername() + "|" + friendUsername);
        return reader.readLine().equals("true");
    }

    // Helper method to convert byte array to string format for transmission
    private String byteArrayToString(byte[] byteArray) {
        StringBuilder sb = new StringBuilder();
        for (byte b : byteArray) {
            sb.append(b).append(",");
        }
        return sb.toString();
    }
    public String search(String username) {
        writer.println("user|SEARCH|" + username);
        writer.flush();
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public boolean setUserName(String name) throws IOException {
        writer.println("user|CHANGE_USERNAME|" + user.getUsername() + "|" + name);
        writer.flush();
        return reader.readLine().equals("true");
    }

    @Override
    public void setPassword(String password) {
        writer.println("user|CHANGE_PASSWORD|" + user.getUsername() + "|" + password);
        writer.flush();
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
                System.out.println("write or edit?");
                String choice = sc.nextLine();
                if (choice.equalsIgnoreCase("write")) {
                    System.out.println("Write a message");
                    String message = sc.nextLine();
                    client.sendMessage("name", message, null);
                    client.kill();
                } else if (choice.equalsIgnoreCase("edit")) {
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
                
            } catch (Exception e) {
                e.printStackTrace();
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
        //have a user or message option
        //if user, have a search, add friend, remove friend, block, unblock, change username, change password
        //if message, have a send, delete, edit
        //dont make a new client, use the existing client
        System.out.println("User or message?");
        String choice = sc.nextLine();
        switch (choice.toLowerCase()) {
            case "user" -> {
                System.out.println("Search, add friend, remove friend, block, unblock, change username, change password?");
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
                                System.out.println("Friend not added because they are already a friend or blocked or " +
                                        "do not exist");
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
                                System.out.println("Friend not removed because they are not a friend or blocked or " +
                                        "do not exist");
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
                System.out.println("Send, delete, edit?");
                String messageChoice = sc.nextLine();
                switch (messageChoice.toLowerCase()) {
                    case "send" -> {
                        do {
                            System.out.println("Who do you want to send a message to?");
                            String receiver = sc.nextLine();
                            if (client.search(receiver).equals("null")) {
                                System.out.println("User not found");
                            } else {
                                System.out.println("Write a message");
                                String message = sc.nextLine();
                                User user = new User(message.substring(message.indexOf("|") + 1));
                                try {
                                    client.sendMessage(user.getUsername(), message, null);
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
                    case "exit" -> client.kill();
                }
            }
        }
    }
}
