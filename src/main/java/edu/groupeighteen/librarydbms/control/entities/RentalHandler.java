package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.*;

import java.sql.*;
import java.time.LocalDateTime;
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
 * Note on Exceptions:
 *
 * "Exceptions should only be thrown in exceptional circumstances, and invalid user input is not exceptional".
 *
 * I've battled this one for long, but if finally clicked. This class is NOT handling user input. That is going
 * to be handled in RentalCreateGUI. When I press the "Create Rental" button in that class, we perform an instant
 * check on whether the title, and any other needed fields, are empty.
 *
 * If so, we print an error message, reset all fields in the GUI and wait for new input.
 *
 * Meaning, createNewRental (as an example) should NEVER be called with an empty or null String as argument.
 * If it is, that IS exceptional.
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
     */
    public static Rental createNewRental(int userID, int itemID) throws UserNotFoundException, ItemNotFoundException, RentalNotAllowedException, InvalidIDException {
        //Check IDs, can throw InvalidIDException
        if (checkUserID(userID))
            throw new InvalidIDException("Rental creation failed: invalid userID " + userID);
        if (checkItemID(itemID))
            throw new InvalidIDException("Rental creation failed: invalid itemID " + itemID);

        String username = ""; //Create username here so catch block is happy
        String title = ""; //Create title here so catch block is happy

        try {
            //Retrieve user, throws UserNotFoundException if not found
            User user = getExistingUser(userID);
            username = user.getUsername();

            //Validate that user is allowed to rent, throws RentalNotAllowedException if not
            validateUserAllowedToRent(user);

            //Retrieve item, throws ItemNotFoundException if not found
            Item item = getExistingItem(itemID);
            title = item.getTitle();

            //Check if there is an available copy of the item, if not, try to find one
            if (!item.isAvailable()) {
                item = getAvailableCopy(title); //Throws ItemNotFoundException if not found
                itemID = item.getItemID(); //Update itemID to ensure correct itemID is used to create Rental
            }

            //Create rental
            Rental newRental = new Rental(userID, itemID);

            //Set rental fields except rentalID
            newRental.setUsername(user.getUsername());
            newRental.setItemTitle(item.getTitle());

            //Due date
            int allowedRentalDays = ItemHandler.getAllowedRentalDaysByID(itemID);
            LocalDateTime dueDate = newRental.getRentalDate().plusDays(allowedRentalDays);
            newRental.setRentalDueDate(dueDate);

            //Save rental
            int rentalID = saveRental(newRental);
            newRental.setRentalID(rentalID);

            //Update Item to change its status to not available
            //Also make sure to update the Maps in ItemHandler, this should be done automatically but verify this
            item.setAvailable(false);
            ItemHandler.updateItem(item);

            //Update User to increment number of current rentals
            user.setCurrentRentals(user.getCurrentRentals() + 1);
            UserHandler.updateUser(user);

            //Return rental
            return newRental;

        } catch (InvalidIDException e) {
            ExceptionHandler.HandleFatalException("Rental creation failed: InvalidIDException thrown for valid ID, message: "
                    + e.getMessage(), e);
        } catch (InvalidTitleException e) {
            ExceptionHandler.HandleFatalException("Rental creation failed: InvalidTitleException thrown for valid title "
                    + title + ", message: " + e.getMessage(), e);
        } catch (InvalidUsernameException e) {
            ExceptionHandler.HandleFatalException("Rental creation failed: InvalidUsernameException thrown for valid username "
                    + username + ", message: " + e.getMessage(), e);
        } catch (ConstructionException e) {
            ExceptionHandler.HandleFatalException("Rental construction failed due to " + e.getCause().getClass().getName(), e.getCause());
        } catch (InvalidDateException e) {
            ExceptionHandler.HandleFatalException("Rental creation failed due to InvalidDateException: " + e.getMessage(), e);
        } catch (InvalidRentalException e) {
            ExceptionHandler.HandleFatalException("Rental creation failed due to InvalidRentalException: " + e.getMessage(), e);
        } catch (NullItemException e) {
            ExceptionHandler.HandleFatalException("Rental creation failed: NullItemException thrown for non-null Item, message: "
                    + e.getMessage(), e);
        } catch (NullUserException e) {
            ExceptionHandler.HandleFatalException("Rental creation failed: NullUserException thrown for non-null User, message: "
                    + e.getMessage(), e);
        }

        //Won't reach, needed for compilation
        return null;
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
     */
    private static int saveRental(Rental rental) {

        try {
            //Validate input
            checkNullRental(rental);

            //Prepare query
            String query = "INSERT INTO rentals " +
                    "(userID, itemID, rentalDate, rentalDueDate, rentalReturnDate, lateFee, deleted) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

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
                    String.valueOf(rental.getLateFee()),
                    "0" //Not deleted by default
            };

            //Execute query and get the generated rentalID, using try-with-resources
            try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params, Statement.RETURN_GENERATED_KEYS)) {
                ResultSet generatedKeys = queryResult.getStatement().getGeneratedKeys();
                if (generatedKeys.next()) return generatedKeys.getInt(1);
            }
        } catch (NullRentalException | SQLException e) {
            ExceptionHandler.HandleFatalException("Failed to save Rental due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        //Cannot be reached, but needed for compilation
        return 0;
    }


    public static List<Rental> getRentals(String sqlSuffix) {
        //Convert the ResultSet into a List of Rental objects
        List<Rental> rentals = new ArrayList<>();

        //Prepare a SQL command to select all rentals from the 'rentals' table with given sqlSuffix
        String sql = "SELECT * FROM rentals " + (sqlSuffix == null ? "" : sqlSuffix);

        try {
            //Execute the query.
            try(QueryResult queryResult = DatabaseHandler.executeQuery(sql)) {

                //Retrieve the ResultSet from the QueryResult
                ResultSet resultSet = queryResult.getResultSet();


                while (resultSet.next()) {
                    int rentalID = resultSet.getInt("rentalID");

                    //Get user by ID, throws UserNotFoundException
                    int userID = resultSet.getInt("userID");
                    User user = UserHandler.getUserByID(userID);
                    if (user == null) throw new NullUserException("Rental retrieval failed: NullUserException " +
                            "thrown for valid user with ID " + userID);
                    String username = user.getUsername();

                    //Get item by ID
                    int itemID = resultSet.getInt("itemID");
                    Item item = ItemHandler.getItemByID(itemID);
                    if (item == null) throw new NullItemException("Rental retrieval failed: NullItemException " +
                            "thrown for valid item with ID " + itemID);
                    String itemTitle = item.getTitle();

                    LocalDateTime rentalDate = resultSet.getTimestamp("rentalDate").toLocalDateTime();
                    LocalDateTime rentalDueDate = resultSet.getTimestamp("rentalDueDate").toLocalDateTime();

                    //Rental return date can be null in the table
                    Timestamp returnDateTimestamp = resultSet.getTimestamp("rentalReturnDate");
                    LocalDateTime rentalReturnDate = null; // Set to null by default
                    if (returnDateTimestamp != null) {
                        rentalReturnDate = returnDateTimestamp.toLocalDateTime();
                    }

                    float lateFee = resultSet.getFloat("lateFee");
                    boolean deleted = resultSet.getBoolean("deleted");

                    Rental rental = new Rental(rentalID, userID, itemID, rentalDate, username, itemTitle,
                            rentalDueDate, rentalReturnDate, lateFee, deleted);
                    rental.setRentalID(rentalID);
                    rental.setRentalDate(rentalDate);
                    rental.setUsername(user.getUsername());
                    rental.setItemTitle(item.getTitle());
                    rental.setRentalDueDate(rentalDueDate);
                    rental.setRentalReturnDate(rentalReturnDate);
                    rental.setLateFee(lateFee);

                    //Add to list
                    rentals.add(rental);
                }
            }
        } catch (SQLException | ConstructionException | InvalidIDException | InvalidDateException |
                InvalidUsernameException | InvalidTitleException | InvalidLateFeeException | NullUserException |
                NullItemException e) {
            ExceptionHandler.HandleFatalException("Failed to retrieve all rentals from database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        //Return the List of rentals
        return rentals;
    }

    /**
     * This method fetches a rental by its rental ID from the database.
     *
     * @param rentalID The ID of the rental to fetch. This should be a valid, existing rental ID.
     * @return A Rental object corresponding to the rental with the specified ID if it exists,
     *      or null if no such rental exists.
     * @throws InvalidIDException If the provided rental ID is invalid (i.e., less than or equal to zero).
     * @throws RentalException If more than one rental with the specified ID exists, which should not happen because
     *      rental IDs are unique.
     */
    public static Rental getRentalByID(int rentalID) throws InvalidIDException, RentalException {
        checkRentalID(rentalID);
        String suffix = "WHERE rentalID = " + rentalID;
        List<Rental> rentals = getRentals(suffix);
        if (rentals.size() > 1)
            throw new RentalException("There should not be more than 1 rental with ID " + rentalID
                    + ", received: " + rentals.size());
        else if (rentals.size() == 1) return rentals.get(0);
        else return null;
    }




/*
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
     //* @param rental The rental object containing the deleted details.
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

    // UTILITY STUFF --------------------------------------------------------------------------------------------------

    private static boolean checkUserID(int userID) {
        return userID <= 0;
    }

    private static boolean checkItemID(int itemID) {
        return itemID <= 0;
    }

    private static void checkNullRental(Rental rental) throws NullRentalException {
        if (rental == null)
            throw new NullRentalException("Error saving rental: rental is null.");
    }

    private static User getExistingUser(int userID) throws UserNotFoundException, InvalidIDException {
        User user = UserHandler.getUserByID(userID);
        if (user == null)
            throw new UserNotFoundException("User with ID " + userID + " not found.");
        return user;
    }

    private static void validateUserAllowedToRent(User user) throws RentalNotAllowedException {
        //User has too many current rentals
        if (user.getCurrentRentals() >= user.getAllowedRentals())
            throw new RentalNotAllowedException("User not allowed to rent due to already renting to capacity. " +
                    "Current rentals: " + user.getCurrentRentals() + ", allowed rentals: " + user.getAllowedRentals());
        //User has late fee
        if (user.getLateFee() > 0)
            throw new RentalNotAllowedException("User not allowed to rent due to having a late fee. " +
                    "Late fee: " + user.getLateFee());
    }

    private static Item getExistingItem(int itemID) throws ItemNotFoundException, InvalidIDException {
        Item item = ItemHandler.getItemByID(itemID);
        if (item == null)
            throw new ItemNotFoundException("Item with ID " + itemID + " not found.");
        return item;
    }

    private static Item getAvailableCopy(String title) throws InvalidTitleException, ItemNotFoundException {
        List<Item> items = ItemHandler.getItemsByTitle(title);
        Item item = null;
        for (Item availableItem : items) {
            if (availableItem.isAvailable())
                item = availableItem;
        }
        if (item == null) throw new ItemNotFoundException("Rental creation failed: No available copy of " + title + " found.");
        return item;
    }

    private static void checkRentalID(int rentalID) throws InvalidIDException {
        if (rentalID <= 0) throw new InvalidIDException("Invalid rentalID. rentalID: " + rentalID);
    }

    //TODO-prio update when Rental class is finished
    /**
     * Prints all data of rentals in a list.
     * @param rentals the list of rentals.
     */
    public static void printRentalList(List<Rental> rentals) {
        System.out.println("Rentals:");
        int count = 1;
        for (Rental rental : rentals) {
            System.out.println(count + " rentalID: " + rental.getRentalID() + ", userID: " + rental.getUserID()
                    + ", username: " + rental.getUsername() + ", itemID: " + rental.getItemID()
                    + ", item title: " + rental.getItemTitle() + ", rental date: " + rental.getRentalDate()
                    + ", rental due date: " + rental.getRentalDueDate()
                    + ", rental return date: " + rental.getRentalReturnDate() + ", late fee: " + rental.getLateFee());
            count++;
        }
    }
}