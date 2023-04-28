package edu.groupeighteen.librarydbms.view.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-04-27
 */
public class CreateAccount {
    public JLabel usernameLabel;
    public JTextField usernameField;
    public JLabel passwordLabel;
    public JPasswordField passwordField;
    public JLabel EmailLabel;
    public JTextField EmailField;
    public JButton tillbakaButton;
    public JButton proceedButton;
    public JPanel CreateAccountPanel;
    public JFrame CreateAccountFrame;

    public void CreateAccountPage(){
        JPanel panel = new JPanel();
        usernameLabel = new JLabel("Skapa Användarnamn:");
        usernameField = new JTextField(10);
        passwordLabel = new JLabel("Skapa Lösenord:");
        passwordField = new JPasswordField(10);
        EmailLabel = new JLabel("Skapa Email");
        EmailField = new JTextField(10);
        CreateAccountFrame = new JFrame("Skapa Konto");
        CreateAccountPanel = new JPanel();
        tillbakaButton = new JButton("Tillbaka");
        proceedButton = new JButton("Skapa");

        CreateAccountPanel.add(proceedButton);
        CreateAccountPanel.add(tillbakaButton);
        CreateAccountPanel.add(usernameLabel);
        CreateAccountPanel.add(usernameField);
        CreateAccountPanel.add(passwordLabel);
        CreateAccountPanel.add(passwordField);
        CreateAccountPanel.add(EmailLabel);
        CreateAccountPanel.add(EmailField);

        CreateAccountFrame.add(CreateAccountPanel);
        CreateAccountFrame.pack();
        CreateAccountFrame.setVisible(true);
        CreateAccountFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tillbakaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateAccountFrame.dispose();
                HomeScreen homeScreen = new HomeScreen();

            }
        });
    }
}
