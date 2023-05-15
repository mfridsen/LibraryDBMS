package view.GUI.entities.user;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.view.GUI.entities.user.UserSearchGUI;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-15
 * Unit Test for the UserSearchGUI class.
 */
public class UserSearchGUITest {
    public static void main(String[] args) {
        LibraryManager.setup();
        new UserSearchGUI(null);
    }
}