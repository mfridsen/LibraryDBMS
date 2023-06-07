package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.entities.Film;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Literature;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.RetrievalException;
import edu.groupeighteen.librarydbms.view.buttons.ButtonRenderer;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
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
public class ItemOverdueGUI extends GUI
{
    private final List<Item> overdueItemsList;
    private JPanel overdueItemsPanel;

    public ItemOverdueGUI(GUI previousGUI, List<Rental> overdueRentalsList)
    {
        super(previousGUI, "Overdue Items", null);

        overdueItemsList = new ArrayList<>();

        //Translate rentals into items
        for (Rental rental : overdueRentalsList)
        {
            int itemID = rental.getItemID();

            try
            {
                overdueItemsList.add(ItemHandler.getItemByID(itemID));
            }
            catch (InvalidIDException | RetrievalException e) //Fatal
            {
                ExceptionHandler.HandleFatalException(e);
            }
        }

        setupScrollPane();
        setupPanels();
        displayGUI();
    }


    private void setupScrollPane()
    {
        String[] columnNames = {"ID", "Title", "Classification", "Item Type", "ISBN/Age Rating",
                "Country of Production", "Actors", "View Item"};

        if (overdueItemsList != null && !overdueItemsList.isEmpty())
        {
            Object[][] data = new Object[overdueItemsList.size()][columnNames.length];

            //Get the data from each item
            int index = 0;
            for (Item item : overdueItemsList) {
                data[index][0] = item.getItemID();
                data[index][1] = item.getTitle();
                data[index][2] = item.getClassificationName();
                data[index][3] = item.getType();
                data[index][7] = "View"; // Text for the button

                if (item instanceof Literature) {
                    Literature literature = (Literature) item;
                    data[index][4] = literature.getISBN();
                    data[index][5] = "-";
                    data[index][6] = "-";
                } else if (item instanceof Film) {
                    Film film = (Film) item;
                    data[index][4] = film.getAgeRating();
                    data[index][5] = film.getCountryOfProduction();
                    data[index][6] = film.getListOfActors();
                }
                index++;
            }

            // Create the JTable
            ItemTable overdueItemsTable = new ItemTable(new ItemTableModel(data, columnNames),
                    overdueItemsList, this);

            // Add the new line here to set minimum column width
            TableColumnModel columnModel = overdueItemsTable.getColumnModel();
            columnModel.getColumn(7).setMinWidth(75);  // Change the width as needed

            // Set the custom cell renderer and editor for the last column
            ButtonRenderer buttonRenderer = new ButtonRenderer();
            overdueItemsTable.getColumn("View Item").setCellRenderer(buttonRenderer);

            for (Item item : overdueItemsList)
            {
                ItemGUIButtonEditor itemGUIButtonEditor = new ItemGUIButtonEditor(new JCheckBox(), item,
                        "View", this);
                overdueItemsTable.getColumnModel().getColumn(7).setCellEditor(itemGUIButtonEditor);
            }

            JScrollPane overdueItemsScrollPane = new JScrollPane();
            overdueItemsScrollPane.setViewportView(overdueItemsTable);
            overdueItemsPanel = new JPanel(new BorderLayout());
            overdueItemsPanel.add(overdueItemsScrollPane, BorderLayout.CENTER);
        }
    }

    @Override
    protected void setupPanels()
    {
        if (overdueItemsPanel != null)
        {
            GUIPanel.add(overdueItemsPanel);
        }
        else
        {
            System.err.println("OverdueItemsPanel is null.");
            dispose();
            previousGUI.displayGUI();
        }
    }

    @Override
    protected JButton[] setupButtons()
    {
        return new JButton[0];
    }
}