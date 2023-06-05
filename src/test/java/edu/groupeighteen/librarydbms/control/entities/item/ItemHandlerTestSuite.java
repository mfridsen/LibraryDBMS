package edu.groupeighteen.librarydbms.control.entities.item;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/12/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Tests for the ItemHandler class.
 */
@Suite
@SelectClasses({
        ItemHandlerSetupTest.class,
        CreateNewLiteratureTest.class,
        CreateNewFilmTest.class,
        GetItemByIDTest.class,
        UpdateItemTest.class,
        DeleteAndRecoverItemTest.class,
        HardDeleteItemTest.class,
        GetAllItemsTest.class,
        GetItemsByTitleTest.class,
        GetItemsByClassificationTest.class,
        GetItemsByISBNTest.class,
        GetItemsByAuthorTest.class,
})
public class ItemHandlerTestSuite
{
}