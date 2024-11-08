public interface ServerInterface {
    void connectUser();
    void readMessages(String username);
    void run();
    static void main(String[] args) {} //I am not sure the main method here is necessary
    boolean validateUser(String username, String password); //this will just call verifyLogin in userdatabase.java
}
