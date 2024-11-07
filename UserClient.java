import java.util.ArrayList;

public class UserClient implements UserClientInt {
    /**
     * @param m
     * @throws BadDataException
     */
    @Override
    public void sendMessage(Message m) throws BadDataException {

    }

    /**
     * @param m
     * @throws BadDataException
     */
    @Override
    public void deleteMessage(Message m) throws BadDataException {

    }

    /**
     * @param m
     * @param n
     * @throws BadDataException
     */
    @Override
    public void editMessage(Message m, Message n) throws BadDataException {

    }

    /**
     *
     */
    @Override
    public void recoverMessages() {

    }

    /**
     * @return
     */
    @Override
    public ArrayList getSentMessages() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public ArrayList getRecievedMessages() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public User getUser() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public String getFilePath() {
        return "";
    }

    /**
     * @param u
     */
    @Override
    public void blockUser(User u) {

    }

    /**
     * @param u
     */
    @Override
    public void unblockUser(User u) {

    }

    /**
     * @param u
     */
    @Override
    public void addFriend(User u) {

    }

    /**
     * @param u
     */
    @Override
    public void removeFriend(User u) {

    }

    /**
     * @return
     */
    @Override
    public String getUserName() {
        return "";
    }

    /**
     * @param name
     */
    @Override
    public void setUserName(String name) {

    }

    /**
     * @param password
     */
    @Override
    public void setPassword(String password) {

    }

    /**
     * @param path
     */
    @Override
    public void setFilePath(String path) {

    }
}
