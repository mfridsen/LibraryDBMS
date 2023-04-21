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
    public JTextField firstInfoField;
    public JTextField lastInfoField;
    public JButton tillbakaButton;

    public void showInfoGUI(){
        // skapar infoGUI
        firstFrame = new JFrame("InfoGUI");
        firstPanel = new JPanel();
        firstInfoField = new JTextField(20);
        lastInfoField = new JTextField(20);
        tillbakaButton = new JButton("Tillbaka");

        firstPanel.add(new JLabel("Hej och välkommen till Lilla Biblioteket"));
        firstPanel.add(firstInfoField);

        firstPanel.add(new JLabel("Våra öppetider är: "));
        firstPanel.add(lastInfoField);

        firstPanel.add(tillbakaButton);

        tillbakaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });



    }
}
