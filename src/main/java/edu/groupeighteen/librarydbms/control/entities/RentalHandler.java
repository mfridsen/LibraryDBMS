package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidTitleException;
import edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.NullEntityException;
import edu.groupeighteen.librarydbms.model.exceptions.rental.*;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidLateFeeException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidNameException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidUserRentalsException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
 * The RentalHandler class provides methods to manage rental data in a database. It allows you to add,
 * retrieve, update, and delete rental records.
 * <p>
 * The class provides the following methods:
 * - addRental(Rental rental): Adds a new rental to the database.
 * - getRentalByID(int rentalID): Retrieves a rental by its ID.
 * - getRentalsByUserID(int userID): Retrieves all rentals associated with a specific user ID.
 * - getRentalsByItemID(int itemID): Retrieves all rentals associated with a specific item ID.
 * - updateRental(Rental oldRental, Rental newRental): Updates the details of a rental in the database.
 * - deleteRental(Rental rental): Deletes a rental from the database.
 * <p>
 * Additionally, the class provides the following private method:
 * - compareRentals(Rental oldRental, Rental newRental): Compares two Rental objects and validates/updates user and
 * item data.
 * <p>
 * The RentalHandler class uses the DatabaseHandler class to interact with the database and performs data validation
 * before executing database operations. It throws SQLException if an error occurs while interacting with the database
 * and IllegalArgumentException if the provided data is not valid.
 * <p>
 * Note on Exceptions:
 * <p>
 * "Exceptions should only be thrown in exceptional circumstances, and invalid user input is not exceptional".
 * <p>
 * I've battled this one for long, but if finally clicked. This class is NOT handling user input. That is going
 * to be handled in RentalCreateGUI. When I press the "Create Rental" button in that class, we perform an instant
 * check on whether the title, and any other needed fields, are empty.
 * <p>
 * If so, we print an error message, reset all fields in the GUI and wait for new input.
 * <p>
 * Meaning, createNewRental (as an example) should NEVER be called with an empty or null String as argument.
 * If it is, that IS exceptional.
 */
public class RentalHandler
{

    private static boolean verbose = false;

    public static boolean isVerbose()
    {
        return verbose;
    }

    public static void setVerbose(boolean verbose)
    {
        RentalHandler.verbose = verbose;
    }

    /**
     * This method creates a new rental in the system.
     * <p>
     * The method will first validate the IDs and ensure the user is allowed to rent.
     * Then, it will create a new Rental object and fill it with the appropriate information.
     * This rental will then be saved to the system, and the item's availability and the user's rental count will be
     * updated accordingly.
     * <p>
     * In case of a failure during the rental creation process, the error is logged as a fatal exception.
     *
     * @param userID The ID of the user who is creating the rental.
     * @param itemID The ID of the item being rented.
     * @return A Rental object representing the new rental that was created.
     * @throws EntityNotFoundException   if the user with the given ID does not exist in the system.
     *                                   if the item with the given ID does not exist in the system, or if there is no
     *                                   available copy of the item.
     * @throws RentalNotAllowedException if the user is not allowed to rent an item (e.g. due to reaching the limit
     *                                   of simultaneous rentals).
     * @throws InvalidIDException        if either the userID or itemID is invalid.
     */

