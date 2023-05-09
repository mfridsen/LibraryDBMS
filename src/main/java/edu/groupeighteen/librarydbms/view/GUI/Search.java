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
    private JButton sökButton;
    private JButton tillbakaButton;
    private JPanel searchPanel;
    private JFrame searchFrame;
    private String[] bookTitles = {"Title 1", "Title 2", "Title 3"}; // Array of book titles

    public void searchGUI() {
        searchPanel = new JPanel();
        searchFrame = new JFrame("Sök Bok/Film");

        sökNamnLabel = new JLabel("Sök Namn");
        sökNamnField = new JTextField(10);
        sökButton = new JButton("Sök");
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
                MenuPage menuPage = new MenuPage("Välkommen");
                menuPage.menuGUI();
            }
        });

        sökButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchedBook = sökNamnField.getText();
                searchFrame.dispose();
                if (isBookTitleExists(searchedBook)) {
                    //SearchResult searchResult = new SearchResult(searchedBook);
                    //searchResult.searchResultGUI();
                }
            }
        });
    }
    private boolean isBookTitleExists(String searchedBook) {
        for (String title : bookTitles) {
            if (title.equalsIgnoreCase(searchedBook)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Search search = new Search();
        search.searchGUI();
    }
}