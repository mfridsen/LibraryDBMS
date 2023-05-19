package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.ItemNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.UserNotFoundException;

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
 *
 * The RentalHandler class provides methods to manage rental data in a database. It allows you to add,
 * retrieve, update, and delete rental records.
 *
 * The class provides the following methods:
 * - addRental(Rental rental): Adds a new rental to the database.
 * - getRentalByID(int rentalID): Retrieves a rental by its ID.
 * - getRentalsByUserID(int userID): Retrieves all rentals associated with a specific user ID.
 * - getRentalsByItemID(int itemID): Retrieves all rentals associated with a specific item ID.
 * - updateRental(Rental oldRental, Rental newRental): Updates the details of a rental in the database.
 * - deleteRental(Rental rental): Deletes a rental from the database.
 *
 * Additionally, the class provides the following private method:
 * - compareRentals(Rental oldRental, Rental newRental): Compares two Rental objects and validates/updates user and
 *   item data.
 *
 * The RentalHandler class uses the DatabaseHandler class to interact with the database and performs data validation
 * before executing database operations. It throws SQLException if an error occurs while interacting with the database
 * and IllegalArgumentException if the provided data is not valid.
 *
 * TODO: The current implementation returns null or false when a rental cannot be found. This might be changed to
 *  throw a custom exception (like RentalNotFoundException) to make error handling more consistent.
 */
public class RentalHandler {

    //TODO-prio UPDATE ALL METHODS AND TEST METHODS WHEN THE REST OF THE FIELDS ARE ADDED
    //TODO-prio ADD GET METHODS FOR ALL NEW FIELDS AS WELL AS TEST METHODS FOR THEM

    //TODO-test
    /**
     * Creates a new rental, saves it in the database, and returns it.
     *
     * This method validates the input user and item IDs, creates a new Rental object, 
     * sets its properties (including retrieving the user's username and item's title from 
     * their respective handlers), and saves it in the database using saveRental.
     *
     * The rental due date is calculated as the rental date (current date/time) plus the 
     * number of allowed rental days for the item.
     *
     * @param userID the ID of the user renting the item.
     * @param itemID the ID of the item being rented.
     * @return the newly created and saved Rental object.
     * @throws IllegalArgumentException if either userID or itemID is not a positive integer, 
     *                                  or if the number of allowed rental days is not a positive integer.
     * @throws SQLException if there is an error fetching the user or item from their handlers, 
     *                      or if there is an error saving the new rental in the database.
     */
    public static Rental createNewRental(int userID, int itemID) throws SQLException, UserNotFoundException, ItemNotFoundException {
        //Validate inputs
        if (userID <= 0 || itemID <= 0)
            throw new IllegalArgumentException("Error creating new rental: Invalid userID or itemID. userID: "
                    + userID + ", itemID: " + itemID);

        //Create and save the new rental
        Rental newRental = new Rental(userID, itemID);

        //Set username (ONLY IN OBJECT, NOT IN TABLE)
        User user = UserHandler.getUserByID(userID);
        if (user == null)
            throw new UserNotFoundException(userID);
        newRental.setUsername(user.getUsername());

        //Set title (ONLY IN OBJECT, NOT IN TABLE)
        Item item = ItemHandler.getItemByID(itemID);
        if (item == null)
            throw new ItemNotFoundException(itemID);
        newRental.setItemTitle(item.getTitle());

        //Obtain and validate allowedRentalDays
        int allowedRentalDays = ItemHandler.getAllowedRentalDaysByID(itemID);
        if (allowedRentalDays <= 0)
            throw new IllegalArgumentException("Error creating new rental: Invalid allowed rental days: " + allowedRentalDays);

        //Set due date to creation date + allowedRentalDays
        LocalDateTime dueDate = newRental.getRentalDate().plusDays(allowedRentalDays);
        newRental.setRentalDueDate(dueDate);

        //Save to database, retrieve and set rentalID, and return newRental
        newRental.setRentalID(saveRental(newRental));
        return newRental;
    }

