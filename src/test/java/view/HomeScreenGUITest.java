package view;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.view.HomeScreenGUI;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-12
 * Unit Test for the HomeScreenGUI class.
 */
public class HomeScreenGUITest {
    public static void main(String[] args) {
        LibraryManager.setup();
        new HomeScreenGUI(null);

    }
}
