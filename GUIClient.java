import javax.swing.*;
import javax.swing.text.StyleConstants;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

import java.awt.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.atomic.*;

public class GUIClient implements Runnable {
    ArrayList<String> usernames;
    ArrayList<String> displayUsers;
    ArrayList<String> messages;

    UserClient client;

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

    String viewingUsername;
    String clientUsername;

    private java.util.Timer messageUpdateTimer;

    public GUIClient(ArrayList<String> usernames, UserClient client, String username) {
        this.usernames = usernames;
        viewingUsername = "";
        this.client = client;
        this.clientUsername = username;
        this.messages = new ArrayList<>();
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
        userList.setListData(displayUsers.toArray(new String[0]));
    }

    private void sendMessage(String message) {
        if (message != null && !message.trim().isEmpty()) {
            try {
                String sending = viewingUser.getText().replace("Currently viewing: ", "");
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

    public void run() {
        this.messages = new ArrayList<String>();

        startMessageUpdater();

        JFrame frame = new JFrame("Social Media App(tm)");
        Container content = frame.getContentPane();
        JPanel panel = new JPanel();
        content.add(panel, BorderLayout.CENTER);
    
        friendButton = new JButton("friend/unfriend");
        blockButton = new JButton("block/unblock");
        sendButton = new JButton("<html>send<br>message</html>");
        sendButton.setPreferredSize(new Dimension(100, 100));
        searchButton = new JButton("search");
        userImage = new JLabel("User Image Placeholder"); 
        messageField = new JTextPane();
        messageField.setPreferredSize(new Dimension(1700, 100));
        messageField.setFont(new Font("Arial", Font.PLAIN, 16));
        messageField.setEditable(true); 
        messageField.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Enable wrapping with styling
        StyledDocument doc = messageField.getStyledDocument();
        Style style = messageField.addStyle("WrapStyle", null);
        StyleConstants.setLineSpacing(style, 0.2f); 
        messageField.setParagraphAttributes(style, true);
        userList = new JList<>(usernames.toArray(new String[0]));
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(100,25));
        viewingUser = new JTextArea(String.format("Currently viewing %s", viewingUsername));
        viewingUser.setEditable(false); // Make the text area non-editable
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

        placeHolder(searchField, "user");
        
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == friendButton) {
                    try {
                        client.addOrRemoveFriend(viewingUsername);
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
                    searchForUser(username);
                } else if (e.getSource() == sendButton) {
                    String message = messageField.getText();
                    sendMessage(message);
                }
            }
        };
    userList.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            if (evt.getClickCount() == 2) {
                int index = userList.locationToIndex(evt.getPoint());
                if (index >= 0) {
                    viewingUsername = userList.getModel().getElementAt(index);
                    viewingUser.setText(String.format("Currently viewing: %s", viewingUsername));
                    try {
                        messages = client.getConversation(viewingUsername);
                        messageJList.setListData(getMessages().toArray(new String[0])); 
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    });

    messageJList.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
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
    
        JPanel topPanel = new JPanel();
        topPanel.add(viewingUser);
        topPanel.add(friendButton);
        topPanel.add(blockButton);
        topPanel.add(userImage);
        
    
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(messageField);
        bottomPanel.add(sendButton);
    
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        rightPanel.add(searchPanel);
        rightPanel.add(userList);

        JPanel messagePanel = new JPanel();
        messagePanel.add(messageJList);
    
        content.add(topPanel, BorderLayout.NORTH);
        content.add(bottomPanel, BorderLayout.SOUTH);
        content.add(rightPanel, BorderLayout.EAST);
        content.add(messagePanel, BorderLayout.WEST);
    
        frame.setSize(1900, 1000);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
//this will run a timertask that will update the gui with new messages every three seconds
//this also makes the other timertask in the media server uncessary now because it calls directly
//upon the getConversation method. This also ensures messages are displayed in the same order universally
    public void startMessageUpdater() {
        //for some reason swing also has a timer class but I am using the java.util one
        messageUpdateTimer = new java.util.Timer();
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
        UserClient client = null;
        Scanner sc = new Scanner(System.in);
        String username = sc.nextLine();
        String password = sc.nextLine();
        try {
            client = new UserClient(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        ArrayList<String> usernames = client.getUserList();
        GUIClient newUserClient = new GUIClient(usernames, client, username);
        SwingUtilities.invokeLater(newUserClient);
    }
}
