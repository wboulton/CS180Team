import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class signInGUI extends JComponent implements Runnable, signInGUIInterface {

    signInGUI signInPage;
    JTextField username;
    JTextField password;
    JTextField firstName;
    JTextField lastName;
    JFileChooser profilePicture;
    JButton signIn;
    JButton signUp;
    JButton profilePictureButton;
    JButton changeToButton;
    UserClient client = null;
    String filePath = null;
    JFrame frame;
    Container pane;

    private boolean signInFlag = true;

    public static void main(String[] Args) {
        SwingUtilities.invokeLater(new signInGUI());
    }

    public signInGUI() {

    }

    public void startMainGUI(String username) {

        ArrayList<String> usernames = client.getUserList();
        GUIClient newUserClient = new GUIClient(usernames, client, username);
        SwingUtilities.invokeLater(newUserClient);
        frame.setVisible(false);
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String usernameText = username.getText();
            String passwordText = password.getText();
            String firstNameText = firstName.getText();
            String lastNameText = lastName.getText();

            // if (usernameText.equals("Enter username..."))
            // usernameText = "";

            // if (passwordText.equals("Enter password..."))
            // passwordText = "";

            // if (firstNameText.equals("Enter first name..."))
            // firstNameText = "";

            // if (lastNameText.equals("Enter last name..."))
            // lastNameText = "";

            if (e.getSource() == signIn) {
                try {
                    client = new UserClient(usernameText, passwordText);

                    startMainGUI(usernameText);
                    System.out.println("sign in ok");
                } catch (Exception ee) {
                    ee.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Invalid Login Credentials", "login error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (e.getSource() == signUp) {
                try {
                    client = new UserClient(usernameText, passwordText, firstNameText, lastNameText, filePath);
                    System.out.println("user created ok");

                    startMainGUI(usernameText);
                } catch (Exception ee) {
                    ee.printStackTrace();
                    JOptionPane.showMessageDialog(null, ee.getMessage(), "sign up error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } else if (e.getSource() == profilePictureButton) {
                //image only
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif",
                        "bmp");
                profilePicture.setFileFilter(filter);
                
                int result = profilePicture.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = profilePicture.getSelectedFile();
                    filePath = selectedFile.getAbsolutePath();
                    // change to byte stream
                    profilePictureButton.setText(filePath);
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                } else {
                    System.out.println("File selection cancelled.");
                }
            } else if (e.getSource() == changeToButton) {
                if (signInFlag) {
                    signInFlag = false;
                    pane.removeAll();
                    pane.revalidate();
                    pane.repaint();
                    JPanel panel = createMainPanel();
                    changeToButton.setText("Already have an account? Sign In");
                    pane.add(panel);
                    frame.setSize(600, 1000);
                } else {
                    signInFlag = true;
                    pane.removeAll();
                    pane.revalidate();
                    pane.repaint();
                    JPanel panel = createMainPanel();
                    changeToButton.setText("Don't have an account? Sign Up");
                    pane.add(panel);
                    frame.setSize(600, 660);
                }
            }
        }
    };

    void setPlaceHolder(JTextField textField, String placeholder) {
        textField.setForeground(Color.gray);
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText(""); // Clear placeholder
                    textField.setForeground(Color.BLACK); // Set normal text color
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder); // Reset placeholder
                    textField.setForeground(Color.GRAY); // Set placeholder text color
                }
            }
        });
    }

    public JPanel createMainPanel() {
        JLabel title = new JLabel();
        if (signInFlag)
            title.setText("Sign In");
        else
            title.setText("Sign Up");
        title.setFont(new Font("Arial", Font.BOLD, 40));

        JLabel subTitle = new JLabel("Login to start chatting with friends!");
        if (!signInFlag)
            subTitle.setText("Create an account to start chatting with friends!");

        subTitle.setFont(new Font("Arial", Font.PLAIN, 24));

        // headings
        JLabel usernameHeading = new JLabel("Username");
        usernameHeading.setFont(new Font("Arial", Font.PLAIN, 24));

        JLabel passwordHeading = new JLabel("Password");
        passwordHeading.setFont(new Font("Arial", Font.PLAIN, 24));

        JLabel firstNameHeading = new JLabel("First Name");
        firstNameHeading.setFont(new Font("Arial", Font.PLAIN, 24));

        JLabel lastNameHeading = new JLabel("Last Name");
        lastNameHeading.setFont(new Font("Arial", Font.PLAIN, 24));

        JLabel profilePictureHeading = new JLabel("Profile Picture");
        profilePictureHeading.setFont(new Font("Arial", Font.PLAIN, 24));

        // text fields
        username = new JTextField("Enter username...", 10);
        setPlaceHolder(username, "Enter username...");
        username.setMaximumSize(new Dimension(2000, 80));
        username.setFont(new Font("Arial", Font.BOLD, 18));

        password = new JTextField("Enter password...", 10);
        setPlaceHolder(password, "Enter password...");
        password.setMaximumSize(new Dimension(2000, 80));
        password.setFont(new Font("Arial", Font.BOLD, 18));

        firstName = new JTextField("Enter first name...", 10);
        setPlaceHolder(firstName, "Enter first name...");
        firstName.setMaximumSize(new Dimension(2000, 80));
        firstName.setFont(new Font("Arial", Font.BOLD, 18));

        lastName = new JTextField("Enter last name...", 10);
        setPlaceHolder(lastName, "Enter last name...");
        lastName.setMaximumSize(new Dimension(2000, 80));
        lastName.setFont(new Font("Arial", Font.BOLD, 18));

        profilePicture = new JFileChooser();

        // buttons
        signIn = new JButton("Sign In");
        signIn.addActionListener(actionListener);
        signUp = new JButton("Sign Up");
        signUp.addActionListener(actionListener);
        changeToButton = new JButton("Don't have an account? Sign Up");
        profilePictureButton = new JButton("Select Profile Picture");
        profilePictureButton.addActionListener(actionListener);
        profilePictureButton.setFont(new Font("Arial", Font.BOLD, 18));
        changeToButton.addActionListener(actionListener);

        signIn.setAlignmentY(Component.CENTER_ALIGNMENT);
        signUp.setAlignmentY(Component.CENTER_ALIGNMENT);
        changeToButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        signIn.setMaximumSize(new Dimension(560, 70));
        signIn.setFont(new Font("Arial", Font.BOLD, 20));
        signUp.setMaximumSize(new Dimension(560, 70));
        signUp.setFont(new Font("Arial", Font.BOLD, 20));
        changeToButton.setMaximumSize(new Dimension(560, 70));
        changeToButton.setFont(new Font("Arial", Font.BOLD, 20));
        signIn.setBackground(new Color(0, 92, 230));
        signIn.setForeground(Color.white);
        changeToButton.setBackground(Color.white);
        changeToButton.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        signUp.setForeground(Color.white);
        signUp.setBackground(new Color(0, 92, 230));

        // main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        panel.add(title);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subTitle);
        panel.add(Box.createVerticalStrut(40));

        // sign up panel

        JPanel signUpPanel = new JPanel();
        signUpPanel.setLayout(new BoxLayout(signUpPanel, BoxLayout.Y_AXIS));
        signUpPanel.setBackground(Color.WHITE);

        signUpPanel.add(firstNameHeading);
        signUpPanel.add(firstName);
        signUpPanel.add(Box.createVerticalStrut(20));
        signUpPanel.add(lastNameHeading);
        signUpPanel.add(lastName);
        signUpPanel.add(Box.createVerticalStrut(20));
        signUpPanel.add(profilePictureHeading);
        signUpPanel.add(profilePictureButton);
        signUpPanel.add(Box.createVerticalStrut(20));

        if (!signInFlag)
            panel.add(signUpPanel);

        // shared panel

        panel.add(usernameHeading);
        panel.add(username);
        panel.add(Box.createVerticalStrut(20));
        panel.add(passwordHeading);
        panel.add(password);
        panel.add(Box.createVerticalStrut(30));

        // sign in/up buttons panel
        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.WHITE);
        panel2.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));

        // panel2.add(Box.createRigidArea(new Dimension(60, 0)));
        if (signInFlag)
            panel2.add(signIn);
        else
            panel2.add(signUp);
        panel2.add(changeToButton);
        // panel2.add(Box.createRigidArea(new Dimension(10, 0)));
        // panel2.add(signUp);

        panel.add(panel2);
        return panel;
    }

    public void run() {
        frame = new JFrame("CS180 Messenger");
        pane = frame.getContentPane();

        JPanel panel = createMainPanel();

        pane.add(panel);

        frame.setSize(600, 660);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
