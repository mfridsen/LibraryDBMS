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

public class BasicGUI extends JFrame {
    private JButton loginButton;

    public BasicGUI() {
        super("Lilla Biblioteket");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.BLUE);
        //When button is clicked, the showLoginDialog method will pop up.
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoginDialog();
            }
        });
        panel.add(loginButton);
        add(panel);

        setVisible(true);
    }

    //This method handles the login window. If username and password are correct,
    //the login window will shut down.
    private void showLoginDialog() {
        JDialog loginDialog = new JDialog(this, "Login", true);
        loginDialog.setSize(300, 200);
        loginDialog.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton okButton = new JButton("OK");

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (username.equals("johanGUD") && password.equals("password")) {
                    JOptionPane.showMessageDialog(BasicGUI.this, "Logged in successfully!");

                    //Closes the login window if the login is successfull.
                    loginDialog.setVisible(false);
                    loginDialog.dispose();
                //If Login Failed, the login window will also be shut down.
                } else {
                    JOptionPane.showMessageDialog(BasicGUI.this, "Login failed!");
                }
                loginDialog.dispose();
            }
        });

        loginDialog.add(usernameLabel);
        loginDialog.add(usernameField);
        loginDialog.add(passwordLabel);
        loginDialog.add(passwordField);
        loginDialog.add(okButton);

        loginDialog.setVisible(true);
    }

    public static void main(String[] args) {
        new BasicGUI();
    }
}
