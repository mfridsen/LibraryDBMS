package edu.groupeighteen.librarydbms.control.entities.rental;

import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalNotAllowedException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/4/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the DeleteAndRecoverRental class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DeleteAndRecoverRentalTest extends BaseRentalHandlerTest
{
    //SOFT DELETE ------------------------------------------------------------------------------------------------------

    /**
     * Test method for {@link RentalHandler#deleteRental(Rental)}.
     * Case: A valid Rental object is passed as argument.
     * The method is expected to perform successfully, and the database should no longer contain the softly deleted Rental.
     */
    @Test
    @Order(1)
    void testDeleteRental_ValidRental()
    {
        System.out.println("\n1: Testing DeleteRental method with a valid rental...");

        try
        {
            // Create and save a new rental
            Rental rentalToDelete = RentalHandler.createNewRental(validUserIDs[0], validUserIDs[0]);
            assertNotNull(rentalToDelete);

            // Softly delete the rental
            try
            {
                RentalHandler.deleteRental(rentalToDelete);
            }
            catch (DeletionException e)
            {
                fail("An unexpected exception occurred: " + e.getMessage());
            }

            // Retrieve the rental from the database
            Rental retrievedRental = RentalHandler.getRentalByID(rentalToDelete.getRentalID());
            assertNotNull(retrievedRental);

            // Verify the rental is softly deleted
            assertTrue(retrievedRental.isDeleted(), "The rental should be softly deleted.");
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException | InvalidTypeException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test method for {@link RentalHandler#deleteRental(Rental)}.
     * Case: A null Rental object is passed as argument.
     * The method is expected to throw a DeletionException.
     */
    @Test
    @Order(2)
    void testDeleteRental_NullRental()
    {
        System.out.println("\n2: Testing DeleteRental method with a null rental...");

        // Attempt to softly delete a null rental
        Exception e = assertThrows(DeletionException.class, () -> RentalHandler.deleteRental(null),
                "A DeletionException should be thrown when attempting to softly delete a null rental.");
        assertTrue(e.getCause() instanceof edu.groupeighteen.librarydbms.model.exceptions.NullEntityException,
                "The cause of the DeletionException should be a NullEntityException.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test method for {@link RentalHandler#deleteRental(Rental)}.
     * Case: A Rental object that doesn't exist in the database is passed as argument.
     * The method is expected to throw a DeletionException.
     */
    @Test
    @Order(3)
    void testDeleteRental_NonExistentRental()
    {
        System.out.println("\n3: Testing DeleteRental method with a non-existent rental...");

        try
        {
            // Create a rental that doesn't exist in the database
            Rental nonExistentRental = new Rental(1, 1);
            nonExistentRental.setRentalID(1); //Make sure the rental has a valid ID

            // Attempt to softly delete the non-existent rental
            Exception e = assertThrows(DeletionException.class, () -> RentalHandler.deleteRental(nonExistentRental),
                    "A DeletionException should be thrown when attempting to softly delete a non-existent rental.");
            assertTrue(e.getCause() instanceof EntityNotFoundException,
                    "The cause of the DeletionException should be a EntityNotFoundException.");
        }
        catch (ConstructionException | InvalidIDException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test method for {@link RentalHandler#recoverRental(Rental)}.
     * Case: A softly deleted Rental object is passed as argument.
     * The method is expected to perform successfully, and the database should contain the recovered Rental.
     */
    @Test
    @Order(4)
    void testDeleteRental_AlreadyDeletedRental()
    {
        System.out.println(
                "\n4: Testing DeleteRental method with a rental that has already been softly deleted...");

        try
        {
            // Create a new rental, softly delete it, and then try to softly delete it again
            Rental rentalToDelete = RentalHandler.createNewRental(validUserIDs[0], validItemIDs[0]);
            assertNotNull(rentalToDelete);
            RentalHandler.deleteRental(rentalToDelete);
            RentalHandler.deleteRental(rentalToDelete);

            // Verify that the rental is marked as deleted in the database
            Rental deletedRental = RentalHandler.getRentalByID(rentalToDelete.getRentalID());
            assertNotNull(deletedRental);
            assertTrue(deletedRental.isDeleted(), "The rental should still be marked as deleted after a s" +
                    "econd soft delete.");
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException | DeletionException |
               InvalidTypeException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    //UNDO SOFT DELETE -------------------------------------------------------------------------------------------------

    /**
     * Test method for {@link RentalHandler#recoverRental(Rental)}.
     * Case: A valid Rental object that was softly deleted is passed as argument.
     * The method is expected to perform successfully, and the database should contain the recovered Rental, no longer marked as deleted.
     */
    @Test
    @Order(5)
    void testRecoverRental_ValidRental()
    {
        System.out.println("\n5: Testing RecoverRental method with a valid, softly deleted rental...");

        try
        {
            // Create a new rental, softly delete it, and then undo the soft delete
            Rental rentalToRecover = RentalHandler.createNewRental(validUserIDs[0], validItemIDs[0]);
            assertNotNull(rentalToRecover);
            RentalHandler.deleteRental(rentalToRecover);
            RentalHandler.recoverRental(rentalToRecover);

            // Verify that the rental is not marked as deleted in the database
            Rental recoveredRental = RentalHandler.getRentalByID(rentalToRecover.getRentalID());
            assertNotNull(recoveredRental);
            assertFalse(recoveredRental.isDeleted(),
                    "The rental should not be marked as deleted after undoing the soft delete.");
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException | DeletionException |
               RecoveryException | InvalidTypeException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test method for {@link RentalHandler#recoverRental(Rental)}.
     * Case: A null Rental object is passed as argument.
     * The method is expected to throw a UpdateException.
     */
    @Test
    @Order(6)
    void testRecoverRental_NullRental()
    {
        System.out.println("\n6: Testing RecoverRental method with a null rental...");

        // Attempt to undo a soft delete on a null rental
        Exception e = assertThrows(RecoveryException.class,
                () -> RentalHandler.recoverRental(null),
                "A RecoveryException should be thrown when attempting to undo a soft " +
                        "delete on a null rental.");
        assertTrue(e.getCause() instanceof edu.groupeighteen.librarydbms.model.exceptions.NullEntityException,
                "The cause of the RecoveryException should be a NullEntityException.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test method for {@link RentalHandler#recoverRental(Rental)}.
     * Case: A Rental object that wasn't softly deleted is passed as argument.
     * The method is expected to perform successfully without changing the Rental.
     */
    @Test
    @Order(7)
    void testRecoverRental_NotSoftlyDeletedRental()
    {
        System.out.println("\n7: Testing RecoverRental method with a rental that was not softly deleted...");

        try
        {
            // Create a new rental and attempt to undo a soft delete on it
            Rental rental = RentalHandler.createNewRental(validUserIDs[0], validItemIDs[0]);
            assertNotNull(rental);
            RentalHandler.recoverRental(rental);

            // Verify that the rental is not marked as deleted in the database
            Rental rentalInDB = RentalHandler.getRentalByID(rental.getRentalID());
            assertNotNull(rentalInDB);
            assertFalse(rentalInDB.isDeleted(),
                    "The rental should not be marked as deleted if it was never softly deleted.");
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException | RecoveryException |
               InvalidTypeException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }


    //HARD DELETE ------------------------------------------------------------------------------------------------------

    /**
     * Test method for {@link RentalHandler#hardDeleteRental(Rental)}.
     * Case: A valid Rental object is passed as argument.
     * The method is expected to perform successfully, and the database should no longer contain the deleted Rental.
     */
    @Test
    @Order(8)
    void testHardDeleteRental_ValidRental()
    {
        System.out.println("\n8: Testing deleteRental method with a valid rental...");

        try
        {
            // Create a new rental and delete it
            Rental rentalToDelete = RentalHandler.createNewRental(validUserIDs[0], validItemIDs[0]);
            assertNotNull(rentalToDelete);
            RentalHandler.hardDeleteRental(rentalToDelete);

            // Verify that the rental no longer exists in the database
            Rental deletedRental = RentalHandler.getRentalByID(rentalToDelete.getRentalID());
            assertNull(deletedRental, "The rental should be null after being deleted.");
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException | DeletionException |
               InvalidTypeException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test method for {@link RentalHandler#hardDeleteRental(Rental)}.
     * Case: A null Rental object is passed as argument.
     * The method is expected to throw a DeletionException.
     */
    @Test
    @Order(9)
    void testHardDeleteRental_NullRental()
    {
        System.out.println("\n9: Testing deleteRental method with a null rental...");

        // Attempt to delete a null rental
        Exception e = assertThrows(DeletionException.class,
                () -> RentalHandler.hardDeleteRental(null),
                "A DeletionException should be thrown " +
                        "when attempting to delete a null rental.");
        assertTrue(e.getCause() instanceof edu.groupeighteen.librarydbms.model.exceptions.NullEntityException,
                "The cause of the DeletionException should be a NullEntityException.");

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test method for {@link RentalHandler#hardDeleteRental(Rental)}.
     * Case: A Rental object that doesn't exist in the database is passed as argument.
     * The method is expected to throw a DeletionException.
     */
    @Test
    @Order(10)
    void testDeleteRental_NotExistingRental()
    {
        System.out.println("\n10: Testing deleteRental method with a rental that doesn't exist...");

        try
        {
            // Attempt to delete a rental that doesn't exist in the database
            Rental nonExistentRental = new Rental(validUserIDs[0], validItemIDs[0]);
            nonExistentRental.setRentalID(1); //Needs a valid ID
            Exception e = assertThrows(DeletionException.class,
                    () -> RentalHandler.hardDeleteRental(nonExistentRental),
                    "A DeletionException should be thrown when attempting to delete a non-existent rental.");
            assertTrue(e.getCause() instanceof EntityNotFoundException,
                    "The cause of the DeletionException should be a EntityNotFoundException.");
        }
        catch (ConstructionException | InvalidIDException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test method for {@link RentalHandler#hardDeleteRental(Rental)}.
     * Case: A Rental object that was softly deleted is passed as argument.
     * The method is expected to perform successfully, and the database should no longer contain the deleted Rental.
     */
    @Test
    @Order(11)
    void testHardDeleteRental_SoftlyDeletedRental()
    {
        System.out.println("\n11: Testing deleteRental method with a rental that was softly deleted...");

        try
        {
            // Create a new rental, softly delete it, and then hard delete it
            Rental rentalToDelete = RentalHandler.createNewRental(validUserIDs[0], validItemIDs[0]);
            assertNotNull(rentalToDelete);
            RentalHandler.deleteRental(rentalToDelete);
            RentalHandler.hardDeleteRental(rentalToDelete);

            // Verify that the rental no longer exists in the database
            Rental deletedRental = RentalHandler.getRentalByID(rentalToDelete.getRentalID());
            assertNull(deletedRental, "The rental should be null after being deleted, " +
                    "even if it was softly deleted before.");
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException | DeletionException |
               InvalidTypeException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test method for {@link RentalHandler#deleteRental(Rental)}.
     * Case: A Rental object that was hard deleted is passed as argument.
     * An exception of type DeletionException should be thrown, with its cause being a EntityNotFoundException, since the rental
     * has been hard deleted and doesn't exist in the database anymore.
     */
    @Test
    @Order(12)
    void testSoftDeleteRental_AlreadyHardDeletedRental()
    {
        System.out.println("\n12: Testing softDeleteRental method with a rental that has already been hard deleted...");

        try
        {
            // Create a new rental, hard delete it, and then try to softly delete it
            Rental rentalToDelete = RentalHandler.createNewRental(validUserIDs[0], validItemIDs[0]);
            RentalHandler.hardDeleteRental(rentalToDelete);

            // Attempt to softly delete the hard deleted rental
            Exception e = assertThrows(DeletionException.class,
                    () -> RentalHandler.deleteRental(rentalToDelete),
                    "A DeletionException should be thrown when attempting to softly delete a " +
                            "hard deleted rental.");
            assertTrue(e.getCause() instanceof EntityNotFoundException,
                    "The cause of the DeletionException should be a EntityNotFoundException.");
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException | DeletionException |
               InvalidTypeException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    /**
     * Test method for {@link RentalHandler#recoverRental(Rental)}.
     * Case: A Rental object that was hard deleted is passed as argument.
     * An exception of type RecoveryException should be thrown, with its cause being a EntityNotFoundException, since the rental
     * has been hard deleted and doesn't exist in the database anymore.
     */
    @Test
    @Order(13)
    void testRecoverRental_HardDeletedRental()
    {
        System.out.println("\n13: Testing RecoverRental method with a rental that was hard deleted...");

        try
        {
            // Create a new rental, hard delete it, and then try to undo a soft delete on it
            Rental rentalToRecover = RentalHandler.createNewRental(validUserIDs[0], validItemIDs[0]);
            RentalHandler.hardDeleteRental(rentalToRecover);

            // Attempt to undo a soft delete on the hard deleted rental
            Exception e = assertThrows(RecoveryException.class,
                    () -> RentalHandler.recoverRental(rentalToRecover),
                    "A RecoveryException should be thrown when attempting to undo a soft delete " +
                            "on a hard deleted rental.");
            assertTrue(e.getCause() instanceof EntityNotFoundException,
                    "The cause of the RecoveryException should be a EntityNotFoundException.");
        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException | DeletionException |
               InvalidTypeException e)
        {
            fail("Exception occurred during test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }
}