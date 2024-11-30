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

    public GUIClient(ArrayList<String> usernames, UserClient client, String username) {
        this.usernames = usernames;
        viewingUsername = "";
        this.client = client;
        this.clientUsername = username;
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
                    System.out.println("Block button clicked");
                } else if (e.getSource() == searchButton) {
                    String username = searchField.getText();
                    searchForUser(username);
                } else if (e.getSource() == sendButton) {
                    String message = messageField.getText();
                    sendMessage(message);
                    System.out.println("Send button clicked");
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
    

    public static void main(String[] args) {
        UserClient client = null;
        String username = "anotherguh";
        String password = "Password1";
        try {
            client = new UserClient(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        ArrayList<String> usernames = client.getUserList();
        SwingUtilities.invokeLater(new GUIClient(usernames, client, username));
    }
}