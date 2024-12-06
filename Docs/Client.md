# UserClient.java
This file handles all requests to the server. This means that it is not able to access any of the databases and can only ask the server for information from the databases. Currently, it runs through the terminal but will eventually implement a GUI for users to interact with. 
This class implements the following fields: 
```java
    private User user;
    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    private ObjectInputStream input;
    private static int portNumber;
```
These are used to read from the server, write to the server, connect to the server and determine who the user that is logged in is. These are then used throughout the class to continue interacting with the server. With the current infastructure the only other file that the client needs to have access to is the [User.java](../User.java) file. This could be worked around to prevent the client from needing to have acces to User.java but seems unecessary. 

The [UserClient.java](../UserClient.java) class implements the [UserClientInt.java](../UserClientInt.java) interface. Per this interface it implements the following methods:
```java
    String sendMessage(String receiver, String content, String picture) throws BadDataException, IOException;
    void deleteMessage(int id) throws BadDataException, IOException;
    String editMessage(int id, String newContent) throws IOException;
    void blockUser(String u) throws IOException;
    boolean unblockUser(String u) throws IOException;
    boolean addFriend(String u) throws IOException;
    boolean removeFriend(String u) throws IOException;
    boolean setUserName(String name) throws IOException;
    boolean setPassword(String password) throws IOException;
    void addOrRemoveFriend(String username) throws IOException;
    void blockOrUnblock(String username) throws IOException;
```
These functions handle all of the information interaction between the client and the server. The last two functions were added later in the project while creating the GUI. These were added to allow a single button which would add or remove a friend based on current status. This means addOrRemoveFriend check is the current user is a friend and then removes them if they are and adds them if they are not. blockOrUnblock works the same way but with blocked users instead of friends. 

There is one important function which is not included in the interface, 
```java
    byte[] getViewingProfilePicture(String username) throws IOException;
```
this method is used to check the profile picture of the user that the client is currently viewing. This is used to display the profile picture of whoever you are messaging while you are messaging them. This method, along with other methods that include sending pictures use an objectInputStream/objectOutputStream to send byte arrays that represent the picture between the client and the network. 

# Testing
Testing the client side is difficult to do with Junit test cases. Instead each function was manually tested multiple times.

# Interacting with the client
Currently the client has no GUI. This means that all interactions happen with the terminal. When starting the client (be sure to run the server first) you will be prompted to say if you are a new or existing user, choose one depending on what you want to do. This will then prompt you for necessary information, keep in mind that for creating new users the password requirement is 8 character with one captial and one number. Then it will ask if you want to do a user or message function. User functions are: Search, add friend, remove friend, block, unblock, change username, change password. Message functions are: Send, delete, edit, read (where read means read all messages between you and another user). depending on what you choose, you will be prompted for more information. If you send a message, that message will not be displayed for you at this moment, only for the reciever. This will be handled better when a GUI is implemented. There is also no validation to ensure that you put in a valid operation. Again, this is just because validation would be handled better when we have an understanding of what the possible inputs will be when a GUI is implemented. 

_note_:
There are a lot of errors that appear in the terminal on both the server and client side. Most of these errors do not actually mean anything and the client and server run as expected otherwise. 