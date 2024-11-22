import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.*;
import java.util.*;

public class GUIClient implements Runnable {
    ArrayList<String> usernames;
    ArrayList<String> displayUsers;
    ArrayList<String> messages = new ArrayList<>(Arrays.asList("message1", "message2", 
    "message3", "message4", "message5"));;

    JButton friendButton;
    JButton blockButton;
    JLabel userImage;
    JButton sendButton;
    JTextField messageField;
    JList<String> userList;
    JButton searchButton;
    JTextField searchField;
    JTextArea viewingUser;
    JList<String> messageJList;

    String viewingUsername;
    String clientUsername;

    public GUIClient(ArrayList<String> usernames) {
        this.usernames = usernames;
        viewingUsername = "";
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
        messageJList.setListData(this.messages.toArray(new String[0]));
    }

    private void searchForUser(String username) {
        displayUsers = new ArrayList<String>();
        for (String user : usernames) {
            if (user.toLowerCase().contains(username.toLowerCase())) {
                displayUsers.add(user);
            }
        }
        userList.setListData(displayUsers.toArray(new String[0]));
    }

    public void run() {
        JFrame frame = new JFrame("Social Media App(tm)");
        Container content = frame.getContentPane();
        JPanel panel = new JPanel();
        content.add(panel, BorderLayout.CENTER);
    
        friendButton = new JButton("friend/unfriend");
        blockButton = new JButton("block/unblock");
        sendButton = new JButton("send");
        searchButton = new JButton("search");
        userImage = new JLabel("User Image Placeholder"); 
        messageField = new JTextField("message");
        userList = new JList<String>(usernames.toArray(new String[0]));
        searchField = new JTextField("user");
        viewingUser = new JTextArea(String.format("Currently viewing %s", viewingUsername));
        messageJList = new JList<String>(messages.toArray(new String[0]));
    
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == friendButton) {
                    System.out.println("Friend button clicked");
                } else if (e.getSource() == blockButton) {
                    System.out.println("Block button clicked");
                } else if (e.getSource() == searchButton) {
                    String username = searchField.getText();
                    searchForUser(username);
                } else if (e.getSource() == sendButton) {
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
        ArrayList<String> usernames = new ArrayList<>(Arrays.asList("fake username",
            "antoher username", "I made a typo in the last one", "one more here"));
        SwingUtilities.invokeLater(new GUIClient(usernames));
    }
}