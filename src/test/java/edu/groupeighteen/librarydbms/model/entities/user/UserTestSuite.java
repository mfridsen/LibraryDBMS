package edu.groupeighteen.librarydbms.model.entities.user;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package model.user
 * @date 4/18/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit test for the User class.
 */
@Suite
@SelectClasses({
        UserCreationTest.class,

})
public class UserTestSuite
{
}