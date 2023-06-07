package edu.groupeighteen.librarydbms.control.entities.rental;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.db.DatabaseConnection;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidTypeException;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalNotAllowedException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.control.entities.rental
 * @contact matfir-1@student.ltu.se
 * @date 6/4/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Brought to you by enough nicotine to kill a large horse.
 */
public abstract class BaseRentalHandlerTest
{
    protected static final String testDatabaseName = "test_database";
    protected static final int[] validUserIDs = new int[]{3, 4, 5, 6, 8, 10};
    protected static final int[] validItemIDs = new int[18];
    protected static Connection connection;

    static
    {
        for (int i = 0; i < validItemIDs.length; i++)
        {
            validItemIDs[i] = i + 3; // Initialize elements with numbers 3-20
        }
    }

    @BeforeAll
    protected static void setup()
    {
        System.out.println("\nSetting up test environment...");

        setupConnectionAndTables();
        setupTestData();
        ItemHandler.setup(); //Fills maps with items
        UserHandler.setup(); //Fills list with users
        DatabaseHandler.setVerbose(false); //Get that thing to shut up

        System.out.println("\nTest environment setup finished.");
    }


    protected static void setupConnectionAndTables()
    {
        System.out.println("\nSetting up connection and tables...");

        try
        {
            connection = DatabaseConnection.setup();
            DatabaseHandler.setConnection(connection);
            DatabaseHandler.setVerbose(true); //For testing we want DBHandler to be Verboten
            DatabaseHandler.executeCommand("drop database if exists " + testDatabaseName);
            DatabaseHandler.executeCommand("create database " + testDatabaseName);
            DatabaseHandler.executeCommand("use " + testDatabaseName);
            DatabaseHandler.setVerbose(false);
            DatabaseHandler.executeSQLCommandsFromFile("src/main/resources/sql/create_tables.sql");
        }
        catch (SQLException | ClassNotFoundException e)
        {
            System.err.println("RentalHandlerTestSuite failed while setting up connection and tables due to " +
                    e.getClass().getName() + " Message: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nConnection and tables setup finished.");
    }

    protected static void setupTestData()
    {
        System.out.println("\nFilling tables with test data...");

        DatabaseHandler.executeSQLCommandsFromFile("src/main/resources/sql/data/test_data.sql");
        DatabaseHandler.setVerbose(true);

        System.out.println("\nTest data setup finished.");
    }

    /**
     * Always close the connection to the database after use.
     */
    @AfterAll
    protected static void tearDown()
    {
        try
        {
            //Drop the test database
            DatabaseHandler.executeCommand("DROP DATABASE IF EXISTS " + testDatabaseName);

            //Close the database connection
            if (connection != null && !connection.isClosed())
            {
                DatabaseHandler.closeDatabaseConnection();
            }
        }
        catch (SQLException e)
        {
            System.err.println("An error occurred during cleanup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates a specific number of Rental instances and saves them to the database with different rental dates.
     * Each rental date is offset by an increasing number of days.
     *
     * @param numOfRentals  The number of Rental instances to be created.
     * @param offsetRentals The number of Rental instances whose rental dates should be offset.
     */
    protected static void createAndSaveRentalsWithDifferentDates(int numOfRentals, int offsetRentals)
    {
        try
        {
            LocalDateTime now = LocalDateTime.now();

            //Create numOfRentals number of rentals
            for (int i = 1; i <= numOfRentals; i++)
                RentalHandler.createNewRental(i, i);

            //Change rentalDates on desired amount of rentals
            for (int i = 1; i <= offsetRentals; i++)
            {
                Rental rental = RentalHandler.getRentalByID(i);
                assertNotNull(rental);

                //This will give different days to all the offset rentals
                LocalDateTime offsetRentalDate = now.minusDays(i);

                String query = "UPDATE rentals SET rentalDate = ? WHERE rentalID = ?";
                String[] params = {
                        String.valueOf(offsetRentalDate),
                        String.valueOf(rental.getRentalID())
                };

                DatabaseHandler.executePreparedUpdate(query, params);
            }

        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException | InvalidTypeException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Creates a specific number of Rental instances and saves them to the database with different rentalDates and
     * rentalDueDates.
     * Each rental date is offset by an increasing number of days, and the rentalDueDate is
     *
     * @param offsetRentals The number of Rental instances whose rental dates should be offset.
     */
    public static void createAndSaveRentalsWithDifferentDateAndDueDates(int offsetRentals)
    {
        LocalDateTime now = LocalDateTime.now();
        int numOfRentals = validItemIDs.length;

        //Create numOfRentals number of rentals
        for (int i = 0; i < numOfRentals; i++)
        {
            try
            {
                RentalHandler.createNewRental(validUserIDs[i % validUserIDs.length], validItemIDs[i]);
            }
            catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException | InvalidTypeException e)
            {
                System.err.println("Error creating rentals with different rentalDates and rentalDueDates: " +
                        e.getMessage());
                e.printStackTrace();
            }
        }

        //Change rentalDates on desired amount of rentals
        for (int i = 0; i <= offsetRentals; i++)
        {
            try
            {
                Rental rental = RentalHandler.getRentalByID(i + 1); // Rental IDs start from 1
                assertNotNull(rental);

                //This will give different days to all the offset rentals
                LocalDateTime offsetRentalDate = now.minusDays(i + 7);
                LocalDateTime offsetRentalDueDate = offsetRentalDate.plusDays(7);

                String query = "UPDATE rentals SET rentalDate = ?, rentalDueDate = ? WHERE rentalID = ?";
                String[] params = {
                        String.valueOf(offsetRentalDate),
                        String.valueOf(offsetRentalDueDate),
                        String.valueOf(rental.getRentalID())
                };

                DatabaseHandler.executePreparedUpdate(query, params);
            }
            catch (InvalidIDException e)
            {
                System.err.println("Error creating offset rentals with different rentalDates and rentalDueDates: " +
                        e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a specific number of Rental instances and saves them to the database with different rental dates.
     * Each rental date is offset by an increasing number of days.
     *
     * @param numOfRentals The number of Rental instances to be created.
     */
    protected static void createAndSaveRentalsWithDifferentTimes(int numOfRentals)
    {
        try
        {
            LocalDateTime now = LocalDateTime.now();

            // Create numOfRentals number of rentals
            for (int i = 1; i <= numOfRentals; i++)
            {
                RentalHandler.createNewRental(i, i);
            }

            // Change rentalTimes on each rental
            for (int i = 1; i <= numOfRentals; i++)
            {
                Rental rental = RentalHandler.getRentalByID(i);
                assertNotNull(rental);

                // This will give different times to all the rentals, by offsetting each one by an increasing number of hours and minutes
                LocalDateTime offsetTime = now.minusMinutes(i);

                String query = "UPDATE rentals SET rentalDate = ? WHERE rentalID = ?";
                String[] params = {
                        String.valueOf(offsetTime),
                        String.valueOf(rental.getRentalID())
                };

                DatabaseHandler.executePreparedUpdate(query, params);
            }

        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException | InvalidTypeException e)
        {
            e.printStackTrace();
        }
    }

    @AfterEach
    protected void resetItemsTable()
    {
        DatabaseHandler.executeCommand("DELETE FROM rentals");
        DatabaseHandler.executeCommand("ALTER TABLE rentals AUTO_INCREMENT = 1;");
        DatabaseHandler.executeCommand("DELETE FROM literature");
        DatabaseHandler.executeCommand("DELETE FROM films");
        DatabaseHandler.executeCommand("DELETE FROM items");
        DatabaseHandler.executeCommand("DELETE FROM users");
        DatabaseHandler.executeSQLCommandsFromFile("src/main/resources/sql/data/item_test_data.sql");
        DatabaseHandler.executeSQLCommandsFromFile("src/main/resources/sql/data/user_test_data.sql");
    }
}