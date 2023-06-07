package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.control.entities.ItemHandlerUtils;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;

import static edu.groupeighteen.librarydbms.control.entities.rental.BaseRentalHandlerTest.createAndSaveRentalsWithDifferentDateAndDueDates;


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
public class ItemHandlerGUITest
{
    public static void main(String[] args)
    {
        try
        {
            LibraryManager.setup();
            //Setup rentals, some overdue
            int numOfOverdueRentals = 8;
            createAndSaveRentalsWithDifferentDateAndDueDates(numOfOverdueRentals);
            ItemHandlerUtils.printItemList(ItemHandler.getAllItems());
            LibraryManager.setCurrentUser(UserHandler.getUserByID(1));
            new ItemHandlerGUI(null);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}