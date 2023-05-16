package view.GUI.entities.user;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.view.GUI.entities.user.UserSearchResultGUI;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-16
 * Unit Test for the UserSearchResultGUI class.
 */
public class UserSearchResultGUITest {
    public static void main(String[] args) {
        LibraryManager.setup();
        new UserSearchResultGUI(null);
    }
}