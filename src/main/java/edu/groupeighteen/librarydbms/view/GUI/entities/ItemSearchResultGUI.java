package edu.groupeighteen.librarydbms.view.GUI.entities;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-04-27
 *
 * this class displays results for a search performed in the ItemSearchGUI
 * leads to ItemGUI
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ItemSearchResultGUI extends JFrame {
    private JPanel searchPanel;
    private JFrame searchFrame;
    private JButton lånaButton;
    private JButton tillbakaButton;
    private JButton visaobjektButton;
    private String searchedBook;
    private String[] bookTitles = { "Harry Potter", "The Mist", "Revenge Of The Sith", "Harry Potter hej", "Harry Potter 3" }; // Example book titles


    public ItemSearchResultGUI(String searchedBook) {
        this.searchedBook = searchedBook;
        ItemSearchGUI search = new ItemSearchGUI();

        searchPanel = new JPanel();
        searchFrame = new JFrame("ItemSearchResultGUI");
        lånaButton = new JButton("Låna");
        tillbakaButton = new JButton("Tillbaka");
        visaobjektButton = new JButton("Visa objekt");

        JLabel resultatLabel;
        if (searchedBook != null) {
            resultatLabel = new JLabel("Resultat: " + getFormattedBookTitle());
        } else {
            resultatLabel = new JLabel("Inget resultat hittades.");
        }
        searchPanel.add(resultatLabel);
        searchPanel.add(lånaButton);
        searchPanel.add(tillbakaButton);
        searchPanel.add(visaobjektButton);

        searchFrame.add(searchPanel);
        searchFrame.pack();
        searchFrame.setVisible(true);
        searchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tillbakaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchFrame.dispose();
                ItemSearchGUI search = new ItemSearchGUI();
                search.searchGUI();
            }
        });
    }

    private String getFormattedBookTitle() {
        for (String title : bookTitles) {
            if (containsIgnoreCase(searchedBook, title)) {
                return title;
            }
        }
        return searchedBook;
    }

    private boolean containsIgnoreCase(String search, String source){
        return source.toLowerCase().contains(search.toLowerCase());
    }

    public static void main(String[] args) {
        ItemSearchResultGUI searchResult = new ItemSearchResultGUI("Book Title");
    }
}
