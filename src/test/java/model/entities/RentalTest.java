package model.entities;

import edu.groupeighteen.librarydbms.model.entities.Rental;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 5/5/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Unit Test for the Rental class.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RentalTest {
    /**
     * Tests the getter and setter methods to ensure that they correctly get and set the username
     * and password fields.
     */
    @Test
    @Order(1)
    void testRentalGettersAndSetters() {
        System.out.println("\nTesting Rental class...");
        // Arrange
        int expectedRentalID = 1;
        int expectedUserID = 1;
        int expectedItemID = 1;
        LocalDateTime expectedRentalDate = LocalDateTime.now();
        LocalDateTime secondRentalDate = LocalDateTime.now().minusDays(1);

        // Act
        Rental rental = new Rental(expectedRentalDate);
        rental.setRentalID(expectedRentalID);
        rental.setUserID(expectedUserID);
        rental.setItemID(expectedItemID);

        // Assert
        assertEquals(expectedRentalID, rental.getRentalID(), "RentalID does not match the expected value");
        assertEquals(expectedUserID, rental.getUserID(), "UserID does not match the expected value");
        assertEquals(expectedItemID, rental.getItemID(), "ItemID does not match the expected value");
        assertEquals(expectedRentalDate, rental.getRentalDate(), "RentalDate does not match the expected value");

        rental.setRentalDate(secondRentalDate);
        assertEquals(secondRentalDate, rental.getRentalDate(), "RentalDate does not match the expected value");

        System.out.println("Test finished!");
    }
}