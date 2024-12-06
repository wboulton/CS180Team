import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.StyleConstants;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import java.io.File;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.net.*;
import java.util.Timer;
import java.util.concurrent.atomic.*;

public class GUIClient implements Runnable, GUIInterface {
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

    public GUIClient(ArrayList<String> usernames, UserClient client, String username) {
        this.usernames = usernames;
        viewingUsername = "";
        this.client = client;
        this.clientUsername = username;
        this.messages = new ArrayList<>();
        final UserClient shutdownClient = client;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdownClient.kill();
        }));
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
        messageJList.setListData(this.messages.toArray(new String[0]));
    }

    private ArrayList<String> getMessages() {
        ArrayList<String> content = new ArrayList<String>();
        try {
            messages = client.getConversation(viewingUsername);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //this is necessary to ensure that if you have no messages with a user there is no error
        if (messages == null) {
            return content;
        }
        for (String message : messages) {
            String[] info = message.split("\\|");
            content.add(String.format("%s: %s", info[1], info[3]));
        }
        return content;
    }

    private String getSender(String sender) {
        return sender.split(":")[0];
    }

    //action listener methods
    private void searchForUser(String username) {
        displayUsers = new ArrayList<String>();
        for (String user : usernames) {
            if (user.toLowerCase().contains(username.toLowerCase())) {
                displayUsers.add(user);
            }
        }
        //if you have blocked a user, the only way to see them is to search their exact username
        if (displayUsers.size() < 1) {
            if (client.search(username) != null) {
                displayUsers.add(client.search(username));
            } else {
                displayUsers.add("No Applicable Users");
            }
        }
        userList.setListData(displayUsers.toArray(new String[0]));
    }

    private void sendMessage(String message, String picture) {
        if (message.contains("|")) {
            JOptionPane.showMessageDialog(null, "'|' character not allowed", "Social Media App(tm)",
                        JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (message != null && !message.trim().isEmpty()) {
            try {
                String sending = viewingUser.getText().replace("Currently viewing: ", "");
                sending = sending.replace(" FRIEND", "");
                String toSend = client.sendMessage(sending, message, null);
                if (toSend.equals("String too long")) {
                    JOptionPane.showMessageDialog(null, "The message was too long", "Social Media App(tm)",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            messageField.setText("");
            messageJList.setListData(getMessages().toArray(new String[0]));
        }
    }

    private void placeHolder(JTextField textField, String placeholderText) {
        textField.setText(placeholderText);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholderText)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholderText);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void editProfile() {
//edit your profile using the Jbutton
        JFrame frame = new JFrame("Social Media App(tm)");
        Container content = frame.getContentPane();
        JPanel panel = new JPanel();
        content.add(panel, BorderLayout.CENTER);
        JButton editUsername = new JButton("Change Username");
        JButton editPassword = new JButton("Change Password");
        JButton editProfilePicture = new JButton("Upload Profile Picture");
        JLabel profilePicture;
        JButton allowAll = new JButton();
        boolean allowed = true;
        try {
            allowed = client.isAllowAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (allowed) {
            allowAll.setText("All users can message");
        } else {
            allowAll.setText("only friends can message");
        }

        JTextArea usernameArea = new JTextArea(clientUsername);
        usernameArea.setWrapStyleWord(true);
       
        BufferedImage pfp = client.getProfilePicture();
        if (pfp != null) {
            ImageIcon icon = new ImageIcon(pfp);
            profilePicture = new JLabel(icon);
        } else {
            profilePicture = new JLabel("No Profile Picture");
        }
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == editUsername) {
                    String newUsername;
                    Boolean success = true;
                        do {
                            newUsername = JOptionPane.showInputDialog("What would you like to change to?",
                                clientUsername);
                            if (newUsername == null) {
                                break;
                            }
                            try {
                                success = client.setUserName(newUsername);
                                if (!success) {
                                    JOptionPane.showMessageDialog(null, "Cannot use this username",
                                        "Social Media App(tm)", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    clientUsername = newUsername;
                                    usernameArea.setText(newUsername);
                                }
                            } catch (Exception error) {
                                error.printStackTrace();
                            }
                        } while (!success);
                } else if (e.getSource() == editPassword) {
                    String newPassword;
                    Boolean success = true;
                        do {
                            newPassword = JOptionPane.showInputDialog("What would you like to change to?","");
                            if (newPassword == null) {
                                break; // Exit the loop if the user clicks the X or Cancel button
                            }
                            try {
                                success = client.setPassword(newPassword);
                                if (!success) {
                                    JOptionPane.showMessageDialog(null, 
                                        "Illegal Password, Legal passwords must be 8 characters" + 
                                        " long with a capital letter and a special character",
                                        "Social Media App(tm)", JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (Exception error) {
                                error.printStackTrace();
                            }
                        } while (!success);
                } else if (e.getSource() == editProfilePicture) {
                    //TODO Mukund, add profile picture changing here
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg",
                        "jpeg", "png", "gif", "bmp", "webp");
                    profilePictureChooser.setFileFilter(filter);
                    int result = profilePictureChooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File file = profilePictureChooser.getSelectedFile();
                        try {
                            client.changeProfilePicture(Files.readAllBytes(file.toPath()));
                            BufferedImage pfp = client.getProfilePicture();
                            if (pfp != null) {
                                ImageIcon icon = new ImageIcon(pfp);
                                profilePicture.setIcon(icon);
                            } else {
                                profilePicture.setIcon(null);;
                            }
                        } catch (Exception error) {
                            error.printStackTrace();
                        }
                    }
                } else if (e.getSource() == allowAll) {
                    try {
                        boolean allowed2 = client.changeAllowAll();
                        if (allowed2) {
                            allowAll.setText("All users can message");
                        } else {
                            allowAll.setText("only friends can message");
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        };

        editPassword.addActionListener(listener);
        editUsername.addActionListener(listener);
        editProfilePicture.addActionListener(listener);
        allowAll.addActionListener(listener);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(editPassword);
        bottomPanel.add(editUsername);
        bottomPanel.add(editProfilePicture);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JScrollPane(usernameArea), BorderLayout.CENTER);
        
        JPanel centerPanel = new JPanel();
        centerPanel.add(profilePicture);

        content.add(topPanel, BorderLayout.NORTH);
        content.add(bottomPanel, BorderLayout.SOUTH);
        content.add(centerPanel, BorderLayout.CENTER);

        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void run() {
        this.messages = new ArrayList<String>();

        startMessageUpdater();

        JFrame frame = new JFrame("Social Media App(tm)");
        Container content = frame.getContentPane();
        frame.setSize(1900, 1000);
        content.setLayout(new BorderLayout());

        profileButton = new JButton("edit profile");
        friendButton = new JButton("friend/unfriend");
        blockButton = new JButton("block/unblock");
        sendButton = new JButton("<html>send<br>message</html>");
        sendButton.setPreferredSize(new Dimension(100, 100));
        searchButton = new JButton("search");
        profilePictureChooser = new JFileChooser();
        profilePictureChooser.setDialogTitle("Choose a profile picture");
        addPicToMessage = new JButton("Add Picture to Message");
        //get the viewing user's profile picture
        userImage = new JLabel("No Profile Picture");
    
        userImage.setHorizontalAlignment(SwingConstants.CENTER);
        userImage.setVerticalAlignment(SwingConstants.CENTER); 

        messageField = new JTextPane();
        messageField.setPreferredSize(new Dimension(1700, 100));
        messageField.setFont(new Font("Arial", Font.PLAIN, 16));
        messageField.setEditable(true); 
        messageField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        Style style = messageField.addStyle("WrapStyle", null);
        StyleConstants.setLineSpacing(style, 0.2f); 
        messageField.setParagraphAttributes(style, true);
        userList = new JList<>(usernames.toArray(new String[0]));
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(100,25));
        viewingUser = new JTextArea(String.format("Currently viewing %s", viewingUsername));
        viewingUser.setEditable(false); 
        messageJList = new JList<String>();
        messageJList.setPreferredSize(new Dimension(1600, 800));
                //this handles alignment by sender The logged in user appears on the left and the 
                //user they are viewing appears on the right
        messageJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                String message = value.toString();
                String sender = getSender(message);
                if (sender.equals(GUIClient.this.clientUsername)) { 
                    label.setHorizontalAlignment(SwingConstants.LEFT);
                } else {
                    label.setHorizontalAlignment(SwingConstants.RIGHT);
                }
                return label;
            }
        });

        placeHolder(searchField, "username");
        
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File sendPic = null;
                if (e.getSource() == friendButton) {
                    String fieldString = viewingUser.getText();
                    try {
                        client.addOrRemoveFriend(viewingUsername);
                        try {
                            fieldString = fieldString.replace(" FRIEND", "");
                            if (client.isFriend(viewingUsername)) {
                                fieldString += " FRIEND";
                            }
                            viewingUser.setText(fieldString);
                        } catch (Exception error) {
                            error.printStackTrace();
                        }
                    } catch (Exception error) {
                        error.printStackTrace();
                    }
                } else if (e.getSource() == blockButton) {
                    try {
                        client.blockOrUnblock(viewingUsername);
                    } catch (Exception error) {
                        error.printStackTrace();
                    }
                } else if (e.getSource() == searchButton) {
                    String username = searchField.getText();
                    if (username.equals("username")) {
                        username = "";
                    }
                    searchForUser(username);
                } else if (e.getSource() == sendButton) {
                    byte[] picBytes = null;
                    if (sendPic != null) {
                        try {
                            picBytes = Files.readAllBytes(sendPic.toPath());
                        } catch (Exception error) {
                            error.printStackTrace();
                        }
                    }
                    String message = messageField.getText();
                    sendMessage(message, byteArraytoString(picBytes));
                } else if (e.getSource() == profileButton) {
                    editProfile();
                } else if (e.getSource() == addPicToMessage) {
                    //TODO Mukund, add picture to message here
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Choose a picture to send");
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg",
                        "jpeg", "png", "gif", "bmp");
                    fileChooser.setFileFilter(filter);
                    int result = fileChooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        sendPic = fileChooser.getSelectedFile();
                    }
                }
            }
        };
    userList.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent evt) {
            if (evt.getClickCount() == 2) {
                int index = userList.locationToIndex(evt.getPoint());
                if (index >= 0) {
                    viewingUsername = userList.getModel().getElementAt(index);
                    String fieldString = String.format("Currently viewing: %s", viewingUsername);
                    try {
                        if (client.isFriend(viewingUsername)) {
                            fieldString += " FRIEND";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    viewingUser.setText(fieldString);
                    try {
                        messages = client.getConversation(viewingUsername);
                        messageJList.setListData(getMessages().toArray(new String[0])); 
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    byte[] viewingProfilePicture;
                    try {
                        viewingProfilePicture = client.getViewingProfilePicture(viewingUsername);
                    } catch (Exception e) {
                        e.printStackTrace();
                        viewingProfilePicture = null;
                    }
                    
                    if (viewingProfilePicture != null) {
                        ImageIcon icon = new ImageIcon(viewingProfilePicture);
                        Image image = icon.getImage();
                        Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        userImage.setIcon(new ImageIcon(scaledImage));
                        userImage.setText("");
                    } else {
                        userImage.setIcon(null);;
                    }
                }
            }
        }
    });

    messageJList.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent evt) {
            if (evt.getClickCount() == 2) {
                int index = messageJList.locationToIndex(evt.getPoint());
                if (index >= 0) {
                    try {
                        String viewingMessage = client.getConversation(viewingUsername).get(index);
                        int messageAlter = JOptionPane.showOptionDialog(null, viewingMessage.split("\\|")[3],
                            "Social Media App(tm)", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                            new String[]{"Edit", "Delete", "Cancel"}, null);
                        if (messageAlter == 0) {
                            //edit
                            String edit;
                            do {
                                String newText = JOptionPane.showInputDialog("What would you like to change it to?",
                                    viewingMessage.split("\\|")[3]);
                                if (newText != null) {
                                    edit = client.editMessage(Integer.parseInt(viewingMessage.split("\\|")[0]), newText);
                                    if (edit.equals("String too long")) {
                                        JOptionPane.showMessageDialog(null, "The message was too long",
                                            "Social Media App(tm)", JOptionPane.ERROR_MESSAGE);
                                    }
                                    messageJList.setListData(getMessages().toArray(new String[0]));
                                } else {
                                    edit = "";
                                }
                            } while (edit.equals("String too long"));
                        } else if (messageAlter == 1) {
                            //delete
                            client.deleteMessage(Integer.parseInt(viewingMessage.split("\\|")[0]));
                            messageJList.setListData(getMessages().toArray(new String[0]));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    });
    
        friendButton.addActionListener(actionListener);
        blockButton.addActionListener(actionListener);
        searchButton.addActionListener(actionListener);
        sendButton.addActionListener(actionListener);
        profileButton.addActionListener(actionListener);
    
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(profileButton);
        topPanel.add(viewingUser);
        topPanel.add(friendButton);
        topPanel.add(blockButton);
        topPanel.add(userImage);
        
    
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(new JScrollPane(messageField), BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
    
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height));
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(userList), BorderLayout.CENTER);

        //TODO make width resizing work
        JPanel messagePanel = new JPanel(new BorderLayout());
        JScrollPane messageScrollPane = new JScrollPane(messageJList);
        //this will make messages include a scroll bar if it resizes so that messages are not viewable
        messageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        messageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            messageScrollPane.setPreferredSize(new Dimension(frame.getWidth() - 260, frame.getHeight() - 200));
            messageScrollPane.revalidate();
            }
        });
        messageScrollPane.setMinimumSize(new Dimension(500, 200)); 
        messagePanel.add(messageScrollPane, BorderLayout.CENTER);
    
        content.add(topPanel, BorderLayout.NORTH);
        content.add(bottomPanel, BorderLayout.SOUTH);
        content.add(rightPanel, BorderLayout.EAST);
        content.add(messagePanel, BorderLayout.WEST);
    
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private String byteArraytoString(byte[] picBytes) {
        if (picBytes == null) {
            return null;
        }
        StringBuilder picString = new StringBuilder();
        for (byte b : picBytes) {
            picString.append(b);
            picString.append(",");
        }
        return picString.toString();
    }

    //this will run a timertask that will update the gui with new messages every three seconds
