package edu.groupeighteen.librarydbms.view.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-04-24
 */
public class MyAccount {

    public static class alterPersonalInfo  {
        private JLabel usernameLabel;
        private JTextField usernameField;
        private JLabel passwordLabel;
        private JPasswordField passwordField;
        private JButton changeButton;
        private JPanel accountPanel;
        private JFrame accountFrame;
        private JButton tillbakaButton;

        public void changeInfoGUI() {

            JPanel panel = new JPanel();
            usernameLabel = new JLabel("Username:");
            usernameField = new JTextField(10);
            passwordLabel = new JLabel("Password:");
            passwordField = new JPasswordField(10);
            changeButton = new JButton("Change");
            tillbakaButton = new JButton("Tillbaka");
            accountPanel = new JPanel();
            accountFrame = new JFrame("Mitt Konto");

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
                    MenuPage menuPage = new MenuPage("Välkommen" + usernameLabel);
                    menuPage.menuGUI();
                }
            });
        }


        }
    }
