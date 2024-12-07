import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Team Project -- UserClient
 *
 * This file contains the interface for the sign in GUI
 *
 * @author Alan Yi
 *
 * @version December 7, 2024
 */
public interface signInGUIInterface {
    SignInGUI signInPage = null;
    JTextField username = null;
    JTextField password = null;
    JTextField firstName = null;
    JTextField lastName = null;
    JFileChooser profilePicture = null;
    JButton signIn = null;
    JButton signUp = null;
    JButton profilePictureButton = null;
    JButton changeToButton = null;
    UserClient client = null;
    String filePath = null;
    JFrame frame = null;

    void startMainGUI(String username);
    JPanel createMainPanel();
    void run();
}
