package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.model.entities.Rental;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.control.entities
 * @contact matfir-1@student.ltu.se
 * @date 5/5/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 */
public class RentalHandler {

    public static Rental createNewRental(int userID, int itemID, LocalDateTime rentalDate) throws SQLException {
        //No need to proceed with invalid data
        if (userID <= 0 || itemID <= 0) {
            System.err.println("Error creating a new rental: invalid userID or itemID.");
            return null;
        }

        if (rentalDate == null || rentalDate.compareTo(LocalDateTime.now()) > 0) {
            System.err.println("Error creating a new rental: invalid rentalDate.");
            return null;
        }

        return null;
    }

    public static int saveRental(Rental rental) throws SQLException {
        return 0;
    }

    public static List<Rental> getAllRentals() throws SQLException {
        return null;
    }

    public static Rental getRentalByID(int rentalID) throws SQLException {
        return new Rental(1, 1, LocalDateTime.now());
    }

    public static List<Rental> getRentalsByRentalDate(LocalDateTime rentalDate) throws SQLException {
        return null;
    }

    public static List<Rental> getRentalsByUserID(int userID) throws SQLException {
        return null;
    }

    public static List<Rental> getRentalsByItemID(int itemID) throws SQLException {
        return null;
    }

    public boolean updateRental(Rental rental) throws SQLException {
        return false;
    }

    public boolean deleteRental(Rental rental) throws SQLException {
        return false;
    }
}