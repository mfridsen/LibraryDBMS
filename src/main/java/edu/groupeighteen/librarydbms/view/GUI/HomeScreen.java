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
public class HomeScreen extends JFrame {

    public JLabel welcomeLabel;
    public JPanel buttonPanel;
    public JFrame homeFrame;
    public JButton loginHereButton;
    public JButton infoButton;
    public JButton skapakontoButton;

    public HomeScreen() {
        welcomeLabel = new JLabel("Welcome to the library system");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);

        buttonPanel = new JPanel();
        loginHereButton = new JButton("Log in here");
        infoButton = new JButton("FAQ");
        skapakontoButton = new JButton("Skapa Konto");

        buttonPanel.add(loginHereButton);
        buttonPanel.add(infoButton);
        buttonPanel.add(skapakontoButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(welcomeLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);

        homeFrame = new JFrame("Library System");
        homeFrame.add(panel);
        homeFrame.setSize(400, 200);
        homeFrame.setLocationRelativeTo(null);
        homeFrame.setVisible(true);
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginHereButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homeFrame.dispose(); // Close the current window
                LoginScreen loginPage = new LoginScreen(); //Open the Log in page
                loginPage.LoginPage();
            }
        });

        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homeFrame.dispose();
                InfoPage infoPage = new InfoPage();
                infoPage.showInfoGUI();
            }
        });
        skapakontoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homeFrame.dispose();
                CreateAccount createAccount = new CreateAccount();
                createAccount.CreateAccountPage();
            }
        });
    }

    public static void main(String[] args) {
        HomeScreen homeScreen = new HomeScreen();
    }
}
