public class serverThread extends Thread{
    private User user;
    private Action action;
    private String value;
    private UserDatabase userDB;

    public serverThread(UserDatabase userDB, User user, Action action, String value){
        this.userDB = userDB;
        this.user = user;
        this.action = action;
        this.value = value;
    }

    @Override
    public void run(){
        switch (action) {
            case SEARCH:
                this.userDB.getUser(this.value);
                break;
            case ADD_FRIEND:
                User otherUser = this.userDB.getUser(this.value);
                this.user.addFriend(otherUser);
                break;
            case REMOVE_FRIEND:
                User otherUser2 = this.userDB.getUser(this.value);
                this.user.removeFriend(otherUser2);
                break;
            case BLOCK:
                User otherUser3 = this.userDB.getUser(this.value);
                this.user.blockUser(otherUser3);
                break;
            case UNBLOCK:
                User otherUser4 = this.userDB.getUser(this.value);
                this.user.unblockUser(otherUser4);
                break;
            case POST:
                
                break;
            default:
                break;
        }
    }
}
