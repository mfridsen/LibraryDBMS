package control;

import control.db.DatabaseHandlerTest;
import control.entities.item.ItemHandlerTest;
import control.entities.rental.RentalHandlerTest;
import control.entities.user.UserHandlerTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @project LibraryDBMS
 * @author Mattias Fridsén
 * @date 4/18/2023
 * @contact matfir-1@student.ltu.se
 *
 * Test Suite for all the test classes related to the LibraryManager part of this application.
 * Calls all the test classes in the librarymanager package. Is itself called by the ApplicationTestSuite class.
 */

@Suite
@SelectClasses({
        //Control tests
        DatabaseHandlerTest.class, //TODO-future text blocks are all printed at the beginning when suite is run
        UserHandlerTest.class,
        ItemHandlerTest.class,
        RentalHandlerTest.class,
})

public class ControlTestSuite {

}