package edu.groupeighteen.librarydbms.dev;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.dev
 * @contact matfir-1@student.ltu.se
 * @date 4/17/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 *
 * Basic GUI that delivers two Strings, a username (uname) and a password (pword).
 */
//TODO JFrame to contain the GUI
//TODO JPanel that holds a button
//TODO Login button that spawns a pop up window
//TODO pop up contains two text fields with labels, and a OK button
//TODO when OK is pressed, GUI retrieves contents of text fields and says "logged in" or failed
public class BasicGUI extends JFrame{

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;


    public BasicGUI() {
        setTitle("Lilla Biblioteket");

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel1 = new JPanel(new GridLayout(4, 2, 5, 6));
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (username.equals("ADMIN") && password.equals("PASSWORD")) {
                statusLabel.setText("Logged in successfully!");
            } else{
                statusLabel.setText("Login failed!");
            }

        });
        statusLabel = new JLabel();

        panel1.add(usernameLabel);
        panel1.add(usernameField);
        panel1.add(passwordLabel);
        panel1.add(passwordField);
        panel1.add(loginButton);
        panel1.add(new JLabel());
        panel1.add(statusLabel);
        add(panel1);

        setVisible(true);
    }



    public static void main(String[] args) {
        new BasicGUI();
    }



    /*********************************** Getters and Setters are self-explanatory. ************************************/


}