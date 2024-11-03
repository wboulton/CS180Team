import java.util.ArrayList;
/**
 * Team Project -- MessageDatabaseThread
 *
 * This file handles the threads for the Message Database
 *
 * @author Alan Yi
 *
 * @version November 1, 2024
 *
 */
public class MessageDatabaseThread extends Thread{
    private Action action;
    private String[] values;
    private MessageDatabase messageDatabase;
 
    public MessageDatabaseThread(MessageDatabase messageDatabase, Action action, String[] values) {
        this.messageDatabase = messageDatabase;
        this.action = action;
        this.values = values;
    }

    @Override
    public void run() {
        try{
            switch (action) {
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
                    messageDatabase.editMessage(oldMessage, newMessage);
                    break;
                default:
                    break;
            }
        }
        catch(IndexOutOfBoundsException e){
            System.out.println("OUT OF BOUNDS, CHECK PARAMETERS");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
        
}
