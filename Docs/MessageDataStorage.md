Message data storage is handled in two different files, Message.java and MessageDatabase.java. 

# Message.java
Message.java handles the creation and manipulation of the Message object, including: 
messageid, sender username, reciever username, text content, if it has a picture, and picture content. 
When stored as a pipe separated line (psv), the message is represented as follows: 
messageID|sender|reciever|content|containsPicture|pictureFile
However, pictureFile (and the comma preceding it) only appear if containsPicture is true. 
here is a breakdown of those fields: 
```java 
    private String sender;
    private String reciever;
    private String content;
    private boolean containsPicture;
    private int messageID;
    private String pictureFile;
```
The message file contains two constructors, one that creates a message with two users and text content and one that takes a pipe separated values (psv) line.
and creates a message based on the psv data. There is no error handling in the psv type, so all data passed to it must be accurate. 
```java
public Message(String data)
public Message(User sender, User reciever, String content)
```
Neither constructor initializes the picture, they simply set containsPicture to false and the method addPicture() must be run to 
create the picture file. 
the second constructor handles messageID's. this will be the constructor we use when sending new messages, as it reads from the id
file and assigns the message a new id. The other constructor simply reads an existing id. 
Per the interface, message.java has the following methods: 
```java
    String getSender();
    String getReciever();
    String getContent();
    byte[] getPicture();
    boolean hasPicture();
    String toString();
    int getMessageID();
    void setMessageID(int id);
    void editMessage(String content); 
    void addPicture(byte[] pictureContent);
    void editPicture(byte[] pictureContent);
    void readMessage();// currently not in use, will be for displaying messages to users. 
```
These methods work generally as expected. When addPicture() is called, a new picture file is created by finding the current number stored in the "picture.txt" file and using that number as the name of the file containing the picture. This then increments that number and stores it in the "picture.txt" file again to be used later. All pictures are immediately stored as jpg files after being created. This again helps with memory persistance. 

# MessageDatabase.java
MessageDatabase.java is the file that handles each user's database of messages. It is intended to create a new text file for each user
and store all of the messages that they have sent or recieved in that file. This file contains the send message and delete message functions
which both take one message as an argument (no reciever is necessary as that will be passed to the message upon creation). It also 
allows for editing messages by replacing one message with a new one that has the same id. My intention for displaying these messages was
to sort them in order of id because message id counts up. 

This database is constructed with just the current user, and the object will contain information about all of the user's messages
```java
    public MessageDatabase(User user) {
            this.user = user;
            filePath = String.format("%s.txt", user.getUsername());
            recievedMessages = new ArrayList<>();
            sentMessages = new ArrayList<>();
        }
```

The program itself stores messages in two different arraylists, one for sent messages and one for recieved messages. Upon starting the 
program for a user, the recoverMessages() function can be called to recover all sent and recieved messages from the current user's file. All functions are listed below:
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
The recoverMessages() method reads all of the messages currently in a user's file and adds them into the program memory as an array list of messages. This will use the first constructor from the Message.java file which parses the psv string for the information required to create the message object. Send, delete, and edit message actually handle adding messages to the sender and reciever's respective databases. Delete message assumes that a valid message will be passed to be deleted, but if an invalid message is passed it will simply print that the message did not exist. Send message ensures that the reciever can actually recieve messages from the sender (i.e. the sender is not blocked and, if the reciever only allows friends, the sender is a friend). If the sender is not allowed to send to the recieving user, the message does not get added to either database. With this system the message still gets created, which allows more flexibility with how we handle it after failure in later stages of the project, but may be changed. Currently this function also checks to make sure that both the sending and recieving user exist before sending the message. Generally, this should not be a problem as long as the server requires the message to be sent with a reciever and the sender is just taken from the current logged in user. 

The methods for sending, deleting, and editing messages throw badDataExceptions if the messages do not exist or cannot be sent for access reasons.