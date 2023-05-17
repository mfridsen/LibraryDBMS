package model.entities;

import edu.groupeighteen.librarydbms.model.entities.Rental;
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

    @Test
    @Order(1)
    void testConstructor() {
        System.out.println("\n1: Testing Rental Constructor...");

        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Rental rental = new Rental(1, 1, now);

        assertNotNull(rental);
        assertEquals(1, rental.getUserID());
        assertEquals(1, rental.getItemID());
        assertEquals(now, rental.getRentalDate());

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(2)
    void testSetRentalID() {
        System.out.println("\n2: Testing setRentalID method...");

        Rental rental = new Rental(1, 1, LocalDateTime.now());

        assertThrows(IllegalArgumentException.class, () -> rental.setRentalID(0));
        assertThrows(IllegalArgumentException.class, () -> rental.setRentalID(-1));

        rental.setRentalID(1);
        assertEquals(1, rental.getRentalID());

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(3)
    void testSetUserID() {
        System.out.println("\n3: Testing setUserID method...");

        Rental rental = new Rental(1, 1, LocalDateTime.now());

        assertThrows(IllegalArgumentException.class, () -> rental.setUserID(0));
        assertThrows(IllegalArgumentException.class, () -> rental.setUserID(-1));

        rental.setUserID(2);
        assertEquals(2, rental.getUserID());

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(4)
    void testSetItemID() {
        System.out.println("\n4: Testing setItemID method...");

        Rental rental = new Rental(1, 1, LocalDateTime.now());

        assertThrows(IllegalArgumentException.class, () -> rental.setItemID(0));
        assertThrows(IllegalArgumentException.class, () -> rental.setItemID(-1));

        rental.setItemID(2);
        assertEquals(2, rental.getItemID());

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(5)
    void testSetUsername() {
        System.out.println("\n5: Testing setUsername method...");

        Rental rental = new Rental(1, 1, LocalDateTime.now());

        assertThrows(IllegalArgumentException.class, () -> {
            rental.setUsername(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            rental.setUsername("");
        });

        rental.setUsername("new_username");
        assertEquals("new_username", rental.getUsername());

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(6)
    void testSetTitle() {
        System.out.println("\n6: Testing setTitle method...");

        Rental rental = new Rental(1, 1, LocalDateTime.now());

        //Test valid setTitle
        rental.setTitle("Harry Potter 2");
        assertEquals("Harry Potter 2", rental.getTitle());

        //Test invalid setTitle
        assertThrows(IllegalArgumentException.class, () -> rental.setTitle(null));
        assertThrows(IllegalArgumentException.class, () -> rental.setTitle(""));

        System.out.println("\nTEST FINISHED.");
    }
}