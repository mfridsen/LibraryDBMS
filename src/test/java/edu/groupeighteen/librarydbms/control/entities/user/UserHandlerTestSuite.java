package edu.groupeighteen.librarydbms.control.entities.user;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 4/19/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the UserHandler class.
 */
@Suite
@SelectClasses({
        UserHandlerSetupTest.class,
        CreateNewUserTest.class,
        GetUserByIDTest.class,
        DeleteAndRecoverUserTest.class,
        //LoginAndValidationTest.class,
        //UpdateUserTest.class,
        //GetUserByUsernameTest.class
})
public class UserHandlerTestSuite
{
    /**
     * This method sets up the test environment before all test cases are executed.
     * It sets up a connection to the database and creates the necessary tables for testing.
     */
    @BeforeAll
    public static void suiteSetUp() {
        // Set up connection and tables here
    }

    /**
     * This method is called after all test cases are executed. It drops the test database and closes the connection.
     */
    @AfterAll
    public static void suiteTearDown() {
        // Clean up here
    }
}

