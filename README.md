# Welcome to the our social media app (tentatively named "SocialMediaApp")

This readme gives a brief introduction to all of the classes and functions of our app so far. For more detailed documentation visit the Docs folder: [a relative link](Docs/). 

# Interfaces
Our file contains four interfaces, one for each main file. This means we have an interface for users, the userDatabase, messages, and the messageDatabase. The interfaces pair with the java classes as follows:
 - ___ : UserDatabase.java
 - ___ : User.java
 - MData.java : MessageDatabase.java
 - MessageInterface.java : Message.java

These files contain necessary methods for each of the classes that implement them. At this point, none of them contain any global variables that get used by the implementing classes and none of them are implemented in more than one class. This does not mean that there will not be implementations for more classes later. 

# Messages
Message handling is contained in two files, MessageDatabase.java and Message.java. Broadly, we store store all of the messages that a user recieves and sends in a file named "username.txt" (where username is replaced with the username of the in-question user). Each message is stored as a single csv line of the format "messageID,sender,reciever,content,containsPicture,pictureFile" (It is important to note that the pictureFile string and preceding comma do not exist if containsPicture is false). For more information about this visit [a relative link](Docs/MessageDataStorage.md). 

_Message.java_
This class handles the creation and manipulation of the message object. Per the interface, it contains the following methods:
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
Not all of which are currently under use. It contains two constructors, one for creating messages based on csv lines stored in the database files and one for creating new messages with the sending user, recieving user, and message content. Messages are not initiallized with a picture, they must be added after the creation of the message. For more information about this, visit [a relative link](Docs/MessageDataStorage.md).

_MessageDatabase.java_
This class handles all of the data storage for messages and allows for users to send, delete, and edit messages. Per the interface, it contains the following methods:
```java
    ArrayList getSentMessages();
    ArrayList getRecievedMessages();
    void recoverMessages();
    User getUser();
    String getFilePath();
    void sendMessage(Message m);
    void deleteMessage(Message m);
    void editMessage(Message m, Message n);
``` 
The most important methods in this file are recoverMessages(), which retrieves all of the user's sent and recieved messages and stores them in two different arrayLists, and the send, edit, and delete Message methods.
Each MessageDatabase object represents the message data for an individual user. This user is stored in the field 
```java
private User user
```
for more information about how these files are formatted or how the methods within the MessageDatabase class function, see [a relative link](Docs/MessageDataStorage.md).