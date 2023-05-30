package control.entities.item;

import control.BaseHandlerTest;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.SQLException;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/12/2023
 * @contact matfir-1@student.ltu.se
 *
 * Unit Test for the ItemHandler class.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemHandlerTest extends BaseHandlerTest
{

    //TODO-future make all tests more verbose
    //TODO-future javadoc tests properly

    //TODO-PRIO CHANGE ORDER OF TESTS TO MATCH ORDER OF METHODS

    @BeforeEach
    @Override
    protected void setupAndReset()
    {
        try
        {
            setupConnectionAndTables();
        }
        catch (SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @AfterEach
    void resetItemHandler()
    {
        ItemHandler.reset(); //Need to clear everything between tests
    }

}