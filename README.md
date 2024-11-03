# Welcome to the our social media app (tentatively named "SocialMediaApp")

This readme gives a brief introduction to all of the classes and functions of our app so far. For more detailed documentation visit the Docs folder: [Documentation](Docs/). 

# How to run
At this stage in the project (phase 1) the project does not run as a social media app, to test it you can use the test cases contained in the [testing.md](Docs/testing.md) file. Compile all of the classes using javac ___ then run them that way.

# Submissions


# Interfaces
Our file contains four interfaces, one for each main file. This means we have an interface for users, the userDatabase, messages, and the messageDatabase. The interfaces pair with the java classes as follows:
 - UserDBInt.java : UserDatabase.java
 - UserInt.java : User.java
 - MData.java : MessageDatabase.java
 - MessageInterface.java : Message.java

These files contain necessary methods for each of the classes that implement them. At this point, none of them contain any global variables that get used by the implementing classes and none of them are implemented in more than one class. This does not mean that there will not be implementations for more classes later. 

# Messages
Message handling is contained in two files, MessageDatabase.java and Message.java. Broadly, we store all of the messages that a user recieves and sends in a file named "username.txt" (where username is replaced with the username of the in-question user). Each message is stored as a single pipe separated value (psv) line of the format "messageID|sender|reciever|content|containsPicture|pictureFile" (It is important to note that the pictureFile string and preceding comma do not exist if containsPicture is false). For more information about this visit [Message Data](Docs/MessageDataStorage.md). 

_Message.java_

This class handles the creation and manipulation of the message object. It holds all of the information necessary to send a message to another user, including message content (text), an internal message id, the sender and reciever usernames, and (optionally) a picture. 
Per the interface, it contains the following methods:
```java
    String getSender();
    String getReciever();
    String getContent();
    byte[] getPicture();
    boolean hasPicture();
    String toString();
    int getMessageID();
    void editMessage(String content); 
    void addPicture(byte[] pictureContent);
    void editPicture(byte[] pictureContent);
    void readMessage(); // currently not in use
    void setMessageID(int id);
```
Not all of which are currently under use. It contains two constructors, one for creating messages based on psv lines stored in the database files and one for creating new messages with the sending user, recieving user, and message content. Messages are not initiallized with a picture, they must be added after the creation of the message. For more information about this, visit [Message Data](Docs/MessageDataStorage.md).

_MessageDatabase.java_

This class handles all of the data storage for messages and allows for users to send, delete, and edit messages. Per the interface, it contains the following methods:
```java
    ArrayList getSentMessages();
    ArrayList getRecievedMessages();
    void recoverMessages();
    User getUser();
    String getFilePath();
    void sendMessage(Message m) throws BadDataExeption;
    void deleteMessage(Message m) throws BadDataException;
    void editMessage(Message m, Message n) throws BadDataException; 
``` 
The most important methods in this file are recoverMessages(), which retrieves all of the user's sent and recieved messages and stores them in two different arrayLists, and the send, edit, and delete Message methods. This class also validates who messages can be sent to before making adjustments to a user's messages in the sendMessage file. It checks to make sure that the reciever has not blocked the sender, and if the reciever only allows friends to send messages it checks if the sender is a friend.
Each MessageDatabase object represents the message data for an individual user. This user is stored in the field 
```java
private User user
```
for more information about how these files are formatted or how the methods within the MessageDatabase class function, see [Message Data](Docs/MessageDataStorage.md).

# Users
User handling is contained within two files, UserDatabase.java and User.java. Briefly, we store all of the users for the app in a single file named "users.txt" and read through that whenever we need to re-initialize the app. Each user is stored as a single psv line containing all of the private information and friendship information of the user. Each line is stored in the following format: 
username|password|firstName|lastName|friends|blockedUsers|profilePicture
for more detials on what each field represents, see [User Data](Docs/UserDatabasing.md).

_User.java_

This class handles the user object, which is created for each person who uses the social media app. It stores all of the information regaurding a user's friends, blocked users, and account information. To handle this information, the file has the following methods:
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
    void changePassword(String newPassword);
    String listToString(ArrayList<User> list);
```
Methods generally work as expected, more information on the methods can be found at [User Data](Docs/UserDatabasing.md). The verify login method checks if the user's inputted password matches the password that they have previously set for their account. This check directly using the String.equals() method. When the app is implemented over a network this will be insecure and is subject to change. The listToString method represents an ArrayList as a comma separated string similar to the way Arrays are represented using the Arrays.toString() method. 

_UserDatabase.java_

This class interacts with the User.java file in order to create and handle users contained within the database. It also handles all of the reading and writing to the "users.txt" file. To handle this, it contains the following methods: 
```java
    void createUser(String username, String password, String firstName, String lastName, String profilePicture)
        throws BadDataException;
    void removeFriend(User user, User friend);
    void addFriend(User user, User friend)
    void blockUser(User user, User blockedUser);
    boolean unblockUser(User user, User blockedUser);
    User getUser(String username);
    boolean verifyLogin(String username, String password);
    void changeUsername(User user, String newUsername);
    boolean legalPassword(String password);
    void writeDB(User user);
    void updateDB();
    void load();
```
The load function handles reading through all of the users written to "users.txt" upon start up and storing them in the program once again. The writeDB function writes all of the users stored in program memory (an arraylist) to the "users.txt" file to be reused again later. Validating user creation happens in this file, where we ensure that the password and other user inputs are valid. Blocking, unblocking, Friending, and unfriending is all handled within this class as well. For more information on UserDatabase.java, see [User Data](Docs/UserDatabasing.md)

# Exceptions
This project has one custom exception: BadDataException. In general this exception is used when a user input passed contains an invalid character or does not fufill requriements (i.e. password requirements). This exception is very standard and just calls the constructor of the Exception class with the passed message. 

# Threading
Threading is handled by synchronizing all calls to shared resources (files and static variables) in each file. To handle the creation of new threads, this project creates two new files to call actions within the MessageDatabase and UserDatabse on a new thread. These files create a run method which uses a switch statement to determine which function from the database class to run. They are both passed a database object to run these methods from. All actions are contained in the [Action.java](Action.java) enumeration file. To read more about how threading is handled in this project, including information about the two classes involved in handling threading, visit the [Threading Docs](Docs/threading.md).

# Testing
Each class has it's own test file, each of which are listed and linked in the [testing](Docs/testing.md) file. Each test file includes multiple test cases for each method and constructor in the class that it is testing and a large test that tests how all of the methods work together and checks the data storage in a file. 