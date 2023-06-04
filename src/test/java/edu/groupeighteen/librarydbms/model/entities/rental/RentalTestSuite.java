package edu.groupeighteen.librarydbms.model.entities.rental;

import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidTitleException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidLateFeeException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidNameException;
import org.junit.jupiter.api.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/5/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Unit Test for the Rental class.
 */

@Suite
@SelectClasses({
        RentalCreationTest.class,
        RentalRetrievalTest.class,
        RentalCopyTest.class,
        RentalSettersTest.class
})
public class RentalTestSuite
{
}