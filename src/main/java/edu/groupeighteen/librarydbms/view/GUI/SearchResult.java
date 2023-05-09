package edu.groupeighteen.librarydbms.view.GUI;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-04-27
 */
import javax.swing.*;

public class SearchResult extends JFrame {
    private JPanel searchPanel;
    private JFrame searchFrame;

    public void searchResultGUI() {
        searchPanel = new JPanel();
        searchFrame = new JFrame("Sök Bok/Film");

        JLabel resultatLabel = new JLabel("Resultat");
        searchPanel.add(resultatLabel);

        searchFrame.add(searchPanel);
        searchFrame.pack();
        searchFrame.setVisible(true);
        searchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