    public static Rental createNewRental(int userID, int itemID)
    throws
    EntityNotFoundException, RentalNotAllowedException, InvalidIDException
    {
        if (verbose)
            System.out.println("\nCreating new rental...");

        //Check IDs, can throw InvalidIDException
        if (checkUserID(userID))
            throw new InvalidIDException("Rental creation failed: invalid userID " + userID);
        if (checkItemID(itemID))
            throw new InvalidIDException("Rental creation failed: invalid itemID " + itemID);

        String username = ""; //Create username here so catch block is happy
        String title = ""; //Create title here so catch block is happy

        try
        {
            //Retrieve user, throws EntityNotFoundException if not found or RentalNotAllowedException if not allowed
            User user = getValidatedUser(userID);
            username = user.getUsername();

            //Retrieve item, throws EntityNotFoundException if not found
            Item item = getExistingItem(itemID);
            title = item.getTitle();
            itemID = item.getItemID(); //Might be changed if item wasn't available

            //Create rental
            Rental newRental = new Rental(userID, itemID);

            //Set rental fields except rentalID
            newRental.setUsername(username);
            newRental.setItemTitle(title);

            //Due date
            int allowedRentalDays = ItemHandler.getAllowedRentalDaysByID(itemID);
            LocalDateTime dueDate = newRental.getRentalDate().plusDays(allowedRentalDays);
            newRental.setRentalDueDate(dueDate);

            //Save rental
            int rentalID = saveRental(newRental);
            newRental.setRentalID(rentalID);

            //Update Item to change its status to not available
            item.setAvailable(false);
            ItemHandler.updateItem(item);

            //Update User to increment number of current rentals
            user.setCurrentRentals(user.getCurrentRentals() + 1);
            UserHandler.updateUser(user);

            //Return rental
            return newRental;

        }
        catch (InvalidIDException | NullEntityException | InvalidDateException | InvalidNameException | InvalidTitleException | RetrievalException | InvalidUserRentalsException e)
        {
            String cause = (e.getCause() != null) ? e.getCause().getClass().getName() : "Unknown";
            ExceptionHandler.HandleFatalException("Rental creation failed due to " + cause + ":" + e.getMessage(), e);
        }
        catch (ConstructionException e)
        {
            String cause = (e.getCause() != null) ? e.getCause().getClass().getName() : "Unknown";
            ExceptionHandler.HandleFatalException("Rental construction failed due to " + cause, e);

        }

        //Won't reach, needed for compilation
        return null;
    }

