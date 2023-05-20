package view.entities.user;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.view.entities.user.UserGUI;

import java.sql.SQLException;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-18
 * Unit Test for the UserGUI class.
 */
public class UserGUITest {
    public static void main(String[] args) {
        LibraryManager.setup();
        new UserGUI(null, UserHandler.getUserByID(1));
    }
}