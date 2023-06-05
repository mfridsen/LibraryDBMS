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
 * Unit Test for the RentalRetrieval class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RentalRetrievalTest
{
    //TODO-future assert exception causes

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
     * Tests the Rental Retrieval Constructor for case when the rentalID is invalid (<= 0).
     */
    @Test
    @Order(2)
    void testRentalRetrievalConstructor_InvalidRentalID()
    {
        System.out.println("\n2: Testing Rental Retrieval Constructor method with rentalID <= 0...");

        //Testing invalid rentalID
        assertThrows(ConstructionException.class, () -> new Rental(-1, userID, itemID, rentalDate,
                        rentalDueDate,
                        username, itemTitle, itemType, rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when rentalID was invalid.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Rental Retrieval Constructor for case when the userID is invalid (<= 0).
     */
    @Test
    @Order(3)
    void testRentalRetrievalConstructor_InvalidUserID()
    {
        System.out.println("\n3: Testing Rental Retrieval Constructor method with userID <= 0...");

        //Testing invalid userID
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, -1, itemID, rentalDate,
                        rentalDueDate, username, itemTitle, itemType, rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when userID was invalid.");

        System.out.println("\nTEST FINISHED.");
    }

// Additional test method skeletons below:

    /**
     * Tests the Rental Retrieval Constructor for case when the itemID is invalid (<= 0).
     */
    @Test
    @Order(4)
    void testRentalRetrievalConstructor_InvalidItemID()
    {
        System.out.println("\n4: Testing Rental Retrieval Constructor method with itemID <= 0...");

        //Testing invalid itemID
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, -1, rentalDate,
                        rentalDueDate, username, itemTitle, itemType, rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when itemID was invalid.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Rental Retrieval Constructor for case when the rentalDate is null.
     */
    @Test
    @Order(5)
    void testRentalRetrievalConstructor_NullRentalDate()
    {
        System.out.println("\n5: Testing Rental Retrieval Constructor method with null rentalDate...");

        //Testing invalid rentalDate null
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, null,
                        rentalDueDate, username, itemTitle, itemType, rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when rentalDate was invalid.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Rental Retrieval Constructor for case when the rentalDate is in the future.
     */
    @Test
    @Order(6)
    void testRentalRetrievalConstructor_FutureRentalDate()
    {
        System.out.println("\n6: Testing Rental Retrieval Constructor method with future rentalDate...");

        //Testing invalid rentalDate in future
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, rentalDate.plusDays(1),
                        rentalDueDate, username, itemTitle, itemType, rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when rentalDate was invalid.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Rental Retrieval Constructor for case when the rentalDueDate is null.
     */
    @Test
    @Order(7)
    void testRentalRetrievalConstructor_NullRentalDueDate()
    {
        System.out.println("\n7: Testing Rental Retrieval Constructor method with null rentalDueDate...");

        //Testing invalid rentalDueDate null
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, rentalDate, null,
                        username, itemTitle, itemType, rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when rentalDueDate was invalid.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Rental Retrieval Constructor for case when the rentalDueDate is in the past.
     */
    @Test
    @Order(8)
    void testRentalRetrievalConstructor_PastRentalDueDate()
    {
        System.out.println("\n8: Testing Rental Retrieval Constructor method with past rentalDueDate...");

        //Testing invalid rentalDueDate in the past
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, rentalDate,
                        rentalDate.minusDays(1), username, itemTitle, itemType, rentalReturnDate, lateFee, receipt,
                        false),
                "Rental constructor did not throw exception when rentalDueDate was invalid.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Rental Retrieval Constructor for case when the username is null.
     */
    @Test
    @Order(9)
    void testRentalRetrievalConstructor_NullUsername()
    {
        System.out.println("\n9: Testing Rental Retrieval Constructor method with null username...");

        //Testing invalid username null
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, rentalDate,
                        rentalDueDate, null, itemTitle, itemType, rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when username was invalid.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Rental Retrieval Constructor for case when the username is empty.
     */
    @Test
    @Order(10)
    void testRentalRetrievalConstructor_EmptyUsername()
    {
        System.out.println("\n10: Testing Rental Retrieval Constructor method with empty username...");

        //Testing invalid username empty
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, rentalDate,
                        rentalDueDate, "", itemTitle, itemType, rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when username was invalid.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Rental Retrieval Constructor for case when the itemTitle is null.
     */
    @Test
    @Order(11)
    void testRentalRetrievalConstructor_NullItemTitle()
    {
        System.out.println("\n11: Testing Rental Retrieval Constructor method with null itemTitle...");

        //Testing invalid itemTitle null
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, rentalDate,
                        rentalDueDate, username, null, itemType, rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when itemTitle was invalid.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Rental Retrieval Constructor for case when the itemTitle is empty.
     */
    @Test
    @Order(12)
    void testRentalRetrievalConstructor_EmptyItemTitle()
    {
        System.out.println("\n12: Testing Rental Retrieval Constructor method with empty itemTitle...");

        //Testing invalid itemTitle empty
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, rentalDate,
                        rentalDueDate, username, "", itemType, rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when itemTitle was invalid.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Rental Retrieval Constructor for case when the itemType is null.
     */
    @Test
    @Order(13)
    void testRentalRetrievalConstructor_NullItemType()
    {
        System.out.println("\n13: Testing Rental Retrieval Constructor method with null itemType...");

        //Testing invalid itemType null
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, rentalDate,
                        rentalDueDate, username, itemTitle, null, rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when itemTitle was invalid.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Rental Retrieval Constructor for case when the itemType is empty.
     */
    @Test
    @Order(14)
    void testRentalRetrievalConstructor_EmptyItemType()
    {
        System.out.println("\n14: Testing Rental Retrieval Constructor method with empty itemType...");

        //Testing invalid itemType empty
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, rentalDate,
                        rentalDueDate, username, itemTitle, "", rentalReturnDate, lateFee, receipt, false),
                "Rental constructor did not throw exception when itemTitle was invalid.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Rental Retrieval Constructor for case when the rentalReturnDate is in the past.
     */
    @Test
    @Order(15)
    void testRentalRetrievalConstructor_PastRentalReturnDate()
    {
        System.out.println("\n15: Testing Rental Retrieval Constructor method with past rentalReturnDate...");

        //Testing invalid rentalReturnDate in the past
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, rentalDate,
                        rentalDueDate, username, itemTitle, itemType, rentalDate.minusDays(1), lateFee, receipt,
                        false),
                "Rental constructor did not throw exception when rentalReturnDate was invalid.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Tests the Rental Retrieval Constructor for case when the lateFee is less than 0.
     */
    @Test
    @Order(16)
    void testRentalRetrievalConstructor_NegativeLateFee()
    {
        System.out.println("\n16: Testing Rental Retrieval Constructor method with negative lateFee...");

        //Testing invalid lateFee less than 0
        assertThrows(ConstructionException.class, () -> new Rental(rentalID, userID, itemID, rentalDate,
                        rentalDueDate, username, itemTitle, itemType, rentalReturnDate, -1.0, receipt, false),
                "Rental constructor did not throw exception when lateFee was invalid.");

        System.out.println("\nTEST FINISHED.");
    }

}