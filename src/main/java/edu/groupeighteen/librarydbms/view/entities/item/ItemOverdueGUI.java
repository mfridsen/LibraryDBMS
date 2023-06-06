package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.entities.Film;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Literature;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.RetrievalException;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
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
    private final List<Rental> overdueItemsList;
    private JPanel overdueItemsPanel;

    public ItemOverdueGUI(GUI previousGUI, List<Rental> overdueItemsList)
    {
        super(previousGUI, "Overdue Items", null);
        this.overdueItemsList = overdueItemsList;
        setupScrollPane();
        setupPanels();
        displayGUI();
    }


    private void setupScrollPane()
    {
        String[] columnNames = {"ID", "Title", "Classification", "Item Type", "ISBN/Age Rating", "Country of Production", "Actors"};

        if (overdueItemsList != null && !overdueItemsList.isEmpty())
        {
            Object[][] data = new Object[overdueItemsList.size()][columnNames.length];
            for (int i = 0; i < overdueItemsList.size(); i++)
            {
                int itemID = overdueItemsList.get(i).getItemID();
                Item item = null;

                try
                {
                    item = ItemHandler.getItemByID(itemID);
                }
                catch (InvalidIDException | RetrievalException e) //Fatal
                {
                    ExceptionHandler.HandleFatalException(e);
                }

                data[i][0] = itemID;
                data[i][1] = item.getTitle();
                data[i][2] = item.getClassificationName();
                data[i][3] = item.getType();

                if (item instanceof Literature)
                {
                    Literature literature = (Literature) item;
                    data[i][4] = literature.getISBN();
                    data[i][5] = "-";
                    data[i][6] = "-";
                }
                else if (item instanceof Film)
                {
                    Film film = (Film) item;
                    data[i][4] = film.getAgeRating();
                    data[i][5] = film.getCountryOfProduction();
                    data[i][6] = film.getListOfActors();
                }
            }

            JTable overdueItemsTable = new JTable(data, columnNames);

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