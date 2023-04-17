package control;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

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
 * Test Suite for all the test classes related to the LibraryManager part of this application.
 * Calls all the test classes in the librarymanager package. Is itself called by the ApplicationTestSuite class.
 */

@Suite
@SelectClasses({
        //Control tests
        DummyControlTest.class, //TODO REMOVE THIS WHEN REAL TEST CLASSES HAVE BEEN CREATED
        //TODO CREATE PROPER TEST CLASSES FOR THIS SUITE
})

public class ControlTestSuite {

}