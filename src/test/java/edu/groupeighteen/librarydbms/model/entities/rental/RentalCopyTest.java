package edu.groupeighteen.librarydbms.model.entities.rental;

import static org.junit.jupiter.api.Assertions.*;

import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

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
    @BeforeAll
    static void setUp()
    {
    }

    @AfterAll
    static void tearDown()
    {
    }

    /**
     *
     */
    @Test
    @Order(1)
    void testRentalCopy()
    {
        System.out.println("\n1: Testing RentalCopy...");
        System.out.println("No test implemented here yet!");
        //TODO Write your code here
        System.out.println("\nTEST FINISHED.");
    }

    /**
     * This test method verifies the correct functionality of the copy constructor of the Rental class.
     * The copy constructor should create a new Rental instance with the same property values as the original.
     * We test it by creating a new Rental object, then using the copy constructor to make a copy,
     * and finally verifying that all properties in the copied Rental are identical to the original.
     */
    @Test
    @Order(5)
    void testRentalConstructor_CopyRental() {
        System.out.println("\n5: Testing Rental copy constructor...");

        try {
            //Create a rental object
            LocalDateTime now = LocalDateTime.now();
            Rental originalRental = new Rental(1, 2, 3, now, "username", "itemTitle", now.plusDays(7), now.plusDays(5), 0.0, false);

            //Use the copy constructor
            Rental copyRental = new Rental(originalRental);

            //Check that all fields are identical
            assertEquals(originalRental.getRentalID(), copyRental.getRentalID());
            assertEquals(originalRental.getUserID(), copyRental.getUserID());
            assertEquals(originalRental.getItemID(), copyRental.getItemID());
            assertEquals(originalRental.getRentalDate(), copyRental.getRentalDate());
            assertEquals(originalRental.getUsername(), copyRental.getUsername());
            assertEquals(originalRental.getItemTitle(), copyRental.getItemTitle());
            assertEquals(originalRental.getRentalDueDate(), copyRental.getRentalDueDate());
            assertEquals(originalRental.getRentalReturnDate(), copyRental.getRentalReturnDate());
            assertEquals(originalRental.getLateFee(), copyRental.getLateFee());
        } catch (ConstructionException e) {
            fail("Valid tests should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }
}