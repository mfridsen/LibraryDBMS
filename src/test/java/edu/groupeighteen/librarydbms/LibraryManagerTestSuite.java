package edu.groupeighteen.librarydbms;

import edu.groupeighteen.librarydbms.control.ControlTestSuite;
import edu.groupeighteen.librarydbms.model.ModelTestSuite;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;


/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 4/18/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Test Suite for all the test classes related to this LibraryManager application.
 * <p>
 * Calls all the other test suites.
 * <p>
 * Brought to you by inhumane amounts of tobacco.
 */

@Suite
@SelectClasses({
        //Model tests
        ModelTestSuite.class,
        //Control tests
        ControlTestSuite.class,
        //View tests excluded cause I have no idea how to test GUIs yet lol
        //ViewTestSuite.class
})

public class
LibraryManagerTestSuite
{

}