package edu.groupeighteen.librarydbms.control.entities.rental;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.control.entities.RentalHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.db.DatabaseConnection;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalNotAllowedException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

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
                LocalDateTime offsetDate = now.minusDays(i);

                String query = "UPDATE rentals SET rentalDate = ? WHERE rentalID = ?";
                String[] params = {
                        String.valueOf(offsetDate),
                        String.valueOf(rental.getRentalID())
                };

                DatabaseHandler.executePreparedUpdate(query, params);
            }

        }
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException e)
        {
            e.printStackTrace();
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
        catch (EntityNotFoundException | RentalNotAllowedException | InvalidIDException e)
        {
            e.printStackTrace();
        }
    }
}