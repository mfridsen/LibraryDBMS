package edu.groupeighteen.librarydbms.model.entities.rental;

import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/4/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the RentalCopy class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RentalCopyTest
{
    //Valid inputs
    private static final int rentalID = 1;
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

    /**
     * This test method verifies the correct functionality of the copy constructor of the Rental class.
     * The copy constructor should create a new Rental instance with the same property values as the original.
     * We test it by creating a new Rental object, then using the copy constructor to make a copy,
     * and finally verifying that all properties in the copied Rental are identical to the original.
     */
    @Test
    @Order(1)
    void testRentalConstructor_CopyRental()
    {
        System.out.println("\n1: Testing Rental copy constructor...");

        try
        {
            //Create a rental object
            //Construct with valid inputs
            Rental originalRental = new Rental(rentalID, userID, itemID, rentalDate, rentalDueDate, username, itemTitle,
                    itemType, rentalReturnDate, lateFee, receipt, false);

            //Use the copy constructor
            Rental copyRental = new Rental(originalRental);

            //Test all fields
            assertEquals(rentalID, copyRental.getRentalID(), "RentalID not set correctly.");
            assertEquals(userID, copyRental.getUserID(), "UserID not set correctly.");
            assertEquals(itemID, copyRental.getItemID(), "ItemID not set correctly.");
            assertEquals(rentalDate, copyRental.getRentalDate(), "RentalDate not set correctly.");
            assertEquals(rentalDueDate, copyRental.getRentalDueDate(), "RentalDueDate not set correctly.");
            assertEquals(username, copyRental.getUsername(), "Username not set correctly.");
            assertEquals(itemTitle, copyRental.getItemTitle(), "ItemTitle not set correctly.");
            assertEquals(itemType, copyRental.getItemType(), "ItemType not set correctly.");
            assertEquals(rentalReturnDate, copyRental.getRentalReturnDate(), "RentalReturnDate should be null.");
            assertEquals(lateFee, copyRental.getLateFee(), "LateFee not set correctly.");
            assertEquals(receipt, copyRental.getReceipt(), "Receipt not set correctly.");
            assertFalse(copyRental.isDeleted());
        }
        catch (ConstructionException e)
        {
            fail("Valid tests should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }
}