# SignInGUI.java
The SignInGUI.java file takes one command line argument to pass an integer port number to the main method. This then immediately creates the GUI on a SwingUtilities.invokeLater thread. This GUI takes in user inputs from the following text fields and file chooser:
```java
    JTextField username;
    JTextField password;
    JTextField firstName;
    JTextField lastName;
    JFileChooser profilePicture;
```
