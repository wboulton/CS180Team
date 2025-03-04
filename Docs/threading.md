The threading is divided into the message database thread and user database thread. The Action enum contains the actions that can be performed on the databases. 

Thread-safe procedures including creating a static object and using the synchronized keyword:

```java
public static final Object lock = new Object();
synchronized (lock) {
    //thread-safe action
}
```

# MessageDatabaseThread.java (removed)
This class handles the threading for Message Database. The actions for the message database include getting sent messages, getting recieved messages, recovering messages, getting a user, sending a message, deleting a message, and editting a message.

The parameters for constructing a MessageDatabaseThread are:
```java
MessageDatabase messageDatabase
Action action
String[] values
```

The messageDabase variable stores the particular database the action is being formed for.

The action variable contains what action is being performed on the database: 
```java
EDIT_USER, UNBLOCK, GET_SENT_MESSAGES, GET_RECIEVED_MESSAGES, RECOVER_MESSAGES,
GET_USER, SEND_MESSAGE, DELETE_MESSAGE, EDIT_MESSAGE
```

"Values" contains the particular parameters to be performed with the actions. For example, in the EDIT_MESSAGE action, values[0] contains the message to be editted and values[1] contains the new message.

If there are not enough elements passed into the Values parameter for the particular action, an IndexOutOfBoundsException will be thrown and program will print a notice to check the parameters. Other exceptions will have their stack traces printed. 

# UserDatabaseThread.java (removed)

The UserDatabaseThread handles the threading for the user database class. The actions for the userdatabase thread include creating a user, verifying login, changing a username, searching for a user, adding a friend, removing a friend, blocking a user, and unblocking a user.

The parameters for constructing a MessageDatabaseThread are:
```java
UserDatabase userDB
Action action
String[] values
```

userDB contains the particular user database to perform the actions on.

The enums for actions include: 
```java
SEARCH, ADD_FRIEND, REMOVE_FRIEND, BLOCK, CREATE_USER, VERIFY_LOGIN, CHANGE_USERNAME
```

Likewise, the Values variable contains the particular parameters to be performed with the actions. For example, in the CREATE_USER action, values[0]: username, values[1]: password, values[2]: password, values[3]: first name, values[4]:last name, values[5]: profile picture


If there are not enough elements passed into the Values parameter for the particular action, an IndexOutOfBoundsException will be thrown and program will print a notice to check the parameters. Other exceptions will have their stack traces printed. 

# Network threads
The Server creates a thread to interact with each client trying to connect with it. To do this, we use lambda functions which create the new thread with a run method defined in the same class and pass necessary variables with it. This way we do not need to create another class or another object that needs to be passed to the Thread object. The downside of running threads this way is that all information passed to the thread must be effectively final and the threads cannot access object fields. This limits our flexibility with datatypes and structure. To get around this, we define a new final variable socket each time a user tries to connect and create the thread with that final socket. With each thread a TimerTask from the java.util package is created in order to update messages and alow for real time messaging. This also works as another thread which updates the messages that have been sent to the user each time it gets run (every three seconds). The run method for this class also needs effectively final variables to run, so it uses an atomic variable currentlyViewing which represents the user that the client is currently looking at messages from. 