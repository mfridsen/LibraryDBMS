package edu.groupeighteen.librarydbms.control.entities.author;


import edu.groupeighteen.librarydbms.control.BaseHandlerTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-06-01
 * Unit Test for the AuthorHandler class.
 */
@Suite
@SelectClasses({
        CreateNewAuthorTest.class,
        GetAuthorByIDTest.class,
        UpdateAuthorTest.class,
        DeleteAndUndoDeleteAuthorTest.class,
        HardDeleteAuthorTest.class
})
public class AuthorHandlerTestSuite extends BaseHandlerTest
{


}