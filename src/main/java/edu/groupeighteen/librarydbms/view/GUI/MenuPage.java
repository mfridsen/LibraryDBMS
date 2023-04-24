package edu.groupeighteen.librarydbms.view.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-04-21
 */
public class MenuPage {
    public JButton sökButton;
    public JButton mittKontoButton;
    public JButton LoggaUtButton;
    public JButton tillbakaButton;
    public JFrame menuFrame;
    public JPanel menuPanel;

    public void menuGUI(){
        menuFrame = new JFrame("Meny");
        menuPanel = new JPanel();
        tillbakaButton = new JButton("Tillbaka");
        sökButton = new JButton("Sök");
        LoggaUtButton = new JButton("Logga Ut");
        mittKontoButton = new JButton("Mitt Konto");

        menuPanel.add(tillbakaButton);
        menuPanel.add(LoggaUtButton);
        menuPanel.add(mittKontoButton);
        menuPanel.add(sökButton);

        tillbakaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuFrame.dispose();
                new LoginPage();
            }
        });
        LoggaUtButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuFrame.dispose();
                new HomeScreen();
            }
        });
        menuFrame.add(menuPanel);
        menuFrame.pack();
        menuFrame.setVisible(true);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }


}
