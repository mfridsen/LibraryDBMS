package model;

import model.db.DatabaseConnectionTest;
import model.entities.author.AuthorTest;
import model.entities.classification.ClassificationTest;
import model.entities.item.FilmTestSuite;
import model.entities.item.LiteratureTestSuite;
import model.entities.rental.RentalTest;
import model.entities.user.UserTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
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
        //Model tests
        DatabaseConnectionTest.class,
        AuthorTest.class,
        ClassificationTest.class,
        LiteratureTestSuite.class,
        FilmTestSuite.class,
        UserTest.class,
        RentalTest.class,
})

public class ModelTestSuite
{

}