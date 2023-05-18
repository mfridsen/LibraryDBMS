package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;

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

    /**
     * Constructs a new GUI object. Stores the previous GUI and sets the title of the GUI.
     *
     */
    public ItemSearchGUI(GUI previousGUI) {
        super(previousGUI, "ItemSearchGUI");
        setupPanels();
        displayGUI();
    }

    @Override
    protected JButton[] setupButtons() {
        sökButton = new JButton("ItemSearchResultGUI");

        sökButton.addActionListener(e -> {
            String searchedBook = sökNamnField.getText();
            dispose();
            new ItemSearchResultGUI(this, searchedBook);
        });
        return new JButton[]{sökButton};
    }

    @Override
    protected void setupPanels() {
        searchPanel = new JPanel();

        sökNamnLabel = new JLabel("Sök Namn");
        sökNamnField = new JTextField(10);

        searchPanel.add(sökButton);
        searchPanel.add(sökNamnLabel);
        searchPanel.add(sökNamnField);

        GUIPanel.add(searchPanel);
    }
}
