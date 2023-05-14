package edu.groupeighteen.librarydbms.view.GUI;

import edu.groupeighteen.librarydbms.LibraryManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-04-21
 */
public class LoginScreenGUI extends JFrame {
    public JLabel usernameLabel;
    public JTextField usernameField;
    public JLabel passwordLabel;
    public JPasswordField passwordField;
    public JPanel LoginPanel;
    public JFrame LoginFrame;
    public JButton tillbakaButton;
    public JButton proceedButton;

    private boolean validateLogin(String username, String password) {
        return username.equals("user") && password.equals("pass");
    }

    public void LoginPage() {
        JPanel panel = new JPanel();
        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(10);
        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(10);
        LoginFrame = new JFrame("LoginScreenGUI");
        LoginPanel = new JPanel();
        tillbakaButton = new JButton("Tillbaka");
        proceedButton = new JButton("MenuPageGUI");

        LoginPanel.add(proceedButton);
        LoginPanel.add(tillbakaButton);
        LoginPanel.add(usernameLabel);
        LoginPanel.add(usernameField);
        LoginPanel.add(passwordLabel);
        LoginPanel.add(passwordField);


        LoginFrame.add(LoginPanel);
        LoginFrame.pack();
        LoginFrame.setVisible(true);
        LoginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginFrame.dispose();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (validateLogin(username, password)) {
                    MenuPageGUI menuPage = new MenuPageGUI(LibraryManager.getCurrentUser());
                    menuPage.menuGUI();
                } else {
                    // show error message or do nothing
                    LoginErrorGUI loginError = new LoginErrorGUI();
                    loginError.ErrorGUI();

                }
            }
        });

        tillbakaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginFrame.dispose();
                HomeScreenGUI homeScreen = new HomeScreenGUI();
            }
        });
    }
}
