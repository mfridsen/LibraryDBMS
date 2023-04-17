import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package PACKAGE_NAME
 * @date 4/17/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Test Suite for all the test classes related to the Library part of this application.
 * Calls all the test classes in the library package. Is itself called by the ApplicationTestSuite class.
 */

@Suite
@SelectClasses({
        //Test Classes here
        DummyLibraryTest.class, //TODO REMOVE THIS WHEN REAL TEST CLASSES HAVE BEEN CREATED
})

public class LibraryTestSuite {

}