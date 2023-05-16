package edu.groupeighteen.librarydbms.view.GUI.entities.item;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.view.GUI.MenuPageGUI;
import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

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

public class ItemSearchGUI extends GUI {
    private JLabel sökNamnLabel;
    private JTextField sökNamnField;
    private JButton sökButton;
    private JPanel searchPanel;
    private JFrame searchFrame;

    /**
     * Constructs a new GUI object. Stores the previous GUI and sets the title of the GUI.
     *
     */
    public ItemSearchGUI(GUI previousGUI) {
        super(previousGUI, "ItemSearchGUI");
        setupButtons();
        addButtonsToPanel(new JButton[]{sökButton});
        setupPanels();
        this.displayGUI();
    }


    public void searchGUI() {
        searchPanel = new JPanel();
        searchFrame = new JFrame("ItemSearchGUI"); //"sök bok/film"

        sökNamnLabel = new JLabel("Sök Namn");
        sökNamnField = new JTextField(10);
        sökButton = new JButton("ItemSearchResultGUI");

        searchPanel.add(sökButton);
        searchPanel.add(sökNamnLabel);
        searchPanel.add(sökNamnField);

        searchFrame.add(searchPanel);
        searchFrame.pack();
        searchFrame.setVisible(true);
        searchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        sökButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchedBook = sökNamnField.getText();
                searchFrame.dispose();
                ItemSearchResultGUI searchResult = new ItemSearchResultGUI(searchedBook);
            }
        });
    }

    @Override
    protected JButton[] setupButtons() {
        return new JButton[0];
    }

    @Override
    protected void setupPanels() {

    }
}
