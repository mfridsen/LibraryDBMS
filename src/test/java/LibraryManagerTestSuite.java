import control.ControlTestSuite;
import model.ModelTestSuite;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import view.ViewTestSuite;

/**
 * @project LibraryDBMS
 * @author Mattias Fridsén
 * @date 4/18/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Test Suite for all the test classes related to this LibraryManager application.
 * Calls all the other test suites.
 */

@Suite
@SelectClasses({
        //Control tests
        ControlTestSuite.class,
        //Model tests
        ModelTestSuite.class,
        //View tests
        ViewTestSuite.class
})

public class LibraryManagerTestSuite {

}