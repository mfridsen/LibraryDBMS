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
public class BasicGUI extends JFrame implements ActionListener {
    private JLabel label;
    private JButton button1;


    public BasicGUI() {
        setTitle("Lilla Biblioteket");

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel1 = new JPanel(new FlowLayout());
            add(panel1);

        JLabel label1 = new JLabel("Username:");
            panel1.add(label1);

        button1 = new JButton("Log In");
        button1.addActionListener(this);
        panel1.add(button1);

        label = new JLabel();
        panel1.add(label);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
       if (e.getSource() == button1) {
            label.setText("Successfully logged in!");
        }
    }

    public static void main(String[] args) {
        new BasicGUI();
    }



    /*********************************** Getters and Setters are self-explanatory. ************************************/


}