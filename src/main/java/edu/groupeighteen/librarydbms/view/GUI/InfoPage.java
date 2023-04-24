package edu.groupeighteen.librarydbms.view.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-04-21
 */
public class InfoPage {
    public JFrame firstFrame;
    public JPanel firstPanel;
    public JButton tillbakaButton;

    public void showInfoGUI(){
        // skapar infoGUI
        firstFrame = new JFrame("Information");
        firstPanel = new JPanel();
        tillbakaButton = new JButton("Tillbaka");

        firstPanel.add(new JLabel("Hej och välkommen till Lilla Biblioteket"));
        firstPanel.add(new JLabel("Våra öppetider är: "));
        firstPanel.add(new JLabel("Mån-Fre: 9-17"));
        firstPanel.add(new JLabel("Lör-Sön: Stängt"));
        firstPanel.add(new JLabel(""));

        firstPanel.add(new JLabel("Lilla Biblioteket är beläget i norra Sverige på östkusten."));
        firstPanel.add(new JLabel("Vi erbjuder en härlig atmosfär och många spännande böcker."));
        firstPanel.add(new JLabel(""));

        firstPanel.add(tillbakaButton);

        tillbakaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                firstFrame.dispose();
                new HomeScreen();

            }
        });
        // Add the first panel to the first frame and set its properties
        firstFrame.add(firstPanel);
        firstFrame.pack();
        firstFrame.setVisible(true);
        firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



    }
}
