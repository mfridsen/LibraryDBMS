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


    public HomeScreen() {

        setTitle("Library System");

        JPanel panel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to the library system");
                welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
                welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
                panel.add(welcomeLabel, BorderLayout.NORTH);

                //Create a button for log in function and add it to the panel.
                JButton loginHereButton = new JButton("Log in here");
                loginHereButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose(); // Close the current window
                        new LoginPage(); //Open the Log in page
                    }
                });
                JPanel buttonPanel = new JPanel();
                buttonPanel.add(loginHereButton);

                JButton infoButton = new JButton("FAQ");
                infoButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                        InfoPage infoPage = new InfoPage();
                        infoPage.showInfoGUI();


                    }
                });
                buttonPanel.add(infoButton);
                panel.add(buttonPanel, BorderLayout.NORTH);

                add(panel);

                setSize(400, 200);
                setLocationRelativeTo(null);
                setVisible(true);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        HomeScreen homeScreen = new HomeScreen();
    }
}
