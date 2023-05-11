package edu.groupeighteen.librarydbms.view.GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-05
 */
public class LoginErrorGUI extends JFrame {
    private JLabel errorMessageLabel;
    private JButton okButton;


    public void ErrorGUI() {
        JPanel panel = new JPanel();
        errorMessageLabel = new JLabel("Error! Enter the correct username and password");
        okButton = new JButton("OK");

        panel.setLayout(new GridLayout(2, 1));
        panel.add(errorMessageLabel);
        panel.add(okButton);

        add(panel);
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            LoginScreenGUI loginPage = new LoginScreenGUI();
            loginPage.LoginPage();

            }
        });
    }
}

