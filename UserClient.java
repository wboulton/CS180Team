import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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

    public String getFilePath() {
        return "";
    }

    public void blockUser(User u) {

    }

    public void unblockUser(User u) {

    }

    public void addFriend(User u) {

    }

    public void removeFriend(User u) {

    }

    public String getUserName() {
        return null;
    }

    public void setUserName(String name) {

    }

    public void setPassword(String password) {

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
