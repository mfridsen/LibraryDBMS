package edu.groupeighteen.librarydbms.control.entities.rental;

import static org.junit.jupiter.api.Assertions.*;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalNotAllowedException;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalReturnException;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/5/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the ReturnRental class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReturnRentalTest extends  BaseRentalHandlerTest
{
    //Test Subjects
    private static Rental validRental;
    private static Rental nonExistingRental;
    private static Rental alreadyReturnedRental;

    //Valid inputs
    private static final int rentalID = 10; //ValidRental is going to have 1, returned will have 2
    private static final int userID = 1;
    private static final int itemID = 1;
    private static final LocalDateTime rentalDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    private static final LocalDateTime rentalDueDate = rentalDate.plusDays(5).withHour(20).withMinute(0).withSecond(0);
    private static final String username = "TestUser";
    private static final String itemTitle = "TestItem";
    private static final String itemType = "TestType";
    private static final LocalDateTime rentalReturnDate = rentalDate.plusDays(4);
    private static final double lateFee = 0.0;
    private static final String receipt = "TestReceipt";


    @BeforeEach
    void setUp()
    {
        try
        {
            validRental = RentalHandler.createNewRental(3, 3);
            nonExistingRental = new Rental(rentalID, userID, itemID, rentalDate, rentalDueDate, username, itemTitle,
                    itemType, rentalReturnDate, lateFee, receipt, false);
            alreadyReturnedRental = RentalHandler.createNewRental(4, 4);
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException | ConstructionException |
               InvalidTypeException e)
        {
            e.printStackTrace();
            fail("Exception thrown during setup.");
        }
    }

    @AfterEach
    void resetRentalsTable()
    {
        DatabaseHandler.executeCommand("DELETE FROM rentals;");
    }

    /**
     * Test to verify successful rental return.
     */
    @Test
    @Order(1)
    void testReturnRental_ValidRental()
    {
        System.out.println("\n1: Testing returnRental method with a valid rental...");

        try
        {
            //Assert all is as it should be
            assertNotNull(validRental);
            assertNotNull(validRental.getRentalDate());
            assertNotNull(validRental.getRentalDueDate());
            assertNull(validRental.getRentalReturnDate());

            //Item is unavailable
            Item item = ItemHandler.getItemByID(validRental.getItemID());
            assertNotNull(item);
            assertFalse(item.isAvailable());

            //User has > 0 current rentals
            User user = UserHandler.getUserByID(validRental.getUserID());
            assertNotNull(user);
            int previousCurrentRentals = user.getCurrentRentals();
            assertTrue(previousCurrentRentals > 0);

            //Return
            RentalHandler.returnRental(validRental);

            //Re-assert
            assertNotNull(validRental.getRentalReturnDate());
            validRental = RentalHandler.getRentalByID(validRental.getRentalID());
            assertNotNull(validRental);
            assertNotNull(validRental.getRentalDate());
            assertNotNull(validRental.getRentalDueDate());
            assertNotNull(validRental.getRentalReturnDate());

            //Item and user again
            item = ItemHandler.getItemByID(validRental.getItemID());
            assertNotNull(item);
            assertTrue(item.isAvailable());

            user = UserHandler.getUserByID(validRental.getUserID());
            assertNotNull(user);
            assertTrue(previousCurrentRentals > user.getCurrentRentals());
        }
        catch (RentalReturnException | InvalidIDException | RetrievalException e)
        {
            e.printStackTrace();
            fail("Exception thrown during test.");
        }

        System.out.println("\nTest finished.");
    }

    /**
     * Test to verify NullEntityException is thrown when null rental is returned.
     */
    @Test
    @Order(2)
    void testReturnRental_NullRental()
    {
        System.out.println("\n2: Testing returnRental method with a null rental...");

        Exception e = assertThrows(RentalReturnException.class, () -> RentalHandler.returnRental(null));
        assertTrue(e.getCause() instanceof NullEntityException);

        System.out.println("\nTest finished.");
    }

    /**
     * Test to verify EntityNotFoundException is thrown when non-existent rental is returned.
     */
    @Test
    @Order(3)
    void testReturnRental_NonExistentRental()
    {
        System.out.println("\n3: Testing returnRental method with a non-existent rental...");

        Exception e = assertThrows(RentalReturnException.class, () -> RentalHandler.returnRental(nonExistingRental));
        assertTrue(e.getCause() instanceof EntityNotFoundException);

        System.out.println("\nTest finished.");
    }

    /**
     * Test to verify RentalReturnException is thrown when already returned rental is returned again.
     */
    @Test
    @Order(4)
    void testReturnRental_AlreadyReturnedRental()
    {
        System.out.println("\n4: Testing returnRental method with an already returned rental...");

        try
        {
            //Return rental
            RentalHandler.returnRental(alreadyReturnedRental);

            //Try to return it again
            Exception e = assertThrows(RentalReturnException.class, () -> RentalHandler.returnRental(alreadyReturnedRental));
            assertTrue(e.getMessage().contains("rental already returned"));
        }
        catch (RentalReturnException ex)
        {
            ex.printStackTrace();
            fail("Exception thrown during test.");
        }

        System.out.println("\nTest finished.");
    }
}