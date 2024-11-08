public interface ServerInterface {
    void connectUser();
    void readMessages(String username);
    void sendPicture(String pictureFile);
    void run();
    static void main(String[] args) {} //I am not sure the main method here is necessary
}
