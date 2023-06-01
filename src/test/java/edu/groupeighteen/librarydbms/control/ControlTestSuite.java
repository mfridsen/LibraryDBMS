package edu.groupeighteen.librarydbms.control;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandlerMetaDataTest;
import edu.groupeighteen.librarydbms.control.db.DatabaseHandlerTest;
import edu.groupeighteen.librarydbms.control.entities.item.ItemHandlerTestSuite;
import edu.groupeighteen.librarydbms.control.entities.rental.RentalHandlerTest;
import edu.groupeighteen.librarydbms.control.entities.user.UserHandlerTest;
import edu.groupeighteen.librarydbms.control.entities.AuthorHandler;
import edu.groupeighteen.librarydbms.control.entities.ClassificationHandler;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 4/18/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Test Suite for all the test classes related to the LibraryManager part of this application.
 * Calls all the test classes in the librarymanager package. Is itself called by the ApplicationTestSuite class.
 */

@Suite
@SelectClasses({
        //Control tests
        DatabaseHandlerTest.class,
        DatabaseHandlerMetaDataTest.class,
        AuthorHandler.class,
        ClassificationHandler.class,
        ItemHandlerTestSuite.class,
        UserHandlerTest.class,
        RentalHandlerTest.class,
})

public class ControlTestSuite
{

}