    /**
     * Saves a new rental in the database.
     *
     * The method accepts a Rental object, validates it, and then attempts to insert it 
     * into the 'rentals' table in the database. The rental's user ID, item ID, rental date, 
     * rental due date, and late fee are required and must be set in the
     * Rental object before calling this method. The rental return date can be null, 
     * indicating that the item has not been returned yet.
     *
     * The method returns the ID of the newly inserted rental as generated by the database.
     *
     * @param rental The Rental object to be saved. It must have all required fields set.
     * @return The ID of the newly inserted rental as generated by the database.
     * @throws IllegalArgumentException if the provided Rental object is null.
     * @throws SQLException if there is an error executing the INSERT operation or if the 
     *                      newly inserted rental's ID could not be obtained.
     */
    private static int saveRental(Rental rental) throws SQLException {
        //Validate input
        if (rental == null)
            throw new IllegalArgumentException("Error saving rental: rental is null.");

        //Prepare query
        String query = "INSERT INTO rentals " +
                "(userID, itemID, rentalDate, rentalDueDate, rentalReturnDate, lateFee) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        //Set parameters for query
        //We check if rental.getRentalReturnDate() is null. If it is, we set the corresponding parameter to null.
        //If it's not, we convert it to a string using toString().
        //This should prevent any NullPointerExceptions when handling the return date.
        String[] params = {
                String.valueOf(rental.getUserID()),
                String.valueOf(rental.getItemID()),
                rental.getRentalDate().toString(),
                rental.getRentalDueDate().toString(),
                (rental.getRentalReturnDate() == null) ? null : rental.getRentalReturnDate().toString(),
                String.valueOf(rental.getLateFee())
        };

        //Execute query and get the generated rentalID, using try-with-resources
        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet generatedKeys = queryResult.getStatement().getGeneratedKeys();
            if (generatedKeys.next()) return generatedKeys.getInt(1);
            else throw new SQLException("Failed to insert the rental, no ID obtained.");
        }
    }

