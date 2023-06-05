package edu.groupeighteen.librarydbms.view.entities.Author;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.view.entities.author.AuthorSearchGUI;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-06-04
 * Unit Test for the AuthorSearchGUI class.
 */
public class AuthorSearchGUITest
{
    public static void main(String[] args)
    {
        LibraryManager.setup();
        new AuthorSearchGUI(null);
    }
}