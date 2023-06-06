package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.model.entities.Entity;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.util.List;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.entities.item
 * @contact matfir-1@student.ltu.se
 * @date 6/6/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Brought to you by enough nicotine to kill a large horse.
 */
public class ItemHandlerGUI extends GUI
{
    public ItemHandlerGUI(GUI previousGUI)
    {
        super(previousGUI, "Item Handler GUI", null);
        setupPanels();
        displayGUI();
    }


    @Override
    protected JButton[] setupButtons()
    {
        JButton createItemButton = new JButton("Create Item");
        createItemButton.addActionListener(e ->
        {
            dispose();
            new ItemCreateGUI(this);
        });

        JButton updateItemButton = new JButton("Update Item");
        updateItemButton.addActionListener(e ->
        {
            dispose();
            new ItemSearchGUI(this); //Open an ItemSearchGUI
        });

        JButton deleteItemButton = new JButton("Delete Item");
        deleteItemButton.addActionListener(e ->
        {
            dispose();
            new ItemSearchGUI(this); //Open an ItemSearchGUI
        });

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e ->
        {
            dispose();
            new ItemSearchGUI(this); //Open an ItemSearchGUI
        });

        JButton overdueButton = new JButton("Overdue Items");
        overdueButton.addActionListener(e ->
        {
            List<Rental> overdueList = RentalHandler.getOverdueRentals();
            if (!overdueList.isEmpty())
            {
                dispose();
                new ItemOverdueGUI(this, overdueList);
            }
            else
                System.out.println("No overdue items.");
        });

        return new JButton[]{createItemButton, updateItemButton, deleteItemButton, searchButton, overdueButton};
    }

    @Override
    protected void setupPanels()
    {

    }
}