    /**
     * Saves a new rental in the database.
     * <p>
     * The method accepts a Rental object, validates it, and then attempts to insert it
     * into the 'rentals' table in the database. The rental's user ID, item ID, rental date,
     * rental due date, and late fee are required and must be set in the
     * Rental object before calling this method. The rental return date can be null,
     * indicating that the item has not been returned yet.
     * <p>
     * The method returns the ID of the newly inserted rental as generated by the database.
     *
     * @param rental The Rental object to be saved. It must have all required fields set.
     * @return The ID of the newly inserted rental as generated by the database.
     */
    private static int saveRental(Rental rental)
    {
        try
        {
            //Validate input
            if (rental == null)
                throw new edu.groupeighteen.librarydbms.model.exceptions.NullEntityException(
                        "Error saving rental: rental is null.");

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
            try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params,
                                                                                Statement.RETURN_GENERATED_KEYS))
            {
                ResultSet generatedKeys = queryResult.getStatement().getGeneratedKeys();
                if (generatedKeys.next()) return generatedKeys.getInt(1);
            }
        }
        catch (edu.groupeighteen.librarydbms.model.exceptions.NullEntityException | SQLException e)
        {
            ExceptionHandler.HandleFatalException("Failed to save Rental due to " +
                                                          e.getClass().getName() + ": " + e.getMessage(), e);
        }

        //Cannot be reached, but needed for compilation
        return 0;
    }


    /**
     * Fetches all rentals from the database matching the provided SQL suffix and parameters. This allows for the execution
     * of complex and dynamic SQL queries, depending on the provided suffix and parameters. The ResultSet from the query
     * is converted into a list of Rental objects. In case of an error, a fatal exception will be handled and the program
     * will terminate.
     *
     * @param sqlSuffix The SQL query suffix to be added after "SELECT * FROM rentals". Can be null or contain conditions,
     *                  ordering, etc. E.g., "WHERE userID = ?".
     * @param params    An array of Strings representing the parameters to be set in the PreparedStatement for the query.
     *                  Each '?' character in the sqlSuffix will be replaced by a value from this array. Can be null if no
     *                  parameters are required.
     * @param settings  Settings to apply to the Statement, passed to the DatabaseHandler's executePreparedQuery method.
     *                  For example, it can be used to set Statement.RETURN_GENERATED_KEYS.
     * @return A list of Rental objects matching the query, or an empty list if no matching rentals are found.
     */
    private static List<Rental> getRentals(String sqlSuffix, String[] params, int settings)
    {
        //Convert the ResultSet into a List of Rental objects
        List<Rental> rentals = new ArrayList<>();

        // Prepare a SQL command to select all rentals from the 'rentals' table with given sqlSuffix
        String sql = "SELECT * FROM rentals " + (sqlSuffix == null ? "" : sqlSuffix);

        try
        {
            //Execute the query.
            try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(sql, params, settings))
            {

                //Retrieve the ResultSet from the QueryResult
                ResultSet resultSet = queryResult.getResultSet();

                //Loop through the results
                while (resultSet.next())
                {
                    int rentalID = resultSet.getInt("rentalID");

                    //Get user by ID, throws EntityNotFoundException
                    int userID = resultSet.getInt("userID");
                    User user = UserHandler.getUserByID(userID);
                    if (user == null) throw new edu.groupeighteen.librarydbms.model.exceptions.NullEntityException(
                            "Rental retrieval failed: NullEntityException " +
                                    "thrown for valid user with ID " + userID);
                    String username = user.getUsername();

                    //Get item by ID
                    int itemID = resultSet.getInt("itemID");
                    Item item = ItemHandler.getItemByID(itemID);
                    if (item == null) throw new NullEntityException("Rental retrieval failed: NullEntityException " +
                                                                            "thrown for valid item with ID " + itemID);
                    String itemTitle = item.getTitle();

                    LocalDateTime rentalDate = resultSet.getTimestamp("rentalDate").toLocalDateTime();
                    LocalDateTime rentalDueDate = resultSet.getTimestamp("rentalDueDate").toLocalDateTime();

                    //Rental return date can be null in the table
                    Timestamp returnDateTimestamp = resultSet.getTimestamp("rentalReturnDate");
                    LocalDateTime rentalReturnDate = null; // Set to null by default
                    if (returnDateTimestamp != null)
                    {
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
        }
        catch (SQLException | ConstructionException | InvalidIDException | InvalidDateException | InvalidNameException | InvalidTitleException | InvalidLateFeeException | NullEntityException | RetrievalException e)
        {
            ExceptionHandler.HandleFatalException("Failed to retrieve rentals from database due to " +
                                                          e.getClass().getName() + ": " + e.getMessage(), e);
        }

        //Return the List of rentals
        return rentals;
    }

    /**
     * Retrieves all rentals found in the table.
     *
     * @return a list of all rentals in database.
     */
    public static List<Rental> getAllRentals()
    {
        //Executor-class Star Dreadnought
        return getRentals(null, null, 0);
    }

    /**
     * This method fetches a rental by its rental ID from the database.
     *
     * @param rentalID The ID of the rental to fetch. This should be a valid, existing rental ID.
     * @return A Rental object corresponding to the rental with the specified ID if it exists,
     * or null if no such rental exists.
     * @throws InvalidIDException If the provided rental ID is invalid (i.e., less than or equal to zero).
     */
    public static Rental getRentalByID(int rentalID)
    throws InvalidIDException
    {
        //Validate input
        validateRentalID(rentalID); //Throws InvalidIDException if <= 0

        //Create a list to store the Rental objects (should only contain one element, but getRentals returns a list)
        List<Rental> rentals = null; //"Redundant" my ass, never rely on automatic initialization

        //Prepare suffix to select rentals by ID
        String suffix = "WHERE rentalID = " + rentalID;

        //Executor-class Star Dreadnought
        rentals = getRentals(suffix, null, 0);

        //Check results, this first option should not happen and will be considered fatal
        if (rentals.size() > 1)
            ExceptionHandler.HandleFatalException(new InvalidIDException("There should not be more than 1 rental " +
                                                                                 "with ID " + rentalID + ", received: " + rentals.size()));

            //Found something
        else if (rentals.size() == 1) return rentals.get(0);

        //Found nothing
        return null;
    }

    //UPDATE -----------------------------------------------------------------------------------------------------------

    /**
     * Updates the details of a rental record in the database.
     * <p>
     * The rental ID, User ID, Item ID, Username, Item Title, and Rental Date are immutable
     * and cannot be changed. Only the Due Date, Return Date, and Late Fee can be modified.
     *
     * @param updatedRental the rental object with updated details.
     * @throws UpdateException if the updatedRental is null, or if a rental with the provided rentalID
     *                               doesn't exist in the database.
     */
    public static void updateRental(Rental updatedRental)
    throws UpdateException
    {
        //Validate input
        try
        {
            validateRental(updatedRental);
        }
        catch (edu.groupeighteen.librarydbms.model.exceptions.NullEntityException | edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException | InvalidIDException e)
        {
            throw new UpdateException("Rental Update failed: " + e.getMessage(), e);
        }

        //Prepare a SQL query to update the rental details
        String query = "UPDATE rentals SET rentalDueDate = ?, rentalReturnDate = ?, lateFee = ? WHERE rentalID = ?";
        String[] params = {
                String.valueOf(updatedRental.getRentalDueDate()),
                (updatedRental.getRentalReturnDate() == null) ? null : updatedRental.getRentalReturnDate().toString(),
                String.valueOf(updatedRental.getLateFee()),
                String.valueOf(updatedRental.getRentalID())
        };

        //Executor-class Star Dreadnought
        DatabaseHandler.executePreparedUpdate(query, params);
    }

    /**
     * Softly deletes a rental, by marking it as deleted in the database.
     * The rental object is expected to be not null, with a valid rental ID.
     * If the rental object is invalid, a DeleteException is thrown.
     *
     * @param rentalToDelete the rental object to be softly deleted
     * @throws DeleteException if rental object is invalid
     */
    public static void deleteRental(Rental rentalToDelete)
    throws DeleteException
    {
        //Validate input
        try
        {
            validateRental(rentalToDelete);
        }
        catch (edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException | InvalidIDException | edu.groupeighteen.librarydbms.model.exceptions.NullEntityException e)
        {
            throw new DeleteException("Rental Delete failed: " + e.getMessage(), e);
        }

        //Set deleted to true (doesn't need to be set before calling this method)
        rentalToDelete.setDeleted(true);

        //Prepare a SQL query to update the rental details
        String query = "UPDATE rentals SET deleted = ? WHERE rentalID = ?";
        String[] params = {
                rentalToDelete.isDeleted() ? "1" : "0",
                String.valueOf(rentalToDelete.getRentalID())
        };

        //Executor-class Star Dreadnought
        DatabaseHandler.executePreparedUpdate(query, params);
    }

    /**
     * Reverses a soft delete of a rental, by marking it as not deleted in the database.
     * The rental object is expected to be not null, with a valid rental ID and the deleted attribute set to true.
     * If the rental object is invalid, a RecoveryException is thrown.
     * <p>
     * This method will do the reverse of softDeleteRental(), it will set the deleted attribute of rentalToRecover
     * to false before proceeding with the update in the database.
     * <p>
     * This allows the rental to be recovered from a soft delete.
     *
     * @param rentalToRecover the rental object to be recovered from soft delete
     * @throws RecoveryException if rental object is invalid
     */
    public static void undoDeleteRental(Rental rentalToRecover)
    throws RecoveryException
    {
        //Validate input
        try
        {
            validateRental(rentalToRecover);
        }
        catch (edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException | InvalidIDException | edu.groupeighteen.librarydbms.model.exceptions.NullEntityException e)
        {
            throw new RecoveryException("Rental Recovery failed: " + e.getMessage(), e);
        }

        //Set deleted to false
        rentalToRecover.setDeleted(false);

        //Prepare a SQL query to update the rental details
        String query = "UPDATE rentals SET deleted = ? WHERE rentalID = ?";
        String[] params = {
                rentalToRecover.isDeleted() ? "1" : "0",
                String.valueOf(rentalToRecover.getRentalID())
        };

        //Executor-class Star Dreadnought
        DatabaseHandler.executePreparedUpdate(query, params);
    }

    /**
     * Completely removes a rental from the database.
     * The rental object is expected to be not null, with a valid rental ID.
     * If the rental object is invalid, a DeleteException is thrown.
     *
     * @param rentalToDelete the rental object to be removed from the database
     * @throws DeleteException if rental object is invalid
     */
    public static void hardDeleteRental(Rental rentalToDelete)
    throws DeleteException
    {
        //Validate input
        try
        {
            validateRental(rentalToDelete);
        }
        catch (edu.groupeighteen.librarydbms.model.exceptions.NullEntityException | edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException | InvalidIDException e)
        {
            throw new DeleteException("Rental Delete failed: " + e.getMessage(), e);
        }

        //Prepare a SQL query to update the rentalToDelete details
        String query = "DELETE FROM rentals WHERE rentalID = ?";
        String[] params = {String.valueOf(rentalToDelete.getRentalID())};

        //Executor-class Star Dreadnought
        DatabaseHandler.executePreparedUpdate(query, params);
    }

    //RETRIEVING -------------------------------------------------------------------------------------------------------

    /**
     * Retrieves all rental instances associated with a given rental date.
     * More than one rental can be created within one second, hence this method returns a list of rentals.
     * The method may return an empty list if no rentals are found for the given date.
     *
     * @param rentalDate The rental date to retrieve rentals for.
     *                   This date is truncated to seconds to match the precision in the database.
     * @return A list of Rental objects that were rented at the given date.
     * If no rentals are found, it returns an empty list.
     * @throws InvalidDateException If the provided rental date is invalid.
     */
    public static List<Rental> getRentalsByRentalDate(LocalDateTime rentalDate)
    throws InvalidDateException
    {
        //No point getting invalid rentals
        validateRentalDate(rentalDate); //Throws InvalidDateException if null or future

        //Create a list to store the Rental objects (more than one can be created within one second)
        List<Rental> rentals = new ArrayList<>();

        //Need to truncate to seconds
        rentalDate = rentalDate.truncatedTo(ChronoUnit.SECONDS);

        // Prepare a SQL suffix to select rentals by rentalDate
        String suffix = " WHERE rentalDate = ?";

        // Prepare parameters for query
        String[] params = {rentalDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))};

        //Executor-class Star Dreadnought
        rentals = getRentals(suffix, params, 0); //No settings

        //Return results (empty list if nothing found)
        return rentals;
    }

    /**
     * Retrieves a list of rental objects whose rentalDate matches the given LocalDate.
     * The rentalDate is checked to be the same day, but not necessarily the exact same time.
     *
     * @param rentalDay the day (LocalDate) for which to find rentals
     * @return a list of Rental objects whose rentalDate matches the input date
     * @throws InvalidDateException if rentalDay is null or a future date
     */
    public static List<Rental> getRentalsByRentalDay(LocalDate rentalDay)
    throws InvalidDateException
    {
        //Validate the input
        validateRentalDay(rentalDay); //Throws InvalidDateException if null or future

        //Create an empty list to store the rentals.
        List<Rental> rentals = new ArrayList<>();

        //Create startOfDay and startOfDayPlusOne from rentalDay
        LocalDateTime startOfDay = rentalDay.atStartOfDay().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime startOfDayPlusOne = startOfDay.plusDays(1);

        //Prepare a SQL suffix to select rentals by rentalDay.
        String suffix = "WHERE rentalDate >= ? AND rentalDate < ?";

        // Prepare parameters for query
        String[] params = {startOfDay.toString(),
                startOfDayPlusOne.toString()};

        //Executor-class Star Dreadnought
        rentals = getRentals(suffix, params, 0); //No settings

        //Return results (empty list if nothing found)
        return rentals;
    }




