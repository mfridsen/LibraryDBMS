package edu.groupeighteen.librarydbms.view.GUI.entities.rental;

import edu.groupeighteen.librarydbms.view.GUI.entities.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.GUI.entities.rental
 * @contact matfir-1@student.ltu.se
 * @date 5/15/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Brought to you by enough nicotine to kill a large horse.
 */
public class RentalCreateGUI extends GUI {
    private JButton resetButton;
    private JButton createButton;

    private JTextField userIDField;
    private JTextField itemIDField;

    public RentalCreateGUI(GUI previousGUI) {
        super(previousGUI, "RentalCreateGUI");
        setupPanels();
        displayGUI();
    }

    @Override
    protected JButton[] setupButtons() {
        resetButton = new JButton("Reset Fields");
        resetButton.addActionListener(e -> {
            resetFields();
        });

        createButton = new JButton("Create Rental");
        createButton.addActionListener(e -> {

        });

        return new JButton[]{resetButton, createButton};
    }

    private void resetFields() {
        userIDField.setText("");
        itemIDField.setText("");
    }


    @Override
    protected void setupPanels() {
        JPanel textFieldsPanel = new JPanel();
        textFieldsPanel.setLayout(new GridLayout(2,2));
        JLabel userIDLabel = new JLabel("Enter user ID:");
        userIDField = new JTextField(10);
        JLabel itemIDLabel = new JLabel("Enter item ID:");
        itemIDField = new JTextField(10);
        textFieldsPanel.add(userIDLabel);
        textFieldsPanel.add(userIDField);
        textFieldsPanel.add(itemIDLabel);
        textFieldsPanel.add(itemIDField);

        GUIPanel.add(textFieldsPanel, BorderLayout.NORTH);
    }
}