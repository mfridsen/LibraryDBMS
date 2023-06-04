package edu.groupeighteen.librarydbms.model.entities.rental;

import static org.junit.jupiter.api.Assertions.*;

import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidDateException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidNameException;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidTitleException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidLateFeeException;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 6/4/2023
 * @contact matfir-1@student.ltu.se
 * <p>
 * Unit Test for the RentalSetters class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RentalSettersTest
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
    void testRentalSetters()
    {
        System.out.println("\n1: Testing RentalSetters...");
        System.out.println("No test implemented here yet!");
        //TODO Write your code here
        System.out.println("\nTEST FINISHED.");
    }


    @Test
    @Order(6)
    void testSetRentalID() {
        System.out.println("\n6: Testing setRentalID...");

        try {
            Rental rental = new Rental(1, 1);
            assertThrows(InvalidIDException.class, () -> rental.setRentalID(-1));
            assertThrows(InvalidIDException.class, () -> rental.setRentalID(0));
            rental.setRentalID(1);
            assertEquals(1, rental.getRentalID());
        } catch (InvalidIDException | ConstructionException e) {
            fail("Valid tests should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(7)
    void testSetUserID() {
        System.out.println("\n7: Testing setUserID...");

        try {
            Rental rental = new Rental(1, 1);
            assertThrows(InvalidIDException.class, () -> rental.setUserID(-1));
            assertThrows(InvalidIDException.class, () -> rental.setUserID(0));
            rental.setUserID(1);
            assertEquals(1, rental.getUserID());
        } catch (InvalidIDException | ConstructionException e) {
            fail("Valid tests should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(8)
    void testSetItemID() {
        System.out.println("\n8: Testing setItemID...");

        try {
            Rental rental = new Rental(1, 1);
            assertThrows(InvalidIDException.class, () -> rental.setItemID(-1));
            assertThrows(InvalidIDException.class, () -> rental.setItemID(0));
            rental.setItemID(1);
            assertEquals(1, rental.getItemID());
        } catch (InvalidIDException | ConstructionException e) {
            fail("Valid tests should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(9)
    void testSetRentalDate() {
        System.out.println("\n9: Testing setRentalDate...");

        try {
            LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            Rental rental = new Rental(1, 1);
            assertThrows(InvalidDateException.class, () -> rental.setRentalDate(null));
            assertThrows(InvalidDateException.class, () -> rental.setRentalDate(LocalDateTime.now().plusSeconds(1)));
            rental.setRentalDate(now);
            //Assuming your test completes within a second, this should pass.
            assertEquals(now, rental.getRentalDate());
        } catch (InvalidDateException | ConstructionException e) {
            fail("Valid tests should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(10)
    void testSetUsername() {
        System.out.println("\n10: Testing setUsername...");

        try {
            Rental rental = new Rental(1, 1);
            assertThrows(InvalidNameException.class, () -> rental.setUsername(null));
            assertThrows(InvalidNameException.class, () -> rental.setUsername(""));
            rental.setUsername("testUser");
            assertEquals("testUser", rental.getUsername());
        } catch (InvalidNameException | ConstructionException e) {
            fail("Valid tests should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(11)
    void testSetItemTitle() {
        System.out.println("\n11: Testing setItemTitle...");

        try {
            Rental rental = new Rental(1, 1);
            assertThrows(InvalidTitleException.class, () -> rental.setItemTitle(null));
            assertThrows(InvalidTitleException.class, () -> rental.setItemTitle(""));
            rental.setItemTitle("testTitle");
            assertEquals("testTitle", rental.getItemTitle());
        } catch (InvalidTitleException | ConstructionException e) {
            fail("Valid tests should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(12)
    void testSetRentalDueDate() {
        System.out.println("\n12: Testing setRentalDueDate...");

        try {
            Rental rental = new Rental(1, 1);
            assertThrows(InvalidDateException.class, () -> rental.setRentalDueDate(null));
            assertThrows(InvalidDateException.class, () -> rental.setRentalDueDate(LocalDateTime.now().minusSeconds(1)));
            rental.setRentalDueDate(LocalDateTime.now().plusDays(1));
            assertEquals(LocalDateTime.now().plusDays(1).withHour(Rental.RENTAL_DUE_DATE_HOURS).withMinute(0).withSecond(0).truncatedTo(ChronoUnit.SECONDS), rental.getRentalDueDate());
        } catch (InvalidDateException | ConstructionException e) {
            fail("Valid tests should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(13)
    void testSetRentalReturnDate() {
        System.out.println("\n13: Testing setRentalReturnDate...");

        try {
            Rental rental = new Rental(1, 1);
            rental.setRentalDate(LocalDateTime.now().minusDays(1)); //Set RentalDate to make RentalReturnDate setting possible
            assertThrows(InvalidDateException.class, () -> rental.setRentalReturnDate(LocalDateTime.now().minusDays(2))); //Return date before RentalDate
            rental.setRentalReturnDate(LocalDateTime.now());
            assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), rental.getRentalReturnDate());
        } catch (InvalidDateException | ConstructionException e) {
            fail("Valid tests should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(14)
    void testSetLateFee() {
        System.out.println("\n14: Testing setLateFee...");

        try {
            Rental rental = new Rental(1, 1);
            assertThrows(InvalidLateFeeException.class, () -> rental.setLateFee(-0.01));
            rental.setLateFee(0.0);
            assertEquals(0.0, rental.getLateFee());
            rental.setLateFee(1.0);
            assertEquals(1.0, rental.getLateFee());
        } catch (InvalidLateFeeException | ConstructionException e) {
            fail("Valid tests should not throw exceptions.");
            e.printStackTrace();
        }

        System.out.println("\nTEST FINISHED.");
    }
}