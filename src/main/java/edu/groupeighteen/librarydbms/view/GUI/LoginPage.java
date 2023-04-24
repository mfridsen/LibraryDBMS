package edu.groupeighteen.librarydbms.view.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-04-21
 */
public class LoginPage extends JFrame {
    public LoginPage() {

    setTitle("Library System - Login");

    JPanel panel = new JPanel(new BorderLayout());

    // Create a form for the user to enter their login information and add it to the panel
    JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

    JLabel usernameLabel = new JLabel("Username:");
    JTextField usernameField = new JTextField();

    JLabel passwordLabel = new JLabel("Password:");
    JPasswordField passwordField = new JPasswordField();

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        panel.add(formPanel,BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton loginButton = new JButton("Ok");
        loginButton.addActionListener(e -> {
            //TODO lägga in någon typ av validation på användarnamn och löesnord.
            //userIsValid lägga in userIsValid i parenteserna. Skapa en metod av den i en annan klass.
            if (usernameField.isValid()) {
                new MenuPage();
                //menuPage.setVisible(true);
                dispose();
            }
            System.out.println("Username: " + usernameField.getText());
            System.out.println("Password: " + new String(passwordField.getPassword()));
        });
        buttonPanel.add(loginButton);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

    add(panel);

    setSize(400,200);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);



    }
}
