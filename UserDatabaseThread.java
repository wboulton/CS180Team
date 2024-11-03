/**
 * Team Project -- Message
 *
 * This file handles the threads for the User Database
 *
 * @author Alan Yi, 7
 *
 * @version November 1, 2024
 *
 */
public class UserDatabaseThread extends Thread {
    private Action action;
    private String[] values;
    private UserDatabase userDB;
 
    public UserDatabaseThread(UserDatabase userDB, Action action, String[] values) {
        this.userDB = userDB;
        this.action = action;
        this.values = values;
    }

    @Override
    public void run() {
        try{
            switch (action) {
                case CREATE_USER:
                    this.userDB.createUser(values[0], values[1], values[2], values[3], values[4]);
                    break;
                case VERIFY_LOGIN:
                    boolean login = this.userDB.verifyLogin(values[0], values[1]);
                    break;
                case CHANGE_USERNAME:
                    User userToModify = this.userDB.getUser(values[0]);
                    this.userDB.changeUsername(userToModify, values[1]);
                    break;
                case SEARCH:
                    this.userDB.getUser(this.values[0]);
                    break;
                case ADD_FRIEND:
                    User user = this.userDB.getUser(this.values[0]);
                    User otherUser = this.userDB.getUser(this.values[1]);
                    this.userDB.addFriend(user,otherUser);
                    break;
                case REMOVE_FRIEND:
                    User user2 = this.userDB.getUser(this.values[0]);
                    User otherUser2 = this.userDB.getUser(this.values[1]);
                    this.userDB.removeFriend(user2, otherUser2);
                    break;
                case BLOCK:
                    User user3 = this.userDB.getUser(this.values[0]);
                    User otherUser3 = this.userDB.getUser(this.values[1]);
                    this.userDB.blockUser(user3, otherUser3);
                    break;
                case UNBLOCK:
                    User user4 = this.userDB.getUser(this.values[0]);
                    User otherUser4 = this.userDB.getUser(this.values[1]);
                    this.userDB.unblockUser(user4, otherUser4);
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
