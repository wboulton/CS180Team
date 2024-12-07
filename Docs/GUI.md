# SignInGUI.java
The SignInGUI.java file takes one command line argument to pass an integer port number to the main method. This then immediately creates the GUI on a SwingUtilities.invokeLater thread. This GUI takes in user inputs from the following text fields and file chooser:
```java
    JTextField username;
    JTextField password;
    JTextField firstName;
    JTextField lastName;
    JFileChooser profilePicture;
```
The when you click the sign in button, the text from the username and password fields are then retrieved and sent to the Network to sign in. If the sign in is successful, the username is passed to the constructor of GUI client. If you are creating a new user, it instead tries to create the user with the server, and if the server successfully creates the user it will pass the username to the constructor of GUI client and start the main GUI. 

# GUIClient.java
This file contains the main GUI for the social media app. To create all interactive and supporting elements of the GUI, the class has the following fields: 
```java
    ArrayList<String> usernames;
    ArrayList<String> displayUsers;
    ArrayList<String> messages;

    UserClient client;

    JButton profileButton;
    JButton friendButton;
    JButton blockButton;
    JLabel userImage;
    JButton sendButton;
    JTextPane messageField;
    JList<String> userList;
    JButton searchButton;
    JTextField searchField;
    JTextArea viewingUser;
    JList<String> messageJList;
    JFileChooser profilePictureChooser;
    String viewingUsername;
    String clientUsername;
    JButton addPicToMessage;

    private Timer messageUpdateTimer;
    private File sendPic;
```

There are two primary panes contained in the GUIClient class, the main pain contained in the run method of the invokeLater thread, and the pane created by the editProfile() method. The main pane in the run method handles all messaging, user search and selection, and friending/blocking users. To do this, we have one action listener for all the buttons on the gui, and two mouse listeners for the JLists for messages and users. The action listener runs neccessary logic when a user clicks a button like send button. The mouse listeners allow the user to double click on a user to select their messages with them or double click on a message to edit, delete, or view the image in a message.

the profile gui displays your username at the top, profile picture in the middle using a JLabel and at the bottom there are four buttons that use a single action listener to allow all profile adjustments. These buttons allow you to change password, change username, upload a new profile picture, or toggle between allowing all users to message you and only allowing friends to message you. This GUI runs on the same thread as the main gui, so users should only use one of these windows at a time. 

All interaction with the server through GUIClient is done through a UserClient object, for information how how UserClient.java works and how it interacts with the server, view [Client.md](Client.md) and [Server.md](Server.md).