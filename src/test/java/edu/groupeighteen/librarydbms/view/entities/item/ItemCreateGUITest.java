package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.RetrievalException;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-21
 * Unit Test for the ItemCreateGUI class.
 */
public class ItemCreateGUITest
{
    public static void main(String[] args)
    {
        LibraryManager.setup();
        try
        {
            new ItemGUI(null, ItemHandler.getItemByID(1));
        }
        catch (InvalidIDException | RetrievalException e)
        {
            throw new RuntimeException(e);
        }
    }
}
