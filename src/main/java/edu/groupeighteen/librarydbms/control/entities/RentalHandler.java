package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    /**
     * Creates a new Rental with the specified userID, itemID and rentalDate, and saves it to the database.
     * If the Rental creation fails, this method returns null.
     *
     * @param userID the ID of the User who rented an Item.
     * @param itemID the ID of the Item being rented by a User.
     * @param rentalDate the LocalDateTime that the Rental is created.
     * @return the created Rental object on success, null on failure.
     * @throws SQLException If an error occurs while interacting with the database.
     */
    public static Rental createNewRental(int userID, int itemID, LocalDateTime rentalDate) throws SQLException {
        //Validate inputs
        if (userID <= 0 || itemID <= 0)
            throw new IllegalArgumentException("Invalid userID or itemID. userID: " + userID + ", itemID: " + itemID);
        if (rentalDate == null || rentalDate.compareTo(LocalDateTime.now()) > 0)
            throw new IllegalArgumentException("Invalid rentalDate: " + rentalDate);
        
        //Create and save the new rental
        Rental newRental = new Rental(userID, itemID, rentalDate);
        newRental.setRentalID(saveRental(newRental));

        //Set username and title
        User user = UserHandler.getUserByID(userID);
        if (user == null)
            throw new SQLException("Failed to find user with ID: " + userID);
        newRental.setUsername(user.getUsername());

        Item item = ItemHandler.getItemByID(itemID);
        if (item == null)
            throw new SQLException("Failed to find item with ID: " + itemID);
        newRental.setTitle(item.getTitle());

        return newRental;
    }

    /**
     * Saves a Rental object to the database.
     *
     * <p>This method attempts to insert a new rental into the 'rentals' table. It uses the userID, itemID and
     * rentalDate properties of the provided Rental object to populate the new record. The database is expected
     * to generate a unique ID for each new rental, which is retrieved and returned by this method.</p>
     *
     * @param rental The Rental object to be saved. This object should have a userID, itemID and rentalDate set.
     * @return The unique ID generated by the database for the new rental record.
     * @throws SQLException If an error occurs while interacting with the database, or if the database does not
     *         generate a new unique ID for the inserted rental.
     */
    public static int saveRental(Rental rental) throws SQLException {
        //Prepare query
        String query = "INSERT INTO rentals (userID, itemID, rentalDate) VALUES (?, ?, ?)";
        String[] params = {String.valueOf(rental.getUserID()), String.valueOf(rental.getItemID()), rental.getRentalDate().toString()};

        //Execute query and get the generated userID, using try-with-resources
        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet generatedKeys = queryResult.getStatement().getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Failed to insert the rental, no ID obtained.");
            }
        }
    }

    public static List<Rental> getAllRentals() throws SQLException {
        return null;
    }

    public static void printRentalList(List<Rental> rentals) {

    }

    public static Rental getRentalByID(int rentalID) throws SQLException {
        return new Rental(1, 1, LocalDateTime.now());
    }

    public static List<Rental> getRentalsByRentalDate(LocalDateTime rentalDate) throws SQLException {
        return null;
    }

    public static List<Rental> getRentalsByRentalDay(LocalDate rentalDay) {
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