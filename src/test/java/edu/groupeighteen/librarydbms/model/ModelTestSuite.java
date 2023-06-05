package edu.groupeighteen.librarydbms.model;

import edu.groupeighteen.librarydbms.model.db.DatabaseConnectionTest;
import edu.groupeighteen.librarydbms.model.entities.author.AuthorTestSuite;
import edu.groupeighteen.librarydbms.model.entities.classification.ClassificationTestSuite;
import edu.groupeighteen.librarydbms.model.entities.item.FilmTestSuite;
import edu.groupeighteen.librarydbms.model.entities.item.LiteratureTestSuite;
import edu.groupeighteen.librarydbms.model.entities.rental.RentalTestSuite;
import edu.groupeighteen.librarydbms.model.entities.user.UserTestSuite;
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
        AuthorTestSuite.class,
        ClassificationTestSuite.class,
        LiteratureTestSuite.class,
        FilmTestSuite.class,
        UserTestSuite.class,
        RentalTestSuite.class,
})

public class ModelTestSuite
{
}