//this also makes the other timertask in the media server uncessary now because it calls directly
//upon the getConversation method. This also ensures messages are displayed in the same order universally
    public void startMessageUpdater() {
        //for some reason swing also has a timer class but I am using the java.util one
        messageUpdateTimer = new Timer();
        messageUpdateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //ensure that we are actually viewing someone
                if (viewingUsername != null && !viewingUsername.isEmpty()) {
                    try {
                        ArrayList<String> updatedMessages = client.getConversation(viewingUsername);
                        if (updatedMessages != null && !updatedMessages.equals(messages)) {
                            messages = updatedMessages; // update the messages array
                            messageJList.setListData(getMessages().toArray(new String[0]));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 3000); // Runs every 3 seconds
    }

    public static void main(String[] args) {
//obsolete main method for testing
        String username;
        String password;
        UserClient client = null;
        Scanner sc = new Scanner(System.in);
        while (true) {
            username = sc.nextLine();
            password = sc.nextLine();
            try {
                client = new UserClient(username, password);
                sc.close();
                break;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Invalid Login Credentials", "Social Media App(tm)",
                    JOptionPane.ERROR_MESSAGE);
                continue;
            }
        }
        
        ArrayList<String> usernames = client.getUserList();
        GUIClient newUserClient = new GUIClient(usernames, client, username);
        SwingUtilities.invokeLater(newUserClient);

        final UserClient shutdownClient = client;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdownClient.kill();
        }));
    }
}
