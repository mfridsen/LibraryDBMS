package edu.groupeighteen.librarydbms.view.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-04-21
 */
public class LoginPage extends JFrame {
    public JLabel usernameLabel;
    public JTextField usernameField;
    public JLabel passwordLabel;
    public JPasswordField passwordField;
    public JPanel LoginPanel;
    public JFrame LoginFrame;
    public JButton tillbakaButton;
    public JButton proceedButton;

    public LoginPage() {
        JPanel panel = new JPanel();
        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(10);
        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(10);
        LoginFrame = new JFrame("Logga In");
        LoginPanel = new JPanel();
        tillbakaButton = new JButton("Tillbaka");
        proceedButton = new JButton("Fortsätt");

        LoginPanel.add(proceedButton);
        LoginPanel.add(tillbakaButton);
        LoginPanel.add(usernameField);
        LoginPanel.add(usernameLabel);
        LoginPanel.add(passwordField);
        LoginPanel.add(passwordLabel);

        LoginFrame.add(LoginPanel);
        LoginFrame.pack();
        LoginFrame.setVisible(true);
        LoginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginFrame.dispose();
                String username = usernameField.getText();
                MenuPage menuPage = new MenuPage(username);
                menuPage.menuGUI();

            }
        });

        tillbakaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginFrame.dispose();
                HomeScreen homeScreen = new HomeScreen();


            }
        });

    }
}
