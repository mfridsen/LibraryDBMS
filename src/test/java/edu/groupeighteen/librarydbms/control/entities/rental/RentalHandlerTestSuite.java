package edu.groupeighteen.librarydbms.control.entities.rental;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/12/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit TestSuite for the RentalHandler class.
 */
@Suite
@SelectClasses({
        CreateNewRentalTest.class,
        GetRentalByIDTest.class,
        GetOverdueRentalsTest.class,
        DeleteAndRecoverRentalTest.class,
        UpdateRentalTest.class,
        ReturnRentalTest.class,

        //GetRentalsByRentalDayTest.class,
        //GetRentalsByRentalDateTest.class,
})
public class RentalHandlerTestSuite
{

}