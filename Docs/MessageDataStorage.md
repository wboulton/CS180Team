Message data storage is handled in two different files, Message.java and MessageDatabase.java. 

# Message.java
Message.java handles the creation and manipulation of the Message object, including: 
messageid, sender username, reciever username, text content, if it has a picture, and picture content. 
When stored as a pipe separated line, the message is represented as follows: 
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
The message file contains two constructors, one that creates a message with two users and text content and one that takes a csv line
and creates a message based on the csv data. There is no error handling in the csv type, so all data passed to it must be accurate. 
```java
public Message(String data)
public Message(User sender, User reciever, String content)
```
Neither constructor initializes the picture, they simply set containsPicture to false and the method addPicture() must be run to 
create the picture file. 
the second constructor handles messageID's. this will be the constructor we use when sending new messages, as it reads from the id
file and assigns the message a new id. The other constructor simply reads an existing id. 

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
program for a user, the recoverMessages() function can be called to recover all sent and recieved messages from the current user's file. 