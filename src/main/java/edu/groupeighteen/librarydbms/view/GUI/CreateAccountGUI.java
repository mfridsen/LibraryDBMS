package edu.groupeighteen.librarydbms.view.GUI;

import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;
import edu.groupeighteen.librarydbms.view.GUI.entities.user.UserWelcomeGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;


/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-04-27
 */
public class CreateAccountGUI extends GUI {
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JLabel EmailLabel;
    private JTextField EmailField;

    private JButton tillbakaButton;
    private JButton proceedButton;
    private JPanel CreateAccountPanel;
    private JFrame CreateAccountFrame;

    /**
     * Constructs a new GUI object. Stores the previous GUI and sets the title of the GUI.
     *
     */
    public CreateAccountGUI(GUI previousGUI) {
        super(previousGUI, "CreateAccountGUI");
        setupButtons();
        addButtonsToPanel(new JButton[]{proceedButton});
        setupPanels();
        this.displayGUI();

    }

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
                HomeScreenGUI homeScreen = new HomeScreenGUI();

            }
        });
        proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateAccountFrame.dispose();
                try {
                    User newUser = UserHandler.createNewUser(usernameField.getText(), passwordField.getPassword().toString());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                UserWelcomeGUI userWelcomeGUI = new UserWelcomeGUI();
                userWelcomeGUI.WelcomeUserGUI();
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

