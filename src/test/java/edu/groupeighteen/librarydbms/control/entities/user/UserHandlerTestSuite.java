package edu.groupeighteen.librarydbms.control.entities.user;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 4/19/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test Suite for the UserHandler class.
 */
@Suite
@SelectClasses({
        UserHandlerSetupTest.class,
        CreateNewUserTest.class,
        GetUserByIDTest.class,
        DeleteAndRecoverUserTest.class,
        LoginAndValidationTest.class,
        UpdateUserTest.class,
        GetUserByUsernameTest.class
})
public class UserHandlerTestSuite
{

}

