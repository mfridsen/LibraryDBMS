package edu.groupeighteen.librarydbms.model.entities.item;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/30/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Test Suite for the test classes dedicated to the Film Entity class.
 */
@Suite
@SelectClasses({
        FilmCreationTest.class,
        FilmRetrievalTest.class,
        FilmCopyTest.class,
        FilmSettersTest.class
})
public class FilmTestSuite
{
}