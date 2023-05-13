package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.entities.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
        //TODO-prio add to these two when more fields are added, as well as javadoc
        //Validate inputs
        if (userID <= 0 || itemID <= 0)
            throw new IllegalArgumentException("Invalid userID or itemID. userID: " + userID + ", itemID: " + itemID);
        if (rentalDate == null || rentalDate.compareTo(LocalDateTime.now()) > 0)
            throw new IllegalArgumentException("Invalid rentalDate: " + rentalDate);

        //Round the rentalDate to seconds or else we get lots of annoying problems when inserting and retrieving
        rentalDate = rentalDate.truncatedTo(ChronoUnit.SECONDS);

        //Create and save the new rental
        Rental newRental = new Rental(userID, itemID, rentalDate);
        newRental.setRentalID(saveRental(newRental));

        //Set username
        User user = UserHandler.getUserByID(userID);
        if (user == null)
            throw new SQLException("Failed to find user with ID: " + userID);
        newRental.setUsername(user.getUsername());

        //Set title
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

        //Execute query and get the generated rentalID, using try-with-resources
        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet generatedKeys = queryResult.getStatement().getGeneratedKeys();
            if (generatedKeys.next()) return generatedKeys.getInt(1);
            else throw new SQLException("Failed to insert the rental, no ID obtained.");
        }
    }

    public static List<Rental> getAllRentals() throws SQLException {
        return null;
    }

    public static void printRentalList(List<Rental> rentals) {

    }

    //TODO-exception might want to throw a custom exception (like RentalNotFoundException) instead of returning null,
    // to make error handling more consistent
    /**
     * Retrieves a Rental object from the database based on the provided rental ID.
     *
     * <p>This method attempts to retrieve the rental details from the 'rentals' table in the database
     * that correspond to the provided rental ID. If a rental with the given ID exists, a new Rental object
     * is created with the retrieved data, and the rental's ID is set.</p>
     *
     * @param rentalID The unique ID of the rental to be retrieved.
     * @return The Rental object corresponding to the provided ID, or null if no such rental is found.
     * @throws SQLException If an error occurs while interacting with the database.
     */
    public static Rental getRentalByID(int rentalID) throws SQLException {
        //No point getting invalid rentals
        if (rentalID <= 0) throw new IllegalArgumentException("Invalid rentalID. rentalID: " + rentalID);

        //Prepare a SQL query to select a rental by rentalID.
        String query = "SELECT userID, itemID, rentalDate FROM rentals WHERE rentalID = ?";
        String[] params = {String.valueOf(rentalID)};

        //Execute the query and store the result in a ResultSet.
        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params)) {
            ResultSet resultSet = queryResult.getResultSet();
            //If the ResultSet contains data, create a new Rental object using the retrieved data
            // and set the rental's rentalID.
            if (resultSet.next()) {
                int userID = resultSet.getInt("userID");
                int itemID = resultSet.getInt("itemID");
                Timestamp timestamp = resultSet.getTimestamp("rentalDate");
                LocalDateTime rentalDate = timestamp.toLocalDateTime();  // Convert Timestamp to LocalDateTime
                Rental rental = new Rental(userID, itemID, rentalDate);
                rental.setRentalID(rentalID);
                return rental;
            }
        }
        //Return null if not found.
        return null;
    }

    //TODO-exception might want to throw a custom exception (like RentalNotFoundException) instead of returning null,
    // to make error handling more consistent
    /**
     * This method retrieves all rentals that have the specified rental date, creates a Rental object for each one,
     * and adds it to a list. The list of rentals is then returned. If no rentals with the specified date are found,
     * an empty list is returned.
     * @param rentalDate the date of the rentals.
     * @return The list of rentals if found, otherwise an empty list.
     * @throws SQLException If an error occurs while interacting with the database.
     */
    public static List<Rental> getRentalsByRentalDate(LocalDateTime rentalDate) throws SQLException {
        //No point getting invalid rentals
        if (rentalDate == null || rentalDate.compareTo(LocalDateTime.now()) > 0)
            throw new IllegalArgumentException("Invalid rentalDate: RentalDate cannot be null or in the future.");

        //Need to truncate to seconds
        rentalDate = rentalDate.truncatedTo(ChronoUnit.SECONDS);

        //Prepare a SQL query to select rentals by rentalDate.
        String query = "SELECT rentalID, userID, itemID FROM rentals WHERE rentalDate = ?";
        String[] params = {rentalDate.toString()};

        //Execute the query and store the result in a ResultSet.
        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params)) {
            ResultSet resultSet = queryResult.getResultSet();

            //Create a list to store the Rental objects.
            List<Rental> rentals = new ArrayList<>();

            //Loop through the ResultSet. For each record, create a new Rental object and add it to the list.
            while (resultSet.next()) {
                int rentalID = resultSet.getInt("rentalID");
                int userID = resultSet.getInt("userID");
                int itemID = resultSet.getInt("itemID");
                Rental rental = new Rental(userID, itemID, rentalDate);
                rental.setRentalID(rentalID);
                rentals.add(rental);
            }
            return rentals;
        }
    }

    public static List<Rental> getRentalsByRentalDay(LocalDate rentalDay) {
        //Gonna need some truncating at some point here
        return null;
    }

    public static List<Rental> getRentalsByTimePeriod(LocalDate startDate, LocalDate endDate) {
        //Gonna need some truncating at some point here
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