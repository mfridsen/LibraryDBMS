package view.entities.item;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.view.entities.item.ItemGUI;
import edu.groupeighteen.librarydbms.view.entities.user.UserGUI;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Test;

import java.sql.SQLException;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-24
 * Unit Test for the ItemGUI class.
 */
public class ItemGUITest {
    public static void main(String[] args) {
        LibraryManager.setup();
        try {
            new ItemGUI(null, ItemHandler.getItemByID(2));
        } catch (InvalidIDException e) {
            throw new RuntimeException(e);
        }
    }
}