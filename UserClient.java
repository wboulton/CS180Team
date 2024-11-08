import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;

public class UserClient implements UserClientInt {
    private User user;
    private static UserDatabase database;
    private MessageDatabase messageDatabase;
    public UserClient(String username, String password, String firstName, String lastName, String profilePicture) throws BadDataException {
        database = new UserDatabase();
        user = database.createUser(username, password, firstName, lastName, profilePicture);
        messageDatabase = new MessageDatabase(user);
    }


    @Override
    public void sendMessage(String receiver, String content, String picture) throws BadDataException {
        //check if the receiver exists
        User receiverUser = UserDatabase.getUser(receiver);
        //if the receiver does not exist, throw exception
        if (receiverUser == null) {
            throw new BadDataException("Receiver does not exist");
        }
        //if the receiver is blocked, throw exception
        if (user.getBlockedUsers().contains(receiver)) {
            throw new BadDataException("You have blocked this user");
        }
        if (receiverUser.getBlockedUsers().contains(user.getUsername())) {
            throw new BadDataException("You are blocked by this user");
        }
        //
        if (!receiverUser.isAllowAll()) {
            //if the user is not a friend, throw exception
            if (!receiverUser.getFriends().contains(user.getUsername())) {
                throw new BadDataException("You are not friends with this user");
            }
        }
        //create a new message
        Message newMessage = new Message(user, receiverUser, content);
        //the filepath of the picture from the user
        messageDatabase.sendMessage(newMessage);
        if (picture != null && !picture.equals("") && !picture.equals("false")) {
            File imageFile = new File(picture);
            try {
                byte[] imageData = Files.readAllBytes(imageFile.toPath());
                newMessage.addPicture(imageData);
            } catch (IOException e) {
                throw new BadDataException("Picture not found");
            }
        }
    }

    public void deleteMessage(String sender, Message m) throws BadDataException {
        User senderUser = UserDatabase.getUser(sender);
        //if the receiver does not exist, throw exception
        if (senderUser == null) {
            throw new BadDataException("Sender does not exist");
        }

        messageDatabase.deleteMessage(m);
    }

    public void editMessage(Message m, Message n) throws BadDataException {

    }

    public void recoverMessages() {

    }

    public ArrayList getSentMessages() {
        return null; //
    }

    public ArrayList getRecievedMessages() {
        return null;
    }

    public User getUser() {
        return null;
    }


    public void blockUser(User u) {
        //if the user is already blocked, return
        if (user.getBlockedUsers().contains(u.getUsername())) {
            return;
        }
        //block the user
        UserDatabase.blockUser(user, u);
    }

    public boolean unblockUser(User u) {
        //if the user is blocked, unblock the user
        return UserDatabase.unblockUser(user, u);
    }

    public boolean addFriend(User u) {
        //if the user is blocked, return false
        if (user.getBlockedUsers().contains(u.getUsername())) {
            return false;
        }
        //if the user is already a friend, return false
        if (user.getFriends().contains(u.getUsername())) {
            return false;
        }
        //add the user to the friends list
        user.addFriend(u.getUsername());
        return true;
    }

    public boolean removeFriend(User u) {
        if (user.getFriends().contains(u.getUsername())) {
            user.removeFriend(u.getUsername());
            return true;
        }
        return false;
    }

    public String getUserName() {
        return user.getUsername();
    }

    public boolean setUserName(String name) {
        //check if the username is already taken
        if (UserDatabase.getUser(name) != null) {
            return false;
        }
        user.changeUsername(name);
        return true;
    }

    public boolean setPassword(String password) {
        //validate the password
        if (!UserDatabase.legalPassword(password)) {
            return false;
        }
        user.changePassword(password);
        return true;
    }

    public void setFilePath(String path) {

    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1010);
        try {
            UserClient client = new UserClient("test", "Testers123!", "test", "test", "false");
        } catch (BadDataException e) {
            System.out.println(e.getMessage());
        }

    }
}
