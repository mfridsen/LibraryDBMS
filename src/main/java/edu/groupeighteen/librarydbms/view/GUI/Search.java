package edu.groupeighteen.librarydbms.view.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-04-24
 */
public class Search extends JFrame {
private JLabel sökNamnLabel;
private JTextField sökNamnField;
private JLabel sökISBNLabel;
private JTextField sökISBNField;
private JButton sökButton;
private JButton tillbakaButton;
private JPanel searchPanel;
private JFrame searchFrame;

public void searchGUI(){
    JPanel panel = new JPanel();
    sökNamnLabel = new JLabel("Sök Namn");
    sökNamnField = new JTextField(10);
    sökISBNLabel = new JLabel("ISBN-nummer");
    sökISBNField = new JTextField(10);
    sökButton = new JButton("Sök");
    tillbakaButton = new JButton("Tillbaka");
    searchPanel = new JPanel();
    searchFrame = new JFrame("Sök Bok/Film");

    searchPanel.add(sökButton);
    searchPanel.add(tillbakaButton);
    searchPanel.add(sökNamnLabel);
    searchPanel.add(sökNamnField);
    searchPanel.add(sökISBNLabel);
    searchPanel.add(sökISBNField);

    searchFrame.add(searchPanel);
    searchFrame.pack();
    searchFrame.setVisible(true);
    searchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    tillbakaButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            searchFrame.dispose();
            MenuPage menuPage = new MenuPage("Välkommen");
            menuPage.menuGUI();


        }
    });
}



}
