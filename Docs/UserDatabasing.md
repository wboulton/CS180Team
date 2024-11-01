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
