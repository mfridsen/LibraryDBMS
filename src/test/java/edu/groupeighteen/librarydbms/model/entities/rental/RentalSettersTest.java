package edu.groupeighteen.librarydbms.model.entities.rental;

import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidTitleException;
import edu.groupeighteen.librarydbms.model.exceptions.rental.InvalidReceiptException;
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
 * Unit Test for the RentalSetters class.
 * <p>
 * Brought to you by copious amounts of nicotine.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RentalSettersTest
{
    //TODO-future split into atomic tests
    //TODO-future beforeeach and aftereach rental creation

    @Test
    @Order(1)
    void testSetRentalID()
    {
        System.out.println("\n1: Testing setRentalID...");

        try
        {
            Rental rental = new Rental(1, 1);
            assertThrows(InvalidIDException.class, () -> rental.setRentalID(-1));
            assertThrows(InvalidIDException.class, () -> rental.setRentalID(0));
            rental.setRentalID(1);
            assertEquals(1, rental.getRentalID());
        }
        catch (InvalidIDException | ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid tests should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(2)
    void testSetUserID()
    {
        System.out.println("\n2: Testing setUserID...");

        try
        {
            Rental rental = new Rental(1, 1);
            assertThrows(InvalidIDException.class, () -> rental.setUserID(-1));
            assertThrows(InvalidIDException.class, () -> rental.setUserID(0));
            rental.setUserID(1);
            assertEquals(1, rental.getUserID());
        }
        catch (InvalidIDException | ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid tests should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(3)
    void testSetItemID()
    {
        System.out.println("\n3: Testing setItemID...");

        try
        {
            Rental rental = new Rental(1, 1);
            assertThrows(InvalidIDException.class, () -> rental.setItemID(-1));
            assertThrows(InvalidIDException.class, () -> rental.setItemID(0));
            rental.setItemID(1);
            assertEquals(1, rental.getItemID());
        }
        catch (InvalidIDException | ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid tests should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(4)
    void testSetRentalDate()
    {
        System.out.println("\n4: Testing setRentalDate...");

        try
        {
            LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            Rental rental = new Rental(1, 1);
            assertThrows(InvalidDateException.class, () -> rental.setRentalDate(null));
            assertThrows(InvalidDateException.class, () -> rental.setRentalDate(LocalDateTime.now().plusSeconds(1)));
            rental.setRentalDate(now);
            //Assuming your test completes within a second, this should pass.
            assertEquals(now, rental.getRentalDate());
        }
        catch (InvalidDateException | ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid tests should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(5)
    void testSetRentalDueDate()
    {
        System.out.println("\n5: Testing setRentalDueDate...");

        try
        {
            Rental rental = new Rental(1, 1);
            assertThrows(InvalidDateException.class, () -> rental.setRentalDueDate(null));
            assertThrows(InvalidDateException.class,
                    () -> rental.setRentalDueDate(LocalDateTime.now().minusSeconds(1)));
            rental.setRentalDueDate(LocalDateTime.now().plusDays(1));
            assertEquals(
                    LocalDateTime.now().plusDays(1).withHour(Rental.RENTAL_DUE_DATE_HOURS).withMinute(0).withSecond(
                            0).truncatedTo(ChronoUnit.SECONDS), rental.getRentalDueDate());
        }
        catch (InvalidDateException | ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid tests should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(6)
    void testSetUsername()
    {
        System.out.println("\n6: Testing setUsername...");

        try
        {
            Rental rental = new Rental(1, 1);
            assertThrows(InvalidNameException.class, () -> rental.setUsername(null));
            assertThrows(InvalidNameException.class, () -> rental.setUsername(""));
            rental.setUsername("testUser");
            assertEquals("testUser", rental.getUsername());
        }
        catch (InvalidNameException | ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid tests should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(7)
    void testSetItemTitle()
    {
        System.out.println("\n7: Testing setItemTitle...");

        try
        {
            Rental rental = new Rental(1, 1);
            assertThrows(InvalidTitleException.class, () -> rental.setItemTitle(null));
            assertThrows(InvalidTitleException.class, () -> rental.setItemTitle(""));
            rental.setItemTitle("testTitle");
            assertEquals("testTitle", rental.getItemTitle());
        }
        catch (InvalidTitleException | ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid tests should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(8)
    void testSetItemType()
    {
        System.out.println("\n8: Testing setItemType...");

        try
        {
            Rental rental = new Rental(1, 1);
            assertThrows(InvalidTypeException.class, () -> rental.setItemType(null));
            assertThrows(InvalidTypeException.class, () -> rental.setItemType(""));
            rental.setItemType("testType");
            assertEquals("testType", rental.getItemType());
        }
        catch (ConstructionException | InvalidTypeException e)
        {
            e.printStackTrace();
            fail("Valid tests should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(9)
    void testSetRentalReturnDate()
    {
        System.out.println("\n9: Testing setRentalReturnDate...");

        try
        {
            Rental rental = new Rental(1, 1);
            rental.setRentalDate(
                    LocalDateTime.now().minusDays(1)); //Set RentalDate to make RentalReturnDate setting possible
            assertThrows(InvalidDateException.class,
                    () -> rental.setRentalReturnDate(LocalDateTime.now().minusDays(2))); //Return date before RentalDate
            rental.setRentalReturnDate(LocalDateTime.now());
            assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), rental.getRentalReturnDate());
        }
        catch (InvalidDateException | ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid tests should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(10)
    void testSetLateFee()
    {
        System.out.println("\n10: Testing setLateFee...");

        try
        {
            Rental rental = new Rental(1, 1);
            assertThrows(InvalidLateFeeException.class, () -> rental.setLateFee(-0.01));
            rental.setLateFee(0.0);
            assertEquals(0.0, rental.getLateFee());
            rental.setLateFee(1.0);
            assertEquals(1.0, rental.getLateFee());
        }
        catch (InvalidLateFeeException | ConstructionException e)
        {
            e.printStackTrace();
            fail("Valid tests should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }

    @Test
    @Order(11)
    void testSetReceipt()
    {
        System.out.println("\n11: Testing setReceipt...");

        try
        {
            Rental rental = new Rental(1, 1);
            rental.setReceipt("testReceipt");
            assertEquals("testReceipt", rental.getReceipt());
        }
        catch (ConstructionException | InvalidReceiptException e)
        {
            e.printStackTrace();
            fail("Valid tests should not throw exceptions.");
        }

        System.out.println("\nTEST FINISHED.");
    }
}