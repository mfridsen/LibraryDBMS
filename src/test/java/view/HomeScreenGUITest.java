package view;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.view.GUI.HomeScreenGUI;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Test;

import java.sql.SQLException;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-12
 * Unit Test for the HomeScreenGUI class.
 */
public class HomeScreenGUITest {
    public static void main(String[] args) {
        LibraryManager.setup();
        new HomeScreenGUI();

    }
}