/*    *//**
     * This method retrieves all rentals from the database.
     *
     * It creates a SQL SELECT statement to retrieve all rentals from the 'rentals' table. It then executes this
     * statement using the DatabaseHandler's executeQuery method, which returns a ResultSet containing all rentals.
     * The method then loops through the ResultSet, converting each row into a Rental object and adding it to
     * an ArrayList. Finally, it returns this ArrayList.
     *
     * @return An ArrayList containing all rentals in the database, each represented by a Rental object.
     * @throws SQLException If an error occurs while interacting with the database.
     *//*
    public static List<Rental> getAllRentals() throws SQLException {
        //Prepare a SQL command to select all rentals from the 'rentals' table.
        String sql = "SELECT * FROM rentals";

        //Execute the query.
        QueryResult queryResult = DatabaseHandler.executeQuery(sql);

        //Retrieve the ResultSet from the QueryResult
        ResultSet resultSet = queryResult.getResultSet();

        //Convert the ResultSet into a List of Rental objects
        List<Rental> rentals = new ArrayList<>();
        while (resultSet.next()) {
            int rentalID = resultSet.getInt("rentalID");
            int userID = resultSet.getInt("userID");
            int itemID = resultSet.getInt("itemID");
            LocalDateTime rentalDate = resultSet.getTimestamp("rentalDate").toLocalDateTime();

            //Get user by ID
            User user = UserHandler.getUserByID(userID);
            if (user == null) {
                throw new SQLException("Error retrieving user from database by ID: username null.");
            }

            //Get item by ID
            Item item = ItemHandler.getItemByID(itemID);
            if (item == null) {
                throw new SQLException("Error retrieving item from database by ID: title null.");
            }

            Rental rental = new Rental(userID, itemID, rentalDate);
            rental.setRentalID(rentalID);
            rental.setUsername(user.getUsername());
            rental.setItemTitle(item.getTitle());

            rentals.add(rental);
        }

        //Close the QueryResult
        queryResult.close();

        //Return the List of rentals
        return rentals;
    }

    //TODO-prio update when Rental class is finished
    *//**
     * Prints all data of rentals in a list.
     * @param rentals the list of rentals.
     *//*
    public static void printRentalList(List<Rental> rentals) {
        System.out.println("Rentals:");
        int count = 1;
        for (Rental rental : rentals) {
            System.out.println(count + " rentalID: " + rental.getRentalID() + ", userID: " + rental.getUserID()
                    + ", username: " + rental.getUsername() + ", itemID: " + rental.getItemID()
                    + ", item title: " + rental.getItemTitle() + ", rental date: " + rental.getRentalDate());
            count++;
        }
    }

    //TODO-exception might want to throw a custom exception (like RentalNotFoundException) instead of returning null,
    //to make error handling more consistent
    *//**
     * Retrieves a Rental object from the database based on the provided rental ID.
     *
     * This method attempts to retrieve the rental details from the 'rentals' table in the database
     * that correspond to the provided rental ID. If a rental with the given ID exists, a new Rental object
     * is created with the retrieved data, and the rental's ID is set.
     *
     * @param rentalID The unique ID of the rental to be retrieved.
     * @return The Rental object corresponding to the provided ID, or null if no such rental is found.
     * @throws SQLException If an error occurs while interacting with the database.
     *//*
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

                //Get user by ID
                User user = UserHandler.getUserByID(userID);
                if (user == null) {
                    throw new SQLException("Error retrieving user from database by ID: username null.");
                }

                //Get item by ID
                Item item = ItemHandler.getItemByID(itemID);
                if (item == null) {
                    throw new SQLException("Error retrieving item from database by ID: title null.");
                }

                Rental rental = new Rental(userID, itemID, rentalDate);
                rental.setRentalID(rentalID);
                rental.setUsername(user.getUsername());
                rental.setItemTitle(item.getTitle());

                return rental;
            }
        }
        //Return null if not found.
        return null;
    }

    //TODO-exception might want to throw a custom exception (like RentalNotFoundException) instead of returning null,
    //to make error handling more consistent
    *//**
     * This method retrieves all rentals that have the specified rental date, creates a Rental object for each one,
     * and adds it to a list. The list of rentals is then returned. If no rentals with the specified date are found,
     * an empty list is returned.
     *
     * Usage: check if returned list is not empty.
     *
     * @param rentalDate the date of the rentals.
     * @return The list of rentals if found, otherwise an empty list.
     * @throws SQLException If an error occurs while interacting with the database.
     *//*
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

                //Get user by ID
                User user = UserHandler.getUserByID(userID);
                if (user == null) {
                    throw new SQLException("Error retrieving user from database by ID: username null.");
                }

                //Get item by ID
                Item item = ItemHandler.getItemByID(itemID);
                if (item == null) {
                    throw new SQLException("Error retrieving item from database by ID: title null.");
                }

                Rental rental = new Rental(userID, itemID, rentalDate);
                rental.setRentalID(rentalID);
                rental.setUsername(user.getUsername());
                rental.setItemTitle(item.getTitle());

                rentals.add(rental);
            }
        }
        //Return the list of rentals.
        return rentals;
    }

    //TODO-exception might want to throw a custom exception (like RentalNotFoundException) instead of returning null,
    //to make error handling more consistent
    *//**
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
     * Usage: check if returned list is not empty.
     *
     * @param rentalDay the day of the rentals, represented as a LocalDate.
     * @return The list of rentals if found, otherwise an empty list.
     * @throws SQLException If an error occurs while interacting with the database.
     * @throws IllegalArgumentException If the provided rentalDay is null or in the future.
     *//*
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

                //Get user by ID
                User user = UserHandler.getUserByID(userID);
                if (user == null) {
                    throw new SQLException("Error retrieving user from database by ID: username null.");
                }

                //Get item by ID
                Item item = ItemHandler.getItemByID(itemID);
                if (item == null) {
                    throw new SQLException("Error retrieving item from database by ID: title null.");
                }

                Rental rental = new Rental(userID, itemID, rentalDate);
                rental.setRentalID(rentalID);
                rental.setUsername(user.getUsername());
                rental.setItemTitle(item.getTitle());

                rentals.add(rental);
            }
        }
        //Return the list of rentals.
        return rentals;
    }

    //TODO-exception might want to throw a custom exception (like RentalNotFoundException) instead of returning null,
    //to make error handling more consistent
    *//**
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
     * Usage: check if returned list is not empty.
     *
     * @param startDate the start date of the time period.
     * @param endDate the end date of the time period.
     * @return The list of rentals if found, otherwise an empty list.
     * @throws SQLException If an error occurs while interacting with the database.
     * @throws IllegalArgumentException If the input dates are invalid.
     *//*
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

                //Get user by ID
                User user = UserHandler.getUserByID(userID);
                if (user == null) {
                    throw new SQLException("Error retrieving user from database by ID: username null.");
                }

                //Get item by ID
                Item item = ItemHandler.getItemByID(itemID);
                if (item == null) {
                    throw new SQLException("Error retrieving item from database by ID: title null.");
                }

                Rental rental = new Rental(userID, itemID, rentalDate);
                rental.setRentalID(rentalID);
                rental.setUsername(user.getUsername());
                rental.setItemTitle(item.getTitle());

                rentals.add(rental);
            }
        }
        //Return the list of rentals
        return rentals;
    }

    //TODO-exception might want to throw a custom exception (like RentalNotFoundException) instead of returning null,
    //to make error handling more consistent
    *//**
     * This method retrieves all rentals that are associated with a specific user, creates a Rental object for each one,
     * and adds it to a list. The list of rentals is then returned. If no rentals are found for the specified user,
     * an empty list is returned.
     *
     * Usage: check if returned list is not empty.
     *
     * @param userID the ID of the user whose rentals are to be retrieved.
     * @return The list of rentals if found, otherwise an empty list.
     * @throws SQLException If an error occurs while interacting with the database.
     *//*
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

                //Get user by ID
                User user = UserHandler.getUserByID(userID);
                if (user == null) {
                    throw new SQLException("Error retrieving user from database by ID: username null.");
                }

                //Get item by ID
                Item item = ItemHandler.getItemByID(itemID);
                if (item == null) {
                    throw new SQLException("Error retrieving item from database by ID: title null.");
                }

                Rental rental = new Rental(userID, itemID, rentalDate);
                rental.setRentalID(rentalID);
                rental.setUsername(user.getUsername());
                rental.setItemTitle(item.getTitle());

                rentals.add(rental);
            }
        }
        //Return the list of rentals
        return rentals;
    }

    //TODO-exception might want to throw a custom exception (like RentalNotFoundException) instead of returning null,
    //to make error handling more consistent
    *//**
     * This method retrieves all rentals that have the specified item ID, creates a Rental object for each one,
     * and adds it to a list. The list of rentals is then returned. If no rentals with the specified item ID are found,
     * an empty list is returned.
     *
     * Usage: check if returned list is not empty.
     *
     * @param itemID the ID of the item.
     * @return The list of rentals if found, otherwise an empty list.
     * @throws SQLException If an error occurs while interacting with the database.
     * @throws IllegalArgumentException If the itemID is less than or equal to 0.
     *//*
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

                //Get user by ID
                User user = UserHandler.getUserByID(userID);
                if (user == null) {
                    throw new SQLException("Error retrieving user from database by ID: username null.");
                }

                //Get item by ID
                Item item = ItemHandler.getItemByID(itemID);
                if (item == null) {
                    throw new SQLException("Error retrieving item from database by ID: title null.");
                }

                Rental rental = new Rental(userID, itemID, rentalDate);
                rental.setRentalID(rentalID);
                rental.setUsername(user.getUsername());
                rental.setItemTitle(item.getTitle());

                rentals.add(rental);
            }
        }
        //Return the list of rentals
        return rentals;
    }

    //TODO-exception might want to throw a custom exception (like RentalNotFoundException) instead of returning null,
    //to make error handling more consistent
    *//**
     * This method retrieves all rentals for the specified username, creates a Rental object for each one,
     * and adds it to a list. The list of rentals is then returned. If no rentals for the specified username are found,
     * an empty list is returned.
     *
     * Usage: check if returned list is not empty.
     *
     * @param username the username for which the rentals are to be retrieved.
     * @return The list of rentals if found, otherwise an empty list.
     * @throws SQLException If an error occurs while interacting with the database.
     * @throws IllegalArgumentException If the provided username is null or empty.
     *//*
    public static List<Rental> getRentalsByUsername(String username) throws SQLException {
        //Validate the input
        if (username == null || username.isEmpty())
            throw new IllegalArgumentException("Invalid username: username can't be null or empty.");

        //Prepare a SQL query to select rentals by username
        String query = "SELECT rentals.* FROM rentals INNER JOIN users ON rentals.userID = users.userID WHERE users.username = ?";
        String[] params = {username};

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

                //Get user by ID
                User user = UserHandler.getUserByID(userID);
                if (user == null) {
                    throw new SQLException("Error retrieving user from database by ID: username null.");
                }

                //Get item by ID
                Item item = ItemHandler.getItemByID(itemID);
                if (item == null) {
                    throw new SQLException("Error retrieving item from database by ID: title null.");
                }

                Rental rental = new Rental(userID, itemID, rentalDate);
                rental.setRentalID(rentalID);
                rental.setUsername(user.getUsername());
                rental.setItemTitle(item.getTitle());

                rentals.add(rental);
            }
        }
        //Return the list of rentals
        return rentals;
    }

    //TODO-exception might want to throw a custom exception (like RentalNotFoundException) instead of returning null,
    //to make error handling more consistent
    *//**
     * This method retrieves all rentals for the specified item title, creates a Rental object for each one,
     * and adds it to a list. The list of rentals is then returned. If no rentals for the specified item title are found,
     * an empty list is returned.
     *
     * Usage: check if returned list is not empty.
     *
     * @param title the title of the item for which the rentals are to be retrieved.
     * @return The list of rentals if found, otherwise an empty list.
     * @throws SQLException If an error occurs while interacting with the database.
     * @throws IllegalArgumentException If the provided title is null or empty.
     *//*
    public static List<Rental> getRentalsByItemTitle(String title) throws SQLException {
        //Validate the input
        if (title == null || title.isEmpty())
            throw new IllegalArgumentException("Invalid title: title can't be null or empty.");

        //Prepare a SQL query to select rentals by item title
        String query = "SELECT rentals.* FROM rentals INNER JOIN items ON rentals.itemID = items.itemID WHERE items.title = ?";
        String[] params = {title};

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

                //Get user by ID
                User user = UserHandler.getUserByID(userID);
                if (user == null) {
                    throw new SQLException("Error retrieving user from database by ID: username null.");
                }

                //Get item by ID
                Item item = ItemHandler.getItemByID(itemID);
                if (item == null) {
                    throw new SQLException("Error retrieving item from database by ID: title null.");
                }

                Rental rental = new Rental(userID, itemID, rentalDate);
                rental.setRentalID(rentalID);
                rental.setUsername(user.getUsername());
                rental.setItemTitle(item.getTitle());

                rentals.add(rental);
            }
        }
        //Return the list of rentals
        return rentals;
    }

    //TODO-exception might want to throw a custom exception (like RentalNotFoundException) instead of returning null,
    //to make error handling more consistent
    *//**
     * This method updates the details of a given rental in the database.
     *
     * @param newRental The rental object containing the updated details.
     * @return true if the update was successful, false otherwise.
     * @throws SQLException If an error occurs while interacting with the database.
     * @throws IllegalArgumentException If the rental object is null or the rentalID is not valid.
     *//*
    public static boolean updateRental(Rental oldRental, Rental newRental) throws SQLException {
        //Validate the input
        if (oldRental == null)
            throw new IllegalArgumentException("Error updating rental: oldRental is null.");
        if (newRental == null)
            throw new IllegalArgumentException("Error updating rental: newRental is null.");
        if (newRental.getRentalID() <= 0 || oldRental.getRentalID() <= 0)
            throw new IllegalArgumentException("Invalid rental: Rental ID must be greater than 0. " +
                    "oldRental ID: " + oldRental.getRentalID() + ", newRental ID: " + newRental.getRentalID());

        compareRentals(oldRental, newRental);

        //Prepare a SQL query to update the rental details
        String query = "UPDATE rentals SET userID = ?, itemID = ?, rentalDate = ?, username = ?, title = ? WHERE rentalID = ?";
        String[] params = {String.valueOf(newRental.getUserID()),
                String.valueOf(newRental.getItemID()),
                newRental.getRentalDate().toString(),
                newRental.getUsername(),
                newRental.getItemTitle(),
                String.valueOf(newRental.getRentalID())};

        //Execute the update and return whether it was successful
        int rowsAffected = DatabaseHandler.executePreparedUpdate(query, params);
        return rowsAffected > 0;
    }

    *//**
     * Compares two Rental objects, typically an old and a new version of the same rental, and validates/updates user and item data.
     * This method is used to ensure the consistency and correctness of user and item data when updating a rental.
     *
     * @param oldRental The original Rental object, typically fetched from the database.
     * @param newRental The updated Rental object, typically received from an update request.
     *
     * Functionality:
     * 1. If userIDs are different and usernames are the same, it updates newRental's username to match the username of the user associated with the new userID.
     * 2. If userIDs are the same and usernames are different, it updates newRental's userID to match the userID of the user associated with the new username.
     * 3. If both userIDs and usernames are different, it checks that they refer to the same user.
     * 4. If itemIDs are different and titles are the same, it updates newRental's title to match the title of the item associated with the new itemID.
     * 5. If itemIDs are the same and titles are different, it updates newRental's itemID to match the itemID of the item associated with the new title.
     * 6. If both itemIDs and titles are different, it checks that they refer to the same item.
     *
     * @throws SQLException If a user or item associated with the provided IDs cannot be fetched.
     * @throws IllegalArgumentException If the updated user or item data are inconsistent (usernames and userIDs or titles and itemIDs do not match).
     *//*
    private static void compareRentals(Rental oldRental, Rental newRental) throws SQLException {
        //1: If userIDs are different and usernames are the same, update newRental's username
        if (oldRental.getUserID() != newRental.getUserID() && oldRental.getUsername().equals(newRental.getUsername())) {
            User user = UserHandler.getUserByID(newRental.getUserID());
            if (user == null)
                throw new SQLException("compareRentals 1: fetched user is null.");
            newRental.setUsername(user.getUsername());
        }
        //2: If userIDs are the same and usernames are different, update newRental's userID
        if (oldRental.getUserID() == newRental.getUserID() && !oldRental.getUsername().equals(newRental.getUsername())) {
            User user = UserHandler.getUserByUsername(newRental.getUsername());
            if (user == null)
                throw new SQLException("compareRentals 2: fetched user is null.");
            newRental.setUserID(user.getUserID());
        }
        //3: If both userIDs and usernames are different, check that they refer to the same user
        if (oldRental.getUserID() != newRental.getUserID() && !oldRental.getUsername().equals(newRental.getUsername())) {
            User user = UserHandler.getUserByID(newRental.getUserID());
            if (user == null)
                throw new SQLException("compareRentals 3: fetched user is null.");

            if (!user.getUsername().equals(newRental.getUsername())) {
                throw new IllegalArgumentException("compareRentals 3: userID and username don't match");
            }
        }

        //4: If itemIDs are different and titles are the same, update newRental's title
        if (oldRental.getItemID() != newRental.getItemID() && oldRental.getItemTitle().equals(newRental.getItemTitle())) {
            Item item = ItemHandler.getItemByID(newRental.getItemID());
            if (item == null)
                throw new SQLException("compareRentals 4: fetched item is null.");
            newRental.setItemTitle(item.getTitle());
        }
        //5: If itemIDs are the same and titles are different, update newRental's itemID
        if (oldRental.getItemID() == newRental.getItemID() && !oldRental.getItemTitle().equals(newRental.getItemTitle())) {
            Item item = ItemHandler.getItemByTitle(newRental.getItemTitle());
            if (item == null)
                throw new SQLException("compareRentals 5: fetched item is null.");
            newRental.setItemID(item.getItemID());
        }
        //6: If both itemIDs and titles are different, check that they refer to the same item
        if (oldRental.getItemID() != newRental.getItemID() && !oldRental.getItemTitle().equals(newRental.getItemTitle())) {
            Item item = ItemHandler.getItemByID(newRental.getItemID());
            if (item == null)
                throw new SQLException("compareRentals 6: fetched item is null.");

            if (!item.getTitle().equals(newRental.getItemTitle())) {
                throw new IllegalArgumentException("compareRentals 6: itemID and title don't match");
            }
        }
    }

    //TODO-exception might want to throw a custom exception (like RentalNotFoundException) instead of returning null,
    //to make error handling more consistent
    *//**
     * This method deletes the details of a given rental in the database.
     *
     * @param rental The rental object containing the deleted details.
     * @return true if the delete was successful, false otherwise.
     * @throws SQLException If an error occurs while interacting with the database.
     * @throws IllegalArgumentException If the rental object is null or the rentalID is not valid.
     *//*
    public static boolean deleteRental(Rental rental) throws SQLException {
        //Validate the input
        if (rental == null)
            throw new IllegalArgumentException("Error deleting rental: rental is null.");
        if (rental.getRentalID() <= 0)
            throw new IllegalArgumentException("Invalid rental: Rental ID must be greater than 0.");

        //Prepare a SQL query to update the rental details
        String query = "DELETE FROM rentals WHERE rentalID = ?";
        String[] params = {String.valueOf(rental.getRentalID())};

        //Execute the update and return whether it was successful
        int rowsAffected = DatabaseHandler.executePreparedUpdate(query, params);
        return rowsAffected > 0;
    }*/
}