package view.GUI.entities.user;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.GUI.entities.user.UserGUI;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Test;

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
        try {
            new UserGUI(null, UserHandler.getUserByID(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}