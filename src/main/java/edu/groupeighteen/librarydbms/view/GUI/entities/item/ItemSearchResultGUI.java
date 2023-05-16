package edu.groupeighteen.librarydbms.view.GUI.entities.item;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-04-27
 *
 * this class displays results for a search performed in the ItemSearchGUI
 * leads to ItemGUI
 */

import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ItemSearchResultGUI extends GUI {
    private JPanel searchPanel;
    private JFrame searchFrame;
    private JButton lånaButton;
    private JButton visaobjektButton;
    private String searchedBook;
    private String[] bookTitles = { "Harry Potter", "The Mist", "Revenge Of The Sith", "Harry Potter hej", "Harry Potter 3" }; // Example book titles


    public ItemSearchResultGUI(GUI previousGUI, String searchedBook) {
        super(previousGUI, "ItemSearchResultGUI");
        this.searchedBook = searchedBook;
        ItemSearchGUI search = new ItemSearchGUI(null);

        searchPanel = new JPanel();
        searchFrame = new JFrame("ItemSearchResultGUI");
        lånaButton = new JButton("Låna");
        visaobjektButton = new JButton("Visa objekt");

        JLabel resultatLabel;
        if (searchedBook != null) {
            resultatLabel = new JLabel("Resultat: " + getFormattedBookTitle());
        } else {
            resultatLabel = new JLabel("Inget resultat hittades.");
        }
        searchPanel.add(resultatLabel);
        searchPanel.add(lånaButton);
        searchPanel.add(visaobjektButton);

        searchFrame.add(searchPanel);
        searchFrame.pack();
        searchFrame.setVisible(true);
        searchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    @Override
    protected JButton[] setupButtons() {
        return new JButton[0];
    }

    @Override
    protected void setupPanels() {

    }
}
