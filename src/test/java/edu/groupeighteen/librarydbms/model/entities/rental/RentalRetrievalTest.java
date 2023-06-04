package edu.groupeighteen.librarydbms.model.entities.rental;

import static org.junit.jupiter.api.Assertions.*;

import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/4/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the RentalRetrieval class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RentalRetrievalTest
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
     * This test case validates the behavior of the Rental constructor when provided with valid data from the database.
     * It checks whether all fields of the Rental object are correctly initialized based on the input parameters.
     */
    @Test
    @Order(1)
    void testRetrievalConstructor_ValidInput()
    {
        System.out.println("\n1: Testing Rental constructor with data retrieved from the database...");

        try
        {
            //Construct with valid inputs
            Rental rental = new Rental(rentalID, userID, itemID, rentalDate, rentalDueDate, username, itemTitle,
                    itemType, rentalReturnDate, lateFee, receipt, false);

            //Test all fields
            assertEquals(rentalID, rental.getRentalID(), "RentalID not set correctly.");
            assertEquals(userID, rental.getUserID(), "UserID not set correctly.");
            assertEquals(itemID, rental.getItemID(), "ItemID not set correctly.");
            assertEquals(rentalDate, rental.getRentalDate(), "RentalDate not set correctly.");
            assertEquals(rentalDueDate, rental.getRentalDueDate(), "RentalDueDate not set correctly.");
            assertEquals(username, rental.getUsername(), "Username not set correctly.");
            assertEquals(itemTitle, rental.getItemTitle(), "ItemTitle not set correctly.");
            assertEquals(itemType, rental.getItemType(), "ItemType not set correctly.");
            assertEquals(rentalReturnDate, rental.getRentalReturnDate(), "RentalReturnDate should be null.");
            assertEquals(lateFee, rental.getLateFee(), "LateFee not set correctly.");
            assertEquals(receipt, rental.getReceipt(), "Receipt not set correctly.");
            assertFalse(rental.isDeleted());
        }
        catch (ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid tests should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This test case validates the behavior of the Rental constructor when provided with invalid data from the database.
     * It checks whether appropriate exceptions are thrown for each type of invalid input.
     */
    @Test
    @Order(4)
    void testRetrievalConstructor_InvalidInput()
    {
        System.out.println(
                "\n4: Testing Rental constructor with data retrieved from the database and invalid input...");

        //Testing invalid rentalID
        assertThrows(ConstructionException.class, () -> new Rental(-1, userID, itemID, rentalDate,
                        rentalDueDate,
                        username, itemTitle, itemType, rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when rentalID was invalid.");

        //Testing invalid userID
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, -1, itemID, rentalDate,
                        rentalDueDate, username, itemTitle, itemType, rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when userID was invalid.");

        //Testing invalid itemID
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, -1, rentalDate,
                        rentalDueDate, username, itemTitle, itemType, rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when itemID was invalid.");

        //Testing invalid rentalDate in future
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, rentalDate.plusDays(1),
                        rentalDueDate, username, itemTitle, itemType, rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when rentalDate was invalid.");

        //Testing invalid rentalDate null
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, null,
                        rentalDueDate, username, itemTitle, itemType, rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when rentalDate was invalid.");

        //Testing invalid username empty
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, rentalDate,
                        rentalDueDate, "", itemTitle, itemType, rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when username was invalid.");

        //Testing invalid username null
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, rentalDate,
                        rentalDueDate, null, itemTitle, itemType, rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when username was invalid.");

        //Testing invalid itemTitle empty
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, rentalDate,
                        rentalDueDate, username, "", itemType, rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when itemTitle was invalid.");

        //Testing invalid itemTitle null
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, rentalDate,
                        rentalDueDate, username, null, itemType, rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when itemTitle was invalid.");

        //Testing invalid rentalDueDate in the past
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, rentalDate,
                        rentalDate.minusDays(1), username, itemTitle, itemType, rentalReturnDate, lateFee, receipt,
                        false),
                "Rental constructor did not throw exception when rentalDueDate was invalid.");

        //Testing invalid rentalDueDate null
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, rentalDate, null,
                        username, itemTitle, itemType, rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when rentalDueDate was invalid.");

        //Testing invalid rentalReturnDate in the past
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, rentalDate,
                        rentalDueDate, username, itemTitle, itemType, rentalDate.minusDays(1), lateFee, receipt,
                        false),
                "Rental constructor did not throw exception when rentalReturnDate was invalid.");

        //Testing invalid lateFee less than 0
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, rentalDate,
                        rentalDueDate, username, itemTitle, itemType, rentalReturnDate, -1.0, receipt, false),
                "Rental constructor did not throw exception when lateFee was invalid.");

        System.out.println("\nTEST FINISHED.");
    }
}