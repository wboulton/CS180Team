User Data storage is handled by two different files, User.java and UserDatabase.java.

# User.java
This file handles all of the creation and modification of the user object. This includes handling friendships, blockages, usernames, and password validation. When creating an object, the User class obtains a username, password, firstname, lastname, and profile from the constructor and sets it to the appropriate field (all of which are strings). None of these strings allow pipes. Every call to the constructor will have valid parameters passed to it, as all validation is done before creation of users. When users are stored in the database, they are stored in a pipe separated line (psv) with the following format:
username|password|firstName|lastName|friends|blockedUsers|profilePicture
here is the breakdown of those fields:
```java
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private ArrayList<User> friends;
    private ArrayList<User> blockedUsers;
```
This file simply contains one constructor, as the handling for splitting lines read from the database is handled in the UserDatabase.java file. This constructor handles all creation of user objects with the following signature:
```java
public User(String username, String password, String firstName, String lastName, String profile)
```
This file also contains all of the logic for the functions to handle and maintain friendships and user information. Per the UserInterface, it contains the following methods:
```java
    boolean addFriend(User user);
    boolean removeFriend(User user);
    boolean blockUser(User user);
    boolean unblockUser(User user);
    String getUsername();
    boolean verifyLogin(String password);
    boolean equals(User user);
    String toString();
    BufferedImage getProfilePicture();
    void changeUsername(String newUsername);
    String listToString(ArrayList<User> list);
```
All methods generally work as expected. The method listToString() is used as a helper method to convert arraylists to useable strings to be stored in the user database file. This is how the program stores the list of friends and list of blocked users (friends and blocked users are stored simply as their username). Currently, the verifyLogin() method just recieves a password and checks if it is the same string, however, it would be nice to implement some real password validation which does not send a password to the server (this is very insecure).

# UserDatabase.java
This file handles the creation of new users and user sign in, along with calling methods from user.java to block and unblock users and add users as friends. This data base interacts with only one database file called "users.txt". The constructor for this class checks if the files for messages and for users exist. At this time, the message file is depreciated and does not do anything within the rest of the code. The constructor then calls the load() method, which reads the data from the users.txt file and loads all of the users contained in that file into a private arraylist. The UserDatabase constructor takes in 0 arguments. 

This function handles all user creation and interaction with the help of the methods defined in User.java. Per the interface, it has the following methods:
```java
    void createUser(String username, String password, String firstName, String lastName, String profilePicture)
        throws BadDataException;
    void removeFriend(User user, User friend);
    void blockUser(User user, User blockedUser);
    boolean unblockUser(User user, User blockedUser);
    User getUser(String username);
    boolean verifyLogin(String username, String password);
    void changeUsername(User user, String newUsername);
    boolean legalPassword(String password);
    void writeDB(User user);
    void load();
```
The createUser() method throws the custom exception BadDataException when one of the strings passed to it is illegal. This could be because it contains the illegal character "|" or it has no information. the load() method reads each line in users.txt and splits it into multiple strings which then get passed to the createUser constructor. These created users then get added into the users arraylist. The load method also directly adds the blocked users and friends to the user object using the block user/add friend functions in user.java. 

Password validation only allows ascii characters and numbers: 
```java
    return password.length() >= 8 && password.matches(".*[0-9].*") 
        && password.matches(".*[A-Z].*") && password.matches(".*[a-z].*");
```