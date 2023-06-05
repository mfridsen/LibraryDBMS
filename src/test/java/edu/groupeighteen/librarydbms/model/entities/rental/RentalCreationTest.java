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
 * Unit Test for the RentalCreation class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RentalCreationTest
{
    /**
     * This test case validates the behavior of the Rental constructor for creation with valid input.
     * It checks whether all fields of the Rental object are correctly initialized based on the input parameters.
     */
    @Test
    @Order(1)
    void testRentalConstructor_ForCreation_ValidInput()
    {
        System.out.println("\n1: Testing Rental constructor for creation with valid input...");

        try
        {
            //Inputs for the constructor
            int userID = 1;
            int itemID = 1;

            //Call the constructor
            Rental rental = new Rental(userID, itemID);

            //Check the values of the object
            assertEquals(0, rental.getRentalID(), "Rental ID should be 0 for a newly created rental.");
            assertEquals(userID, rental.getUserID(), "User ID should match the input.");
            assertEquals(itemID, rental.getItemID(), "Item ID should match the input.");

            LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            assertEquals(now, rental.getRentalDate(), "Rental date should be approximately the current time.");
            assertNull(rental.getRentalDueDate(), "Rental due date should be null for a newly created rental.");

            assertNull(rental.getUsername(), "Username should be null for a newly created rental.");
            assertNull(rental.getItemTitle(), "Item title should be null for a newly created rental.");
            assertNull(rental.getItemType(), "Item type should be null for a newly created rental.");
            assertNull(rental.getRentalReturnDate(), "Rental return date should be null for a newly created rental.");
            assertEquals(0.0, rental.getLateFee(), "Late fee should be 0.0 for a newly created rental.");
            assertNull(rental.getReceipt(), "Receipt should be null for a newly crated rental.");
        }
        catch (ConstructionException e)
        {
            fail("Valid tests should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This test case validates the behavior of the Rental constructor for creation with invalid input.
     * It checks whether the constructor correctly throws an exception when the user ID or item ID are not valid.
     */
    @Test
    @Order(2)
    void testRentalConstructor_ForCreation_InvalidInput()
    {
        System.out.println("\n2: Testing Rental constructor for creation with invalid input...");

        //Inputs for the constructor
        int invalidUserID = 0;
        int invalidItemID = -1;

        //Test invalid user ID
        assertThrows(ConstructionException.class, () -> new Rental(invalidUserID, 1),
                "Constructor should throw an IllegalArgumentException when the user ID is not valid.");

        //Test invalid item ID
        assertThrows(ConstructionException.class, () -> new Rental(1, invalidItemID),
                "Constructor should throw an IllegalArgumentException when the item ID is not valid.");

        System.out.println("\nTEST FINISHED.");
    }

}