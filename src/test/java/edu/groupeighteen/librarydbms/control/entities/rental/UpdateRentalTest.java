package edu.groupeighteen.librarydbms.control.entities.rental;

import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalNotAllowedException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidLateFeeException;
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
 * Unit Test for the UpdateRental class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UpdateRentalTest extends BaseRentalHandlerTest
{

    /**
     * Tests the UpdateRental method by changing the userID.
     */
    @Test
    @Order(1)
    void testUpdateRental_userIDChanged()
    {
        System.out.println("\n1: Testing UpdateRental method by changing userID...");

        //TODO test implementation here

        System.out.println("\nTest finished.");
    }

    /**
     * Tests the UpdateRental method by changing the itemID.
     */
    @Test
    @Order(2)
    void testUpdateRental_itemIDChanged()
    {
        System.out.println("\n2: Testing UpdateRental method by changing itemID...");

        //TODO test implementation here

        System.out.println("\nTest finished.");
    }

    /**
     * Tests the UpdateRental method by changing the rentalDate.
     */
    @Test
    @Order(3)
    void testUpdateRental_rentalDateChanged()
    {
        System.out.println("\n3: Testing UpdateRental method by changing rentalDate...");

        //TODO test implementation here

        System.out.println("\nTest finished.");
    }

    /**
     * Tests the UpdateRental method by changing the rentalDueDate.
     */
    @Test
    @Order(4)
    void testUpdateRental_rentalDueDateChanged()
    {
        System.out.println("\n4: Testing UpdateRental method by changing rentalDueDate...");

        //TODO test implementation here

        System.out.println("\nTest finished.");
    }

    /**
     * Tests the UpdateRental method by changing the rentalReturnDate.
     */
    @Test
    @Order(5)
    void testUpdateRental_rentalReturnDateChanged()
    {
        System.out.println("\n5: Testing UpdateRental method by changing rentalReturnDate...");

        //TODO test implementation here

        System.out.println("\nTest finished.");
    }

    /**
     * Tests the UpdateRental method by changing the receipt.
     */
    @Test
    @Order(6)
    void testUpdateRental_receiptChanged()
    {
        System.out.println("\n6: Testing UpdateRental method by changing receipt...");

        //TODO test implementation here

        System.out.println("\nTest finished.");
    }

    /**
     * Tests the UpdateRental method by changing all fields.
     */
    @Test
    @Order(7)
    void testUpdateRental_allFieldsChanged()
    {
        System.out.println("\n7: Testing UpdateRental method by changing all fields...");

        //TODO test implementation here

        System.out.println("\nTest finished.");
    }

    /**
     * Tests the UpdateRental method with null rental.
     */
    @Test
    @Order(8)
    void testUpdateRental_nullRental()
    {
        System.out.println("\n8: Testing UpdateRental method with null rental...");

        //TODO test implementation here

        System.out.println("\nTest finished.");
    }

    /**
     * Tests the UpdateRental method with a valid rental that has been soft deleted.
     */
    @Test
    @Order(9)
    void testUpdateRental_validRentalSoftDeleted()
    {
        System.out.println("\n9: Testing UpdateRental method with a valid rental that has been soft deleted...");

        //TODO test implementation here

        System.out.println("\nTest finished.");
    }

    /**
     * Tests the UpdateRental method with a valid rental that has been recovered.
     */
    @Test
    @Order(10)
    void testUpdateRental_validRentalRecovered()
    {
        System.out.println("\n10: Testing UpdateRental method with a valid rental that has been recovered...");

        //TODO test implementation here

        System.out.println("\nTest finished.");
    }

    /**
     * Tests the UpdateRental method with a valid rental that has been hard deleted.
     */
    @Test
    @Order(11)
    void testUpdateRental_validRentalHardDeleted()
    {
        System.out.println("\n11: Testing UpdateRental method with a valid rental that has been hard deleted...");

        //TODO test implementation here

        System.out.println("\nTest finished.");
    }

    /**
     * Tests the UpdateRental method with a valid rental that does not exist.
     */
    @Test
    @Order(12)
    void testUpdateRental_validNonExistingRental()
    {
        System.out.println("\n12: Testing UpdateRental method with a valid rental that does not exist...");

        //TODO test implementation here

        System.out.println("\nTest finished.");
    }


    /**
     * Test case for updateRental method when the rental to update is null.
     * <p>
     * This test checks if a NullEntityException is correctly thrown when the rental is null.
     */
    @Test
    @Order(17)
    public void testUpdateRental_NullRental()
    {
        System.out.println("\n17: Testing updateRental method with a null rental...");

        // Call the updateRental method
        Exception exception = assertThrows(UpdateException.class, () -> RentalHandler.updateRental(null));

        // Check that the exception has the right message and cause
        assertTrue(exception.getMessage().contains("Rental Update failed:"));
        assertTrue(exception.getCause() instanceof edu.groupeighteen.librarydbms.model.exceptions.NullEntityException);

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for updateRental method when the rental to update does not exist in the database.
     * <p>
     * This test checks if a EntityNotFoundException is correctly thrown when the rental does not exist.
     */
    @Test
    @Order(18)
    public void testUpdateRental_NonExistingRental()
    {
        System.out.println("\n18: Testing updateRental method with a non-existing rental...");

        try
        {
            // Create a non-existing rental
            Rental nonExistingRental = new Rental(1, 1);
            nonExistingRental.setRentalID(1); //Needs a valid ID (> 0)

            // Call the updateRental method
            Exception exception = assertThrows(UpdateException.class,
                    () -> RentalHandler.updateRental(nonExistingRental));

            // Check that the exception has the right message and cause
            assertTrue(exception.getMessage().contains("Rental Update failed:"));
            assertTrue(exception.getCause() instanceof EntityNotFoundException);
        }
        catch (ConstructionException | InvalidIDException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for updateRental method when the rental to update has an invalid rentalID.
     * <p>
     * This test checks if an InvalidIDException is correctly thrown when the rentalID is invalid.
     */
    @Test
    @Order(19)
    public void testUpdateRental_InvalidRentalID()
    {
        System.out.println("\n19: Testing updateRental method with an invalid RentalID...");

        try
        {
            // Create a rental with an invalid ID. ID is going to be 0 by default, which is invalid
            Rental invalidRental = new Rental(1, 1);

            // Call the updateRental method
            Exception exception = assertThrows(UpdateException.class,
                    () -> RentalHandler.updateRental(invalidRental));

            // Check that the exception has the right message and cause
            assertTrue(exception.getMessage().contains("Rental Update failed:"));
            assertTrue(exception.getCause() instanceof InvalidIDException);
        }
        catch (ConstructionException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for updateRental method when only the due date of the rental is updated.
     * <p>
     * This test checks if the due date is correctly updated in the database.
     */
    @Test
    @Order(20)
    public void testUpdateRental_ChangeDueDate()
    {
        System.out.println("\n20: Testing updateRental method by only changing the DueDate...");

        try
        {
            // Create a rental and save it
            Rental rentalToUpdate = RentalHandler.createNewRental(3, 4);
            assertNotNull(rentalToUpdate);

            // Store the original values
            int originalRentalID = rentalToUpdate.getRentalID();
            LocalDateTime originalRentalDate = rentalToUpdate.getRentalDate();
            LocalDateTime originalReturnDate = rentalToUpdate.getRentalReturnDate();
            double originalLateFee = rentalToUpdate.getLateFee();

            // Change the dueDate
            LocalDateTime newDueDate = LocalDateTime.now().plusDays(7).
                    withHour(Rental.RENTAL_DUE_DATE_HOURS).withMinute(0).withSecond(0).truncatedTo(ChronoUnit.SECONDS);
            rentalToUpdate.setRentalDueDate(newDueDate);

            // Update the rental
            RentalHandler.updateRental(rentalToUpdate);

            // Retrieve the updated rental
            Rental updatedRental = RentalHandler.getRentalByID(originalRentalID);
            assertNotNull(updatedRental);

            // Check all values are as expected
            assertEquals(originalRentalID, updatedRental.getRentalID());
            assertEquals(originalRentalDate, updatedRental.getRentalDate());
            assertEquals(newDueDate, updatedRental.getRentalDueDate());
            assertEquals(originalReturnDate, updatedRental.getRentalReturnDate());
            assertEquals(originalLateFee, updatedRental.getLateFee());
        }
        catch (InvalidDateException | UpdateException | InvalidIDException | EntityNotFoundException |
               RentalNotAllowedException | InvalidTypeException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for updateRental method when only the return date of the rental is updated.
     * <p>
     * This test checks if the return date is correctly updated in the database.
     */
    @Test
    @Order(21)
    public void testUpdateRental_ChangeReturnDate()
    {
        System.out.println("\n21: Testing updateRental method by only changing the ReturnDate...");

        try
        {
            // Create a rental and save it
            Rental rentalToUpdate = RentalHandler.createNewRental(3, 4);
            assertNotNull(rentalToUpdate);

            // Store the original values
            int originalRentalID = rentalToUpdate.getRentalID();
            LocalDateTime originalRentalDate = rentalToUpdate.getRentalDate();
            LocalDateTime originalDueDate = rentalToUpdate.getRentalDueDate();
            double originalLateFee = rentalToUpdate.getLateFee();

            // Change the returnDate
            LocalDateTime newReturnDate = LocalDateTime.now().plusDays(5).
                    withHour(Rental.RENTAL_DUE_DATE_HOURS).withMinute(0).withSecond(0).truncatedTo(ChronoUnit.SECONDS);
            rentalToUpdate.setRentalReturnDate(newReturnDate);

            // Update the rental
            RentalHandler.updateRental(rentalToUpdate);

            // Retrieve the updated rental
            Rental updatedRental = RentalHandler.getRentalByID(originalRentalID);
            assertNotNull(updatedRental);

            // Check all values are as expected
            assertEquals(originalRentalID, updatedRental.getRentalID());
            assertEquals(originalRentalDate, updatedRental.getRentalDate());
            assertEquals(originalDueDate, updatedRental.getRentalDueDate());
            assertEquals(newReturnDate, updatedRental.getRentalReturnDate());
            assertEquals(originalLateFee, updatedRental.getLateFee());
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException | InvalidDateException |
               UpdateException | InvalidTypeException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for updateRental method when only the late fee of the rental is updated.
     * <p>
     * This test checks if the late fee is correctly updated in the database.
     */
    @Test
    @Order(22)
    public void testUpdateRental_ChangeLateFee()
    {
        System.out.println("\n22: Testing updateRental method by only changing the LateFee...");

        try
        {
            // Create a rental and save it
            Rental rentalToUpdate = RentalHandler.createNewRental(3, 4);
            assertNotNull(rentalToUpdate);

            // Store the original values
            int originalRentalID = rentalToUpdate.getRentalID();
            LocalDateTime originalRentalDate = rentalToUpdate.getRentalDate();
            LocalDateTime originalDueDate = rentalToUpdate.getRentalDueDate();
            LocalDateTime originalReturnDate = rentalToUpdate.getRentalReturnDate();

            // Change the lateFee
            double newLateFee = 50.0;
            rentalToUpdate.setLateFee(newLateFee);

            // Update the rental
            RentalHandler.updateRental(rentalToUpdate);

            // Retrieve the updated rental
            Rental updatedRental = RentalHandler.getRentalByID(originalRentalID);
            assertNotNull(updatedRental);

            // Check all values are as expected
            assertEquals(originalRentalID, updatedRental.getRentalID());
            assertEquals(originalRentalDate, updatedRental.getRentalDate());
            assertEquals(originalDueDate, updatedRental.getRentalDueDate());
            assertEquals(originalReturnDate, updatedRental.getRentalReturnDate());
            assertEquals(newLateFee, updatedRental.getLateFee());
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException | InvalidLateFeeException |
               UpdateException | InvalidTypeException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test case for updateRental method when the due date, return date, and late fee of the rental are updated.
     * <p>
     * This test checks if the due date, return date, and late fee are correctly updated in the database.
     */
    @Test
    @Order(23)
    public void testUpdateRental_ChangeAllFields()
    {
        System.out.println("\n23: Testing updateRental method by changing all mutable fields...");

        try
        {
            // Create a rental and save it
            Rental rentalToUpdate = RentalHandler.createNewRental(3, 4);
            assertNotNull(rentalToUpdate);

            // Store the original values
            int originalRentalID = rentalToUpdate.getRentalID();
            LocalDateTime originalRentalDate = rentalToUpdate.getRentalDate();

            // Change all mutable fields
            LocalDateTime newDueDate = LocalDateTime.now().plusDays(7).
                    withHour(Rental.RENTAL_DUE_DATE_HOURS).withMinute(0).withSecond(0).truncatedTo(ChronoUnit.SECONDS);
            LocalDateTime newReturnDate = LocalDateTime.now().plusDays(5).
                    withHour(Rental.RENTAL_DUE_DATE_HOURS).withMinute(0).withSecond(0).truncatedTo(ChronoUnit.SECONDS);
            double newLateFee = 50.0;
            rentalToUpdate.setRentalDueDate(newDueDate);
            rentalToUpdate.setRentalReturnDate(newReturnDate);
            rentalToUpdate.setLateFee(newLateFee);

            // Update the rental
            RentalHandler.updateRental(rentalToUpdate);

            // Retrieve the updated rental
            Rental updatedRental = RentalHandler.getRentalByID(originalRentalID);
            assertNotNull(updatedRental);

            // Check all values are as expected
            assertEquals(originalRentalID, updatedRental.getRentalID());
            assertEquals(originalRentalDate, updatedRental.getRentalDate());
            assertEquals(newDueDate, updatedRental.getRentalDueDate());
            assertEquals(newReturnDate, updatedRental.getRentalReturnDate());
            assertEquals(newLateFee, updatedRental.getLateFee());
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException | InvalidDateException |
               InvalidLateFeeException | UpdateException | InvalidTypeException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }
}