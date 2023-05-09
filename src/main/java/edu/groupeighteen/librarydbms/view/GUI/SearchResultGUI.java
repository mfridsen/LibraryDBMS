package edu.groupeighteen.librarydbms.view.GUI;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-04-27
 */
import com.beust.ah.A;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchResultGUI extends JFrame {
    private JPanel searchPanel;
    private JFrame searchFrame;
    private JButton lånaButton;
    private JButton tillbakaButton;

    public SearchResultGUI(String searchedBook) {
        searchPanel = new JPanel();
        searchFrame = new JFrame("Sök Bok/Film");
        lånaButton = new JButton("Låna");
        tillbakaButton = new JButton("Tillbaka");

        JLabel resultatLabel;
        if (searchedBook != null) {
            resultatLabel = new JLabel("Resultat: " + searchedBook);
        } else {
            resultatLabel = new JLabel("Inget resultat hittades.");
        }
        searchPanel.add(resultatLabel);
        searchPanel.add(lånaButton);
        searchPanel.add(tillbakaButton);

        searchFrame.add(searchPanel);
        searchFrame.pack();
        searchFrame.setVisible(true);
        searchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tillbakaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Search search = new Search();
                search.searchGUI();
            }
        });
    }

    public static void main(String[] args) {
        SearchResultGUI searchResult = new SearchResultGUI("Book Title");
    }
}
