package edu.groupeighteen.librarydbms.view.GUI;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-04-21
 */
public class HomeScreenGUI extends GUI {

    public JLabel welcomeLabel;
    public JPanel buttonPanel;
    public JFrame homeFrame;
    public JButton loginHereButton;
    public JButton infoButton;
    public JButton skapakontoButton;

    public HomeScreenGUI(GUI previousGUI) {
        super(previousGUI, "HomeScreenGUI");
        setupButtons();
        addButtonsToPanel(new JButton[]{loginHereButton});
        setupPanels();
        this.displayGUI();

        welcomeLabel = new JLabel("Welcome to the library system");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);

        buttonPanel = new JPanel();
        loginHereButton = new JButton("LoginScreenGUI");
        infoButton = new JButton("InfoPageGUI");
        skapakontoButton = new JButton("CreateAccountGUI");

        buttonPanel.add(loginHereButton);
        buttonPanel.add(infoButton);
        buttonPanel.add(skapakontoButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(welcomeLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);

        homeFrame = new JFrame("HomeScreenGUI");
        homeFrame.add(panel);
        homeFrame.setSize(400, 200);
        homeFrame.setLocationRelativeTo(null);
        homeFrame.setVisible(true);
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginHereButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homeFrame.dispose(); // Close the current window
                LoginScreenGUI loginPage = new LoginScreenGUI(); //Open the Log in page
                loginPage.LoginPage();
            }
        });

        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homeFrame.dispose();
                InfoPageGUI infoPage = new InfoPageGUI();
                infoPage.showInfoGUI();
            }
        });
        skapakontoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homeFrame.dispose();
                CreateAccountGUI createAccount = new CreateAccountGUI();
                createAccount.CreateAccountPage();
            }
        });
    }


    @Override
    protected JButton[] setupButtons() {
        return new JButton[0];
    }

    @Override
    protected void setupPanels() {

    }
}

