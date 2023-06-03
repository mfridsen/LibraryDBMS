package edu.groupeighteen.librarydbms.control;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandlerMetaDataTest;
import edu.groupeighteen.librarydbms.control.db.DatabaseHandlerTest;
import edu.groupeighteen.librarydbms.control.entities.item.ItemHandlerTestSuite;
import edu.groupeighteen.librarydbms.control.entities.rental.RentalHandlerTestSuite;
import edu.groupeighteen.librarydbms.control.entities.user.UserHandlerTestSuite;
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
        //AuthorHandlerTestSuite.class,
        //ClassificationHandlerTestSuite.class,
        ItemHandlerTestSuite.class,
        UserHandlerTestSuite.class,
        RentalHandlerTestSuite.class,
})

public class ControlTestSuite
{

}