package edu.groupeighteen.librarydbms.view.entities.Author;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.AuthorHandler;
import edu.groupeighteen.librarydbms.view.entities.author.AuthorGUI;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-06-04
 * Unit Test for the AuthorGUI class.
 */
public class AuthorGUITest
{
    public static void main(String[] args)
    {
        LibraryManager.setup();
        new AuthorGUI(null, AuthorHandler.getAuthorByID(1, false));
    }
}