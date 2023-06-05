package edu.groupeighteen.librarydbms.view.entities.user;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-18
 * Unit Test for the UserWelcome class.
 */
public class UserWelcomeTest
{
    public static void main(String[] args)
    {
        LibraryManager.setup();
        try
        {
            new UserGUI(null, UserHandler.getUserByID(1));
        }
        catch (InvalidIDException e)
        {
            e.printStackTrace();
        }
    }
}