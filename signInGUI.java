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
    float widthMultiplier;
    float heightMultiplier;

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
                // image only
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
                    frame.setSize((int) (300 * widthMultiplier), (int) (500 * heightMultiplier));
                } else {
                    signInFlag = true;
                    pane.removeAll();
                    pane.revalidate();
                    pane.repaint();
                    JPanel panel = createMainPanel();
                    changeToButton.setText("Don't have an account? Sign Up");
                    pane.add(panel);
                    frame.setSize((int) (300 * widthMultiplier), (int) (330 * heightMultiplier));
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
        title.setFont(new Font("Arial", Font.BOLD, (int) (20 * heightMultiplier)));

        JLabel subTitle = new JLabel("Login to start chatting with friends!");
        if (!signInFlag)
            subTitle.setText("Create an account to start chatting with friends!");

        subTitle.setFont(new Font("Arial", Font.PLAIN, (int) (12 * heightMultiplier)));

        // headings
        JLabel usernameHeading = new JLabel("Username");
        usernameHeading.setFont(new Font("Arial", Font.PLAIN, (int) (12 * heightMultiplier)));

        JLabel passwordHeading = new JLabel("Password");
        passwordHeading.setFont(new Font("Arial", Font.PLAIN, (int) (12 * heightMultiplier)));

        JLabel firstNameHeading = new JLabel("First Name");
        firstNameHeading.setFont(new Font("Arial", Font.PLAIN, (int) (12 * heightMultiplier)));

        JLabel lastNameHeading = new JLabel("Last Name");
        lastNameHeading.setFont(new Font("Arial", Font.PLAIN, (int) (12 * heightMultiplier)));

        JLabel profilePictureHeading = new JLabel("Profile Picture");
        profilePictureHeading.setFont(new Font("Arial", Font.PLAIN, (int) (12 * heightMultiplier)));

        // text fields
        username = new JTextField("Enter username...", 10);
        setPlaceHolder(username, "Enter username...");
        username.setMaximumSize(new Dimension((int) (1000 * widthMultiplier), (int) (40 * heightMultiplier)));
        username.setFont(new Font("Arial", Font.PLAIN, (int) (10 * heightMultiplier)));

        password = new JTextField("Enter password...", 10);
        setPlaceHolder(password, "Enter password...");
        password.setMaximumSize(new Dimension((int) (1000 * widthMultiplier), (int) (40 * heightMultiplier)));
        password.setFont(new Font("Arial", Font.PLAIN, (int) (10 * heightMultiplier)));

        firstName = new JTextField("Enter first name...", 10);
        setPlaceHolder(firstName, "Enter first name...");
        firstName.setMaximumSize(new Dimension((int) (1000 * widthMultiplier), (int) (40 * heightMultiplier)));
        firstName.setFont(new Font("Arial", Font.PLAIN, (int) (10 * heightMultiplier)));

        lastName = new JTextField("Enter last name...", 10);
        setPlaceHolder(lastName, "Enter last name...");
        lastName.setMaximumSize(new Dimension((int) (1000 * widthMultiplier), (int) (40 * heightMultiplier)));
        lastName.setFont(new Font("Arial", Font.PLAIN, (int) (10 * heightMultiplier)));

        profilePicture = new JFileChooser();

        // buttons
        signIn = new JButton("Sign In");
        signIn.addActionListener(actionListener);
        signUp = new JButton("Sign Up");
        signUp.addActionListener(actionListener);
        changeToButton = new JButton("Don't have an account? Sign Up");
        profilePictureButton = new JButton("Select Profile Picture");
        profilePictureButton.addActionListener(actionListener);
        profilePictureButton.setFont(new Font("Arial", Font.PLAIN, (int) (10 * heightMultiplier)));
        changeToButton.addActionListener(actionListener);

        signIn.setAlignmentY(Component.CENTER_ALIGNMENT);
        signUp.setAlignmentY(Component.CENTER_ALIGNMENT);
        changeToButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        signIn.setMaximumSize(new Dimension((int) (280 * widthMultiplier), (int) (35 * heightMultiplier)));
        signIn.setFont(new Font("Arial", Font.BOLD, (int) (12 * heightMultiplier)));
        signUp.setMaximumSize(new Dimension((int) (280 * widthMultiplier), (int) (35 * heightMultiplier)));
        signUp.setFont(new Font("Arial", Font.BOLD, (int) (12 * heightMultiplier)));
        changeToButton.setMaximumSize(new Dimension((int) (280 * widthMultiplier), (int) (35 * heightMultiplier)));
        changeToButton.setFont(new Font("Arial", Font.BOLD, (int) (12 * heightMultiplier)));
        signIn.setBackground(new Color(0, 92, 230));
        signIn.setForeground(Color.white);
        changeToButton.setBackground(Color.white);
        changeToButton.setBorder(BorderFactory.createEmptyBorder((int) (10 * heightMultiplier),
                (int) (10 * widthMultiplier), (int) (10 * heightMultiplier), (int) (10 * widthMultiplier)));
        signUp.setForeground(Color.white);
        signUp.setBackground(new Color(0, 92, 230));

        // main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder((int) (10 * heightMultiplier), (int) (10 * widthMultiplier),
                (int) (10 * heightMultiplier), (int) (10 * widthMultiplier)));

        panel.add(title);
        panel.add(Box.createVerticalStrut((int) (5 * heightMultiplier)));
        panel.add(subTitle);
        panel.add(Box.createVerticalStrut((int) (20 * heightMultiplier)));

        // sign up panel

        JPanel signUpPanel = new JPanel();
        signUpPanel.setLayout(new BoxLayout(signUpPanel, BoxLayout.Y_AXIS));
        signUpPanel.setBackground(Color.WHITE);

        signUpPanel.add(firstNameHeading);
        signUpPanel.add(firstName);
        signUpPanel.add(Box.createVerticalStrut((int) (10 * heightMultiplier)));
        signUpPanel.add(lastNameHeading);
        signUpPanel.add(lastName);
        signUpPanel.add(Box.createVerticalStrut((int) (10 * heightMultiplier)));
        signUpPanel.add(profilePictureHeading);
        signUpPanel.add(profilePictureButton);
        signUpPanel.add(Box.createVerticalStrut((int) (10 * heightMultiplier)));

        if (!signInFlag)
            panel.add(signUpPanel);

        // shared panel

        panel.add(usernameHeading);
        panel.add(username);
        panel.add(Box.createVerticalStrut((int) (10 * heightMultiplier)));
        panel.add(passwordHeading);
        panel.add(password);
        panel.add(Box.createVerticalStrut((int) (15 * heightMultiplier)));

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

        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() * 0.8);
        int height = (int) (screenSize.getHeight() * 0.8);
        widthMultiplier = width / 1024; // built in 1024px
        heightMultiplier = height / 576; // built in 576px
        
        JPanel panel = createMainPanel();

        pane.add(panel);


        frame.setSize((int) (300 * widthMultiplier), (int) (330 * heightMultiplier));

        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
