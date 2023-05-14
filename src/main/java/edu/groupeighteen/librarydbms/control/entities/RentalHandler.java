package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.entities.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.control.entities
 * @contact matfir-1@student.ltu.se
 * @date 5/5/2023
 *
 * This class contains database CRUD operation methods as well as other methods related to the Rental entity class.
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
            throw new IllegalArgumentException("Error creating new rental: Invalid userID or itemID. userID: " + userID + ", itemID: " + itemID);
        if (rentalDate == null)
            throw new IllegalArgumentException("Error creating new rental: rentalDate is null.");
        if (rentalDate.compareTo(LocalDateTime.now()) > 0)
            throw new IllegalArgumentException("Error creating new rental: rentalDate is in the future: " + rentalDate.toString());

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
     * This method attempts to insert a new rental into the 'rentals' table. It uses the userID, itemID and
     * rentalDate properties of the provided Rental object to populate the new record. The database is expected
     * to generate a unique ID for each new rental, which is retrieved and returned by this method.
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
    //to make error handling more consistent
    /**
     * Retrieves a Rental object from the database based on the provided rental ID.
     *
     * This method attempts to retrieve the rental details from the 'rentals' table in the database
     * that correspond to the provided rental ID. If a rental with the given ID exists, a new Rental object
     * is created with the retrieved data, and the rental's ID is set.
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
            //and set the rental's rentalID.
            if (resultSet.next()) {
                int userID = resultSet.getInt("userID");
                int itemID = resultSet.getInt("itemID");
                Timestamp timestamp = resultSet.getTimestamp("rentalDate");
                LocalDateTime rentalDate = timestamp.toLocalDateTime();  //Convert Timestamp to LocalDateTime
                Rental rental = new Rental(userID, itemID, rentalDate);
                rental.setRentalID(rentalID);
                return rental;
            }
        }
        //Return null if not found.
        return null;
    }

    //TODO-exception might want to throw a custom exception (like RentalNotFoundException) instead of returning null,
    //to make error handling more consistent
    /**
     * This method retrieves all rentals that have the specified rental date, creates a Rental object for each one,
     * and adds it to a list. The list of rentals is then returned. If no rentals with the specified date are found,
     * an empty list is returned.
     * 
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

        //Create a list to store the Rental objects.
        List<Rental> rentals = new ArrayList<>();

        //Execute the query and store the result in a ResultSet.
        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params)) {
            ResultSet resultSet = queryResult.getResultSet();

            //Loop through the ResultSet. For each record, create a new Rental object and add it to the list.
            while (resultSet.next()) {
                int rentalID = resultSet.getInt("rentalID");
                int userID = resultSet.getInt("userID");
                int itemID = resultSet.getInt("itemID");
                Rental rental = new Rental(userID, itemID, rentalDate);
                rental.setRentalID(rentalID);
                rentals.add(rental);
            }
        }
        //Return the list of rentals.
        return rentals;
    }

    //TODO-exception might want to throw a custom exception (like RentalNotFoundException) instead of returning null,
    //to make error handling more consistent
    /**
     * Retrieves all rentals that occurred on the specified rental day, creates a Rental object for each one,
     * and adds it to a list. The list of rentals is then returned. If no rentals on the specified day are found,
     * an empty list is returned.
     *
     * This method first truncates the rentalDay to the start of that day (00:00:00), then gets all rentals that
     * occurred from that time up to, but not including, the start of the next day. This means it retrieves all rentals
     * that occurred on that specific calendar day, regardless of the time of day they occurred.
     *
     * Note: The provided rentalDay LocalDateTime must not be in the future.
     *
     * @param rentalDay the day of the rentals, represented as a LocalDate.
     * @return The list of rentals if found, otherwise an empty list.
     * @throws SQLException If an error occurs while interacting with the database.
     * @throws IllegalArgumentException If the provided rentalDay is null or in the future.
     */
    public static List<Rental> getRentalsByRentalDay(LocalDate rentalDay) throws SQLException {
        //Validate the input
        if (rentalDay == null || rentalDay.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Invalid rentalDay: RentalDay cannot be null or in the future.");

        //Create startOfDay and startOfDayPlusOne from rentalDay
        LocalDateTime startOfDay = rentalDay.atStartOfDay().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime startOfDayPlusOne = startOfDay.plusDays(1);

        //Prepare a SQL query to select rentals by rentalDay.
        String query = "SELECT * FROM rentals WHERE rentalDate >= ? AND rentalDate < ?";
        String[] params =   {startOfDay.toString(),
                            startOfDayPlusOne.toString()};

        //Create an empty list to store the rentals.
        List<Rental> rentals = new ArrayList<>();

        //Execute the query and store the result in a ResultSet.
        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params)) {
            ResultSet resultSet = queryResult.getResultSet();

            //Loop through the ResultSet.
            while (resultSet.next()) {
                //For each row in the ResultSet, create a new Rental object and add it to the list.
                int rentalID = resultSet.getInt("rentalID");
                int userID = resultSet.getInt("userID");
                int itemID = resultSet.getInt("itemID");
                LocalDateTime rentalDate = resultSet.getTimestamp("rentalDate").toLocalDateTime();

                Rental rental = new Rental(userID, itemID, rentalDate);
                rental.setRentalID(rentalID);
                rentals.add(rental);
            }
        }
        //Return the list of rentals.
        return rentals;
    }

    //TODO-exception might want to throw a custom exception (like RentalNotFoundException) instead of returning null,
    //to make error handling more consistent
    /**
     * This method retrieves all rentals that fall within the specified start and end dates, inclusive. It creates a Rental object for each one,
     * and adds it to a list. The list of rentals is then returned. If no rentals within the specified time period are found,
     * an empty list is returned.
     *
     * The method will throw an IllegalArgumentException if any of the following conditions are met:
     * - The start date or end date is null.
     * - The start date or end date is in the future.
     * - The start date is after the end date.
     *
     * The start and end dates are inclusive. For example, if the start date is 2023-01-01 and the end date is 2023-01-31, rentals
     * from both the 1st and 31st of January 2023 will be included in the result.
     *
     * @param startDate the start date of the time period.
     * @param endDate the end date of the time period.
     * @return The list of rentals if found, otherwise an empty list.
     * @throws SQLException If an error occurs while interacting with the database.
     * @throws IllegalArgumentException If the input dates are invalid.
     */
    public static List<Rental> getRentalsByTimePeriod(LocalDate startDate, LocalDate endDate) throws SQLException {
        //Validate the inputs
        if (startDate == null ||  startDate.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Invalid dates: Start date cannot be null or in the future.");
        if (endDate == null || endDate.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Invalid dates: End date cannot be null or in the future.");
        if (startDate.isAfter(endDate))
            throw new IllegalArgumentException("Invalid dates: Start date must be before or equal to end date.");

        //Convert the LocalDate to LocalDateTime for database comparison
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atTime(23, 59, 59);

        //Prepare a SQL query to select rentals by rentalDate within a given period
        String query = "SELECT * FROM rentals WHERE rentalDate >= ? AND rentalDate <= ?";
        String[] params = {startDateTime.toString(), endDateTime.toString()};

        //Create an empty list to store the rentals
        List<Rental> rentals = new ArrayList<>();

        //Execute the query and store the result in a ResultSet
        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params)) {
            ResultSet resultSet = queryResult.getResultSet();

            //Loop through the ResultSet
            while (resultSet.next()) {
                //For each row in the ResultSet, create a new Rental object and add it to the list
                int rentalID = resultSet.getInt("rentalID");
                int userID = resultSet.getInt("userID");
                int itemID = resultSet.getInt("itemID");
                LocalDateTime rentalDate = resultSet.getTimestamp("rentalDate").toLocalDateTime();

                Rental rental = new Rental(userID, itemID, rentalDate);
                rental.setRentalID(rentalID);
                rentals.add(rental);
            }
        }
        //Return the list of rentals
        return rentals;
    }

    //TODO-exception might want to throw a custom exception (like RentalNotFoundException) instead of returning null,
    //to make error handling more consistent
    /**
     * This method retrieves all rentals that are associated with a specific user, creates a Rental object for each one,
     * and adds it to a list. The list of rentals is then returned. If no rentals are found for the specified user,
     * an empty list is returned.
     *
     * @param userID the ID of the user whose rentals are to be retrieved.
     * @return The list of rentals if found, otherwise an empty list.
     * @throws SQLException If an error occurs while interacting with the database.
     */
    public static List<Rental> getRentalsByUserID(int userID) throws SQLException {
        //Validate the input
        if (userID <= 0)
            throw new IllegalArgumentException("Invalid userID: " + userID + ". userID must be greater than 0.");

        //Prepare a SQL query to select rentals by userID
        String query = "SELECT * FROM rentals WHERE userID = ?";
        String[] params = {String.valueOf(userID)};

        //Create an empty list to store the rentals
        List<Rental> rentals = new ArrayList<>();

        //Execute the query and store the result in a ResultSet
        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params)) {
            ResultSet resultSet = queryResult.getResultSet();

            //Loop through the ResultSet
            while (resultSet.next()) {
                //For each row in the ResultSet, create a new Rental object and add it to the list
                int rentalID = resultSet.getInt("rentalID");
                int itemID = resultSet.getInt("itemID");
                LocalDateTime rentalDate = resultSet.getTimestamp("rentalDate").toLocalDateTime();

                Rental rental = new Rental(userID, itemID, rentalDate);
                rental.setRentalID(rentalID);
                rentals.add(rental);
            }
        }
        //Return the list of rentals
        return rentals;
    }

    //TODO-exception might want to throw a custom exception (like RentalNotFoundException) instead of returning null,
    //to make error handling more consistent
    /**
     * This method retrieves all rentals that have the specified item ID, creates a Rental object for each one,
     * and adds it to a list. The list of rentals is then returned. If no rentals with the specified item ID are found,
     * an empty list is returned.
     *
     * @param itemID the ID of the item.
     * @return The list of rentals if found, otherwise an empty list.
     * @throws SQLException If an error occurs while interacting with the database.
     * @throws IllegalArgumentException If the itemID is less than or equal to 0.
     */
    public static List<Rental> getRentalsByItemID(int itemID) throws SQLException {
        //Validate the input
        if (itemID <= 0)
            throw new IllegalArgumentException("Invalid itemID: " + itemID + ". itemID must be greater than 0.");

        //Prepare a SQL query to select rentals by itemID
        String query = "SELECT * FROM rentals WHERE itemID = ?";
        String[] params = {Integer.toString(itemID)};

        //Create an empty list to store the rentals
        List<Rental> rentals = new ArrayList<>();

        //Execute the query and store the result in a ResultSet
        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params)) {
            ResultSet resultSet = queryResult.getResultSet();

            //Loop through the ResultSet
            while (resultSet.next()) {
                //For each row in the ResultSet, create a new Rental object and add it to the list
                int rentalID = resultSet.getInt("rentalID");
                int userID = resultSet.getInt("userID");
                LocalDateTime rentalDate = resultSet.getTimestamp("rentalDate").toLocalDateTime();

                Rental rental = new Rental(userID, itemID, rentalDate);
                rental.setRentalID(rentalID);
                rentals.add(rental);
            }
        }
        //Return the list of rentals
        return rentals;
    }

    //TODO-exception might want to throw a custom exception (like RentalNotFoundException) instead of returning null,
    //to make error handling more consistent
    /**
     * This method updates the details of a given rental in the database.
     *
     * @param rental The rental object containing the updated details.
     * @return true if the update was successful, false otherwise.
     * @throws SQLException If an error occurs while interacting with the database.
     * @throws IllegalArgumentException If the rental object is null or the rentalID is not valid.
     */
    public boolean updateRental(Rental rental) throws SQLException {
        // Validate the input
        if (rental == null)
            throw new IllegalArgumentException("Invalid rental: Rental cannot be null.");
        if (rental.getRentalID() <= 0)
            throw new IllegalArgumentException("Invalid rental: Rental ID must be greater than 0.");

        // Prepare a SQL query to update the rental details
        String query = "UPDATE rentals SET userID = ?, itemID = ?, rentalDate = ? WHERE rentalID = ?";
        String[] params = {String.valueOf(rental.getUserID()),
                String.valueOf(rental.getItemID()),
                rental.getRentalDate().toString(),
                String.valueOf(rental.getRentalID())};

        // Execute the update and return whether it was successful
        int rowsAffected = DatabaseHandler.executePreparedUpdate(query, params);
        return rowsAffected > 0;
    }

    //TODO-exception might want to throw a custom exception (like RentalNotFoundException) instead of returning null,
    //to make error handling more consistent
    public boolean deleteRental(Rental rental) throws SQLException {
        return false;
    }
}