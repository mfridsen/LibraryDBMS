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

    public static Rental createNewRental() {
        return null;
    }

    public static int saveRental(Rental rental) {
        return 0;
    }

    public static List<Rental> getAllRentals() {
        return null;
    }

    public static Rental getRentalByID(int rentalID) {
        return new Rental(1, 1, LocalDateTime.now());
    }

    public static List<Rental> getRentalsByRentalDate(LocalDateTime rentalDate) {
        return null;
    }

    public static List<Rental> getRentalsByUserID(int userID) {
        return null;
    }

    public static List<Rental> getRentalsByItemID(int itemID) {
        return null;
    }

    public boolean updateRental(Rental rental) {
        return false;
    }

    public boolean deleteRental(Rental rental) {
        return false;
    }
}