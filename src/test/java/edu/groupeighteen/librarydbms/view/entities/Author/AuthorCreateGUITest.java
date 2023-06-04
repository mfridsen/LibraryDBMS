package edu.groupeighteen.librarydbms.view.entities.Author;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.AuthorHandler;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.RetrievalException;
import edu.groupeighteen.librarydbms.view.entities.author.AuthorGUI;
import edu.groupeighteen.librarydbms.view.entities.item.ItemGUI;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-06-04
 * Unit Test for the AuthorCreateGUI class.
 */
public class AuthorCreateGUITest {
    public static void main(String[] args) {
        LibraryManager.setup();
        new AuthorGUI(null, AuthorHandler.getAuthorByID(1));
    }
}