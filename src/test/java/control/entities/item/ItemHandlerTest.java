package control.entities.item;

import control.BaseHandlerTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/12/2023
 * @contact matfir-1@student.ltu.se
 *
 * Unit Tests for the ItemHandler class.
 */
@Suite
@SelectClasses({
        ItemHandlerSetupTest.class,
        CreateNewLiteratureTest.class,
        CreateNewFilmTest.class,
        GetItemByIDTest.class,
        UpdateItemTest.class,
        DeleteAndUndoDeleteLiteratureTest.class,
        DeleteAndUndoDeleteFilmTest.class,
        HardDeleteLiteratureTest.class,
        HardDeleteFilmTest.class,

})
public class ItemHandlerTest extends BaseHandlerTest
{
}