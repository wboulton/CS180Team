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
        } catch (Exception e) {
            System.out.println("the object was not a user");
        }
    }

    private void createNewUser(String username, String password, String firstName, String lastName, String profilePicture) throws IOException, BadDataException {
        writer.println("new user");  // Send new user command
        writer.println(username);
        writer.println(password);
        writer.println(firstName);
        writer.println(lastName);
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

    public void deleteMessage(String sender, Message m) throws IOException {
        // Send DELETE_MESSAGE command
        writer.println("DELETE_MESSAGE|" + sender + "|" + m.getMessageID());
    }

    public void editMessage(int id, String newContent) throws IOException {
        // Send EDIT_MESSAGE command
        writer.println("message|" + "EDIT_MESSAGE|" + id + "|" + newContent);
    }

    public void blockUser(String usernameToBlock) throws IOException {
        // Send BLOCK command
        writer.println("BLOCK|" + user.getUsername() + "|" + usernameToBlock);
    }

    public void unblockUser(String usernameToUnblock) throws IOException {
        // Send UNBLOCK command
        writer.println("UNBLOCK|" + user.getUsername() + "|" + usernameToUnblock);
    }
    public void getConeversation(String username) throws IOException {
        writer.println("message|SET_VIEWING|" + user.getUsername());
        writer.println("message|GET_CONVERSATION|" + username);
    }
    public void addFriend(String friendUsername) throws IOException {
        // Send ADD_FRIEND command
        writer.println("ADD_FRIEND|" + user.getUsername() + "|" + friendUsername);
    }

    public void removeFriend(String friendUsername) throws IOException {
        // Send REMOVE_FRIEND command
        writer.println("REMOVE_FRIEND|" + user.getUsername() + "|" + friendUsername);
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
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void setUserName(String name) {
        writer.println("CHANGE_USERNAME|" + user.getUsername() + "|" + name);
    }

    @Override
    public void setPassword(String password) {
        writer.println("CHANGE_PASSWORD|" + user.getUsername() + "|" + password);
    }

    public static void main(String[] args) {        
        Scanner sc = new Scanner(System.in);
        System.out.println("new or existing user?");
        String existance = sc.nextLine();
        if (existance.equalsIgnoreCase("existing")) {
            System.out.println("Username: ");
            
            String username = sc.nextLine();
            System.out.println("Password: ");
            String password = sc.nextLine();
            try {
                UserClient client = new UserClient(username, password);
                client.input = new ObjectInputStream(client.socket.getInputStream());
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
                        System.out.println("put it good id's you bozo");
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
                UserClient client = new UserClient(username, password, firstName, lastName, profilePicture);
                client.input = new ObjectInputStream(client.socket.getInputStream());
                System.out.println("write or edit?");
                String choice = sc.nextLine();
                if (choice.equalsIgnoreCase("write")) {
                    do {
                        System.out.println("Who do you want to send a message to?");
                        String receiver = sc.nextLine();
                        if (client.search(receiver).equals("null")) {
                            System.out.println("User not found");
                        } else {
                            break;
                        }
                    } while (true);
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
                        System.out.println("put it good id's you bozo");
                    }
                    client.kill();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
