package edu.groupeighteen.librarydbms.view.GUI.entities.item;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.view.GUI.MenuPageGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-04-24
 *
 * this class handles searching for items
 * leads to ItemSearchResultGUI
 */

public class ItemSearchGUI extends JFrame {
    private JLabel sökNamnLabel;
    private JTextField sökNamnField;
    private JButton sökButton;
    private JButton tillbakaButton;
    private JPanel searchPanel;
    private JFrame searchFrame;


    public void searchGUI() {
        searchPanel = new JPanel();
        searchFrame = new JFrame("ItemSearchGUI"); //"sök bok/film"

        sökNamnLabel = new JLabel("Sök Namn");
        sökNamnField = new JTextField(10);
        sökButton = new JButton("ItemSearchResultGUI");
        tillbakaButton = new JButton("Tillbaka");

        searchPanel.add(sökButton);
        searchPanel.add(tillbakaButton);
        searchPanel.add(sökNamnLabel);
        searchPanel.add(sökNamnField);

        searchFrame.add(searchPanel);
        searchFrame.pack();
        searchFrame.setVisible(true);
        searchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tillbakaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchFrame.dispose();
                MenuPageGUI menuPage = new MenuPageGUI(LibraryManager.getCurrentUser());
                menuPage.menuGUI();
            }
        });

        sökButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchedBook = sökNamnField.getText();
                searchFrame.dispose();
                ItemSearchResultGUI searchResult = new ItemSearchResultGUI(searchedBook);
            }
        });
    }
}
