import java.util.ArrayList;
/**
 * Team Project -- Message
 *
 * This file handles the threads for the server
 *
 * @author Alan Yi, 7
 *
 * @version November 1, 2024
 *
 */
public class serverThread extends Thread {
    private User user;
    private Action action;
    private String[] values;
    private UserDatabase userDB;
    private MessageDatabase messageDatabase;

    public serverThread(UserDatabase userDB, MessageDatabase messageDatabase, User user, Action action, String[] values) {
        this.userDB = userDB;
        this.messageDatabase = messageDatabase;
        this.user = user;
        this.action = action;
        this.values = values;
    }

    @Override
    public void run() {
        switch (action) {
            case SEARCH:
                this.userDB.getUser(this.values[0]);
                break;
            case ADD_FRIEND:
                User otherUser = this.userDB.getUser(this.values[0]);
                this.user.addFriend(otherUser);
                break;
            case REMOVE_FRIEND:
                User otherUser2 = this.userDB.getUser(this.values[0]);
                this.user.removeFriend(otherUser2);
                break;
            case BLOCK:
                User otherUser3 = this.userDB.getUser(this.values[0]);
                this.user.blockUser(otherUser3);
                break;
            case UNBLOCK:
                User otherUser4 = this.userDB.getUser(this.values[0]);
                this.user.unblockUser(otherUser4);
                break;
            case POST:
                // todo
                break;
            case GET_SENT_MESSAGES:
                ArrayList sentMessages = this.messageDatabase.getSentMessages();
                break;
            case GET_RECIEVED_MESSAGES:
                ArrayList recievedMessages = this.messageDatabase.getRecievedMessages();
                break;
            case RECOVER_MESSAGES:
                this.messageDatabase.recoverMessages();
                break;
            case GET_USER:
                User user = this.messageDatabase.getUser();
                break;
            case SEND_MESSAGE:
                Message mToSend = new Message(this.values[0]);
                this.messageDatabase.sendMessage(mToSend);
                break;
            case DELETE_MESSAGE:
                Message mToDelete = new Message(this.values[0]);
                this.messageDatabase.deleteMessage(mToDelete);
                break;
            case EDIT_MESSAGE:
                Message oldMessage = new Message(this.values[0]);
                Message newMessage = new Message(this.values[1]);
                messageDatabase.editMessage(oldMessage,newMessage);
                break;
            default:
                break;
        }
    }
}