/*
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

    //TODO-exception might want to throw a custom exception (like EntityNotFoundException) instead of returning null,
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

    //TODO-exception might want to throw a custom exception (like EntityNotFoundException) instead of returning null,
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

    //TODO-exception might want to throw a custom exception (like EntityNotFoundException) instead of returning null,
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

    //TODO-exception might want to throw a custom exception (like EntityNotFoundException) instead of returning null,
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
    }*/


    // UTILITY STUFF --------------------------------------------------------------------------------------------------

    /**
     * Checks if the provided userID is valid.
     *
     * @param userID the userID to be checked
     * @return true if userID is invalid (<= 0), false otherwise
     */
    private static boolean checkUserID(int userID)
    {
        return userID <= 0;
    }

    /**
     * Checks if the provided itemID is valid.
     *
     * @param itemID the itemID to be checked
     * @return true if itemID is invalid (<= 0), false otherwise
     */
    private static boolean checkItemID(int itemID)
    {
        return itemID <= 0;
    }

    /**
     * Retrieves a User object given a userID, throwing an exception if the User doesn't exist or is (soft) deleted.
     *
     * @param userID the id of the User to be retrieved
     * @return the User object corresponding to the given userID
     * @throws edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException if there's no User with the given userID
     * @throws InvalidIDException                                                     if the userID is invalid
     */
    private static User getValidatedUser(int userID)
    throws
    edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException, InvalidIDException,
    RentalNotAllowedException
    {
        if (verbose)
            System.out.println("\nGetting valid user with ID " + userID);

        User user = UserHandler.getUserByID(userID);

        //Not null
        if (user == null)
            throw new edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException(
                    "User with ID " + userID + " not found.");
        if (verbose)
            System.out.println("Found non-null user with ID " + userID);

        //Not deleted
        if (user.isDeleted())
            throw new edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException(
                    "User with ID " + userID + " found but is deleted.");
        if (verbose)
            System.out.println("User with ID " + userID + " is not deleted: " + !user.isDeleted());

        //Allowed to rent
        if (!user.isAllowedToRent())
            throw new RentalNotAllowedException("User not allowed to rent either due to already renting at " +
                                                        "maximum capacity or having a late fee." +
                                                        "\nCurrent late fee: " + user.getLateFee() + ", Current rentals: " + user.getCurrentRentals() +
                                                        ", Allowed rentals: " + user.getAllowedRentals());
        if (verbose)
            System.out.println("User with ID " + userID + " is allowed to rent: " + user.isAllowedToRent());

        return user;
    }

    /**
     * Retrieves an Item object given an itemID, throwing an exception if the Item doesn't exist or is (soft) deleted.
     *
     * @param itemID the id of the Item to be retrieved
     * @return the Item object corresponding to the given itemID
     * @throws EntityNotFoundException if there's no Item with the given itemID
     * @throws InvalidIDException      if the itemID is invalid
     */
    private static Item getExistingItem(int itemID)
    throws
    EntityNotFoundException, InvalidIDException,
    InvalidTitleException, RetrievalException
    {
        if (verbose)
            System.out.println("\nGetting available item with ID " + itemID);

        Item item = ItemHandler.getItemByID(itemID);
        if (item == null)
            throw new EntityNotFoundException("Item with ID " + itemID + " not found.");
        if (verbose)
            System.out.println("Found non-null Item with ID " + itemID + " and title '" + item.getTitle() + "'");

        if (item.isDeleted())
            throw new EntityNotFoundException("Item with ID " + itemID + " found but is deleted.");
        if (verbose)
            System.out.println("Item with ID " + itemID + " is not deleted: " + !item.isDeleted());

        if (verbose)
            System.out.println("Item with ID " + itemID + " is available: " + item.isAvailable());

        if (!item.isAvailable())
            item = getAvailableCopy(item.getTitle());

        return item;
    }

    /**
     * Retrieves an available copy of an Item given its title, throwing an exception if no available copy exists.
     *
     * @param title the title of the Item to be retrieved
     * @return an available Item object with the given title
     * @throws InvalidTitleException   if the title is invalid
     * @throws EntityNotFoundException if there's no available copy of the Item with the given title
     */
    private static Item getAvailableCopy(String title)
    throws InvalidTitleException, EntityNotFoundException
    {
        if (verbose)
            System.out.println("\nGetting another available copy of item with title '" + title + "'");

        List<Item> items = ItemHandler.getItemsByTitle(title);

        for (Item item : items)
        {
            if (item.isAvailable())
                return item;
        }

        throw new EntityNotFoundException(
                "Rental creation failed: No available copy of " + title + " found.");
    }

    /**
     * Validates a rentalID, ensuring it is a positive integer.
     *
     * @param rentalID the rentalID to be validated
     * @throws InvalidIDException if the rentalID is invalid (<= 0)
     */
    private static void validateRentalID(int rentalID)
    throws InvalidIDException
    {
        if (rentalID <= 0) throw new InvalidIDException("Invalid rentalID. rentalID: " + rentalID);
    }

    /**
     * Validates a rentalDate, ensuring it is not null and is not a future date.
     *
     * @param rentalDate the rentalDate to be validated
     * @throws InvalidDateException if the rentalDate is null or a future date
     */
    private static void validateRentalDate(LocalDateTime rentalDate)
    throws InvalidDateException
    {
        if (rentalDate == null || rentalDate.compareTo(LocalDateTime.now()) > 0)
            throw new InvalidDateException("Invalid rentalDate: RentalDate cannot be null or in the future. " +
                                                   "Received: " + rentalDate);
    }

    /**
     * Validates a given LocalDate, checking that it is not null and is not a future date.
     * If the input is null or a future date, an InvalidDateException is thrown.
     *
     * @param rentalDay the date to be validated
     * @throws InvalidDateException if rentalDay is null or a future date
     */
    private static void validateRentalDay(LocalDate rentalDay)
    throws InvalidDateException
    {
        if (rentalDay == null || rentalDay.isAfter(LocalDate.now()))
            throw new InvalidDateException("Invalid rentalDay: RentalDay cannot be null or in the future. " +
                                                   "Received: " + rentalDay);
    }

    /**
     * Validates a rental object.
     * <p>
     * A rental is considered valid if it is not null and it exists in the database (has a valid ID).
     *
     * @param rentalToValidate the rental object to validate.
     * @throws edu.groupeighteen.librarydbms.model.exceptions.NullEntityException     if the rental is null.
     * @throws edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException if a rental with the provided rentalID doesn't exist in the database.
     * @throws InvalidIDException                                                     if the provided rentalID is invalid (less than or equal to 0).
     */
    private static void validateRental(Rental rentalToValidate)
    throws
    edu.groupeighteen.librarydbms.model.exceptions.NullEntityException,
    edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException, InvalidIDException
    {
        if (rentalToValidate == null)
            throw new edu.groupeighteen.librarydbms.model.exceptions.NullEntityException(
                    "Error validating rental: rental is null.");
        if (getRentalByID(rentalToValidate.getRentalID()) == null)
            throw new edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException(
                    "Error validating rental: rental with ID " + rentalToValidate.getRentalID() + " not found.");
    }


    /**
     * Prints all data of rentals in a list.
     *
     * @param rentals the list of rentals.
     */
    public static void printRentalList(List<Rental> rentals)
    {
        System.out.println("Rentals:");
        int count = 1;
        for (Rental rental : rentals)
        {
            System.out.println(count + " rentalID: " + rental.getRentalID() + ", userID: " + rental.getUserID()
                                       + ", username: " + rental.getUsername() + ", itemID: " + rental.getItemID()
                                       + ", item title: " + rental.getItemTitle() + ", rental date: " + rental.getRentalDate()
                                       + ", rental due date: " + rental.getRentalDueDate()
                                       + ", rental return date: " + rental.getRentalReturnDate() + ", late fee: " + rental.getLateFee());
            count++;
        }
    }
}