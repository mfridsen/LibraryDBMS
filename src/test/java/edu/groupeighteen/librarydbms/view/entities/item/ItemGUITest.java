package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.RetrievalException;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-24
 * Unit Test for the ItemGUI class.
 */
public class ItemGUITest
{
    public static void main(String[] args)
    {
        LibraryManager.setup();
        testWithItemAllowedToRent();
    }

    private static void testWithItemNotAllowedToRent()
    {
        try
        {
            new ItemGUI(null, ItemHandler.getItemByID(2));
        }
        catch (InvalidIDException | RetrievalException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static void testWithItemAllowedToRent()
    {
        try
        {
            new ItemGUI(null, ItemHandler.getItemByID(6));
        }
        catch (InvalidIDException | RetrievalException e)
        {
            throw new RuntimeException(e);
        }
    }
}