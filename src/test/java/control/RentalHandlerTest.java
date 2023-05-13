package control;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/12/2023
 * @contact matfir-1@student.ltu.se
 *
 * Unit Test for the RentalHandler class.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RentalHandlerTest extends BaseHandlerTest {

    @BeforeEach
    @Override
    void setupAndReset() {
        super.setupAndReset();
        setupTestTablesAndData();
    }

    @Test
    @Order(1)
    void testCreateNewRental() {
        System.out.println("\n1: Testing createNewRental method...");
        // Test the creation of a new Rental object
        // Remember, this method should not only create a new Rental object, but also insert it into the database
        System.out.println("Test finished.");
    }

    @Test
    @Order(2)
    void testSaveRental() {
        System.out.println("\n2: Testing saveRental method...");
        // Test the saving of a Rental object to the database
        // You should test both inserting a new Rental and updating an existing Rental
        System.out.println("Test finished.");
    }

    @Test
    @Order(3)
    void testGetAllRentals() {
        System.out.println("\n3: Testing getAllRentals method...");
        // Test the retrieval of all Rentals from the database
        System.out.println("Test finished.");
    }

    @Test
    @Order(4)
    void testGetRentalByID() {
        System.out.println("\n4: Testing getRentalByID method...");
        // Test the retrieval of a specific Rental by its rentalID
        System.out.println("Test finished.");
    }

    @Test
    @Order(5)
    void testGetRentalsByRentalDate() {
        System.out.println("\n5: Testing getRentalsByRentalDate method...");
        // Test the retrieval of Rentals by their rentalDate
        System.out.println("Test finished.");
    }

    @Test
    @Order(6)
    void testGetRentalsByUserID() {
        System.out.println("\n6: Testing getRentalsByUserID method...");
        // Test the retrieval of Rentals by their userID
        System.out.println("Test finished.");
    }

    @Test
    @Order(7)
    void testGetRentalsByItemID() {
        System.out.println("\n7: Testing getRentalsByItemID method...");
        // Test the retrieval of Rentals by their itemID
        System.out.println("Test finished.");
    }

    @Test
    @Order(8)
    void testUpdateRental() {
        System.out.println("\n8: Testing updateRental method...");
        // Test the updating of a specific Rental in the database
        System.out.println("Test finished.");
    }

    @Test
    @Order(9)
    void testDeleteRental() {
        System.out.println("\n9: Testing deleteRental method...");
        // Test the deletion of a specific Rental from the database
        System.out.println("Test finished.");
    }
}