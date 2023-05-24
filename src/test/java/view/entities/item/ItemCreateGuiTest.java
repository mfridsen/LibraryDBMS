package view.entities.item;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.view.entities.item.ItemCreateGUI;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemCreateGuiTest {
    public static void main(String[] args) {
        LibraryManager.setup();
        new ItemCreateGUI(null);
    }
}