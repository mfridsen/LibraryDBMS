package edu.groupeighteen.librarydbms.view.GUI.entities;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.GUI.MenuPageGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-04-24
 */
public class UserUpdateGUI {
        private JLabel usernameLabel;
        private JTextField usernameField;
        private JLabel passwordLabel;
        private JPasswordField passwordField;
        private JButton changeButton;
        private JPanel accountPanel;
        private JFrame accountFrame;
        private JButton tillbakaButton;
        private User user;

    public UserUpdateGUI(User user) {
        this.user = user;
        changeInfoGUI();
    }

    public void changeInfoGUI() {

            JPanel panel = new JPanel();
            usernameLabel = new JLabel("Username:");
            usernameField = new JTextField(10);
            passwordLabel = new JLabel("Password:");
            passwordField = new JPasswordField(10);
            changeButton = new JButton("Change");
            tillbakaButton = new JButton("Tillbaka");
            accountPanel = new JPanel();
            accountFrame = new JFrame("UserUpdateGUI");

            accountPanel.add(changeButton);
            accountPanel.add(tillbakaButton);
            accountPanel.add(usernameLabel);
            accountPanel.add(usernameField);
            accountPanel.add(passwordLabel);
            accountPanel.add(passwordField);

            accountFrame.add(accountPanel);
            accountFrame.pack();
            accountFrame.setVisible(true);
            accountFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            tillbakaButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    accountFrame.dispose();
                    new MenuPageGUI(LibraryManager.getCurrentUser());

                }
            });
            changeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    accountFrame.dispose();
                    LibraryManager.getCurrentUser().setUsername(usernameField.getText());
                    LibraryManager.getCurrentUser().setPassword(Arrays.toString(passwordField.getPassword()));
                    try {
                        UserHandler.updateUser(LibraryManager.getCurrentUser());
                        new MenuPageGUI(LibraryManager.getCurrentUser());

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);//TODO-Exception
                    }

                }
            });
        }


        }

