package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.model.entities.Rental;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.item.InvalidTitleException;
import edu.groupeighteen.librarydbms.model.exceptions.rental.InvalidReceiptException;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalNotAllowedException;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalReturnException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidUserRentalsException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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
 */
public class RentalHandler
{
    //TODO-future overhaul exception handling
    //TODO-future move utility methods into a separate class
    //TODO-future implement more get-methods
    //TODO-future overhaul main get-method


    //TODO-PRIO RETURNING OF RENTALS

    /**
     * Indicates whether verbose mode is enabled.
     */
    private static boolean verbose = false;

    /**
     * Checks if verbose mode is enabled.
     *
     * @return true if verbose mode is enabled, false otherwise
     */
    public static boolean isVerbose()
    {
        return verbose;
    }

    /**
     * Sets the verbose mode.
     *
     * @param verbose the value indicating whether verbose mode should be enabled or disabled
     */
    public static void setVerbose(boolean verbose)
    {
        RentalHandler.verbose = verbose;
    }

    /**
     * Creates a new rental in the system.
     * This method checks that the user ID and item ID are valid, retrieves the user and item,
     * and updates the item to mark it as not available and the user to increment the number of current rentals.
     * The newly created rental is then returned.
     *
     * @param userID the ID of the user renting the item
     * @param itemID the ID of the item being rented
     * @return the newly created Rental
     * @throws EntityNotFoundException   if the user or item cannot be found
     * @throws RentalNotAllowedException if the user is not allowed to rent the item
     * @throws InvalidIDException        if the user ID or item ID is not valid§
     *                                   <p>
     * TODO: Improve exception handling. Current handling is not consistent with other classes and needs refinement.
     * TODO: Make this method shorter. It currently performs multiple tasks which might be better broken down into
     *  smaller methods.
     * @see User
     * @see Item
     * @see Rental
     */
    public static Rental createNewRental(int userID, int itemID)
    throws
    EntityNotFoundException, RentalNotAllowedException, InvalidIDException, InvalidTypeException
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
        String itemType = ""; //Create the itemType string here so catch block is happy

        try
        {
            //Retrieve user, throws EntityNotFoundException if not found or RentalNotAllowedException if not allowed
            User user = getValidatedUser(userID);
            username = user.getUsername();

            //Retrieve item, throws EntityNotFoundException if not found
            Item item = getExistingItem(itemID);
            title = item.getTitle();
            itemID = item.getItemID(); //Might be changed if item wasn't available
            itemType = item.getType().toString();

            //Create rental
            Rental newRental = new Rental(userID, itemID);

            //Set rental fields except rentalID
            newRental.setUsername(username);
            newRental.setItemTitle(title);
            newRental.setItemType(itemType);

            //Due date
            int allowedRentalDays = ItemHandler.getAllowedRentalDaysByID(itemID);
            LocalDateTime dueDate = newRental.getRentalDate().plusDays(allowedRentalDays);
            newRental.setRentalDueDate(dueDate);

            //Create and set receipt
            newRental.setReceipt(createReceipt(newRental));

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
        catch (InvalidIDException | NullEntityException | InvalidDateException | InvalidNameException |
               InvalidTitleException | RetrievalException | InvalidUserRentalsException | UpdateException |
               InvalidReceiptException e)
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
     * Generates a receipt for a newly created rental.
     * The receipt includes information about the rental date, rental due date, user ID, username, item title
     * and item type.
     *
     * @param newRental the new Rental for which the receipt is to be created
     * @return a String representing the receipt for the Rental
     * @see Rental
     */
    private static String createReceipt(Rental newRental)
    {
        return "\nRECEIPT" +
                "\nRental Date: " + newRental.getRentalDate() +
                "\nRental Due Date: " + newRental.getRentalDueDate() +
                "\nUser ID: " + newRental.getUserID() + ", User: " + newRental.getUsername() +
                "\nItem Title: " + newRental.getItemTitle() +
                "\nItem Type: " + newRental.getItemType() + "\n";
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
                throw new NullEntityException(
                        "Error saving rental: rental is null.");

            //Prepare query
            String query = "INSERT INTO rentals " +
                    "(userID, itemID, rentalDate, rentalDueDate, rentalReturnDate, lateFee, receipt, deleted) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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
                    rental.getReceipt(),
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
        catch (NullEntityException | SQLException e)
        {
            ExceptionHandler.HandleFatalException("Failed to save Rental due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        //Cannot be reached, but needed for compilation
        return 0;
    }

    /**
     * Returns a rental and updates the necessary details related to the associated user and item.
     * <p>
     * This method executes the procedure of returning a rented item as follows:
     * <ol>
     *   <li>Validates the returnability of the provided rental. The rental is considered returnable if it is not already
     *       returned and it is associated with a valid user and a valid item. If the rental is not valid, the method
     *       throws a {@link RentalReturnException}.</li>
     *   <li>Retrieves the User and Item associated with the rental. This step ensures that the rental is associated with
     *       valid entities. If any of the associated entities are not valid, the method considers it a fatal exception and
     *       handles it accordingly.</li>
     *   <li>Sets the return date of the rental to the current time. The method considers any exception during this step as
     *       a fatal exception.</li>
     *   <li>Updates the rental in the database. Any exception during this update is considered fatal.</li>
     *   <li>Decrements the number of rentals of the User and updates the User. The method handles any exception during
     *       this step as a fatal exception.</li>
     *   <li>Changes the availability status of the Item and updates it in the database. The method also updates the list of
     *       available items in the ItemHandler.</li>
     * </ol>
     *
     * @param rentalToReturn The rental to be returned.
     * @return The rental that has been returned.
     * @throws RentalReturnException If the rental is not returnable due to invalid input or the rental is already returned.
     */
    public static Rental returnRental(Rental rentalToReturn)
    throws RentalReturnException
    {
        //Validate input, check if rental is already returned
        try
        {
            validateReturnableRental(rentalToReturn); //Throws sooo many exceptions
        }
        catch (NullEntityException | InvalidIDException | EntityNotFoundException e)
        { //Input Exceptions are considered non-fatal
            throw new RentalReturnException("Rental return failed: " + e.getMessage(), e);
        }

        try
        {
            //Retrieve User and Item
            User user = UserHandler.getUserByID(rentalToReturn.getUserID()); //InvalidIDException, fatal
            Item item = ItemHandler.getItemByID(rentalToReturn.getItemID()); //RetrievalException, fatal

            //I need to look over UserHandler since user should technically be able to be null just like item here
            if (item == null)
                throw new EntityNotFoundException(
                        "Item related to rental with rentalID " + rentalToReturn.getRentalID() +
                                " not found.");

            //Set rentalReturnDate, throws InvalidDateException which is considered fatal in this context
            rentalToReturn.setRentalReturnDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

            //Update the rental in the table, throws UpdateException which is considered fatal in this context
            updateRental(rentalToReturn);

            //Decrement User's currentRentals and update
            user.setCurrentRentals(user.getCurrentRentals() - 1); //InvalidUserRentalsException, fatal
            UserHandler.updateUser(user);

            //Change Item's availability and update ItemHandler's list
            item.setAvailable(true);
            ItemHandler.updateItem(item);
            ItemHandler.incrementAvailableTitles(item.getTitle());
        }
        catch (InvalidDateException | UpdateException | InvalidIDException | RetrievalException |
               InvalidUserRentalsException | NullEntityException | EntityNotFoundException e)
        { //We get these and something has gone seriously wrong
            ExceptionHandler.HandleFatalException("Rental return failed fatally: " + e.getMessage(), e);
        }

        return rentalToReturn;
    }

    /**
     * Validates whether a rental is returnable.
     * <p>
     * This method checks that the provided rental meets the following conditions:
     * <ul>
     *   <li>The rental is not null.</li>
     *   <li>The rental exists in the database.</li>
     *   <li>The rental is active (has not been returned yet).</li>
     * </ul>
     * If any of these conditions is not met, the method throws the appropriate exception.
     *
     * @param rentalToReturn The rental to be validated.
     * @throws NullEntityException     If the rental is null.
     * @throws InvalidIDException      If the rental's ID is invalid.
     * @throws EntityNotFoundException If the rental is not found in the database.
     * @throws RentalReturnException   If the rental has already been returned.
     */
    private static void validateReturnableRental(Rental rentalToReturn)
    throws NullEntityException, InvalidIDException, EntityNotFoundException, RentalReturnException
    {
        //Not null
        if (rentalToReturn == null)
            throw new NullEntityException("Can't return rental; rental is null.");
        //Exists
        if (getRentalByID(rentalToReturn.getRentalID()) == null)
            throw new EntityNotFoundException("Can't return rental; rental not found in table.");
        //Is active
        if (rentalToReturn.getRentalReturnDate() != null)
            throw new RentalReturnException("Can't return rental; rental already returned.");
    }

    //TODO-prio update according to getUsers, getItems, to take a wider variety of suffixes

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
                    //Construct rental
                    Rental retrievedRental = constructRetrievedRental(resultSet);

                    //Add to list
                    rentals.add(retrievedRental);
                }
            }
        }
        catch (SQLException e)
        {
            ExceptionHandler.HandleFatalException("Failed to retrieve rentals from database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        //Return the List of rentals
        return rentals;
    }

    /**
     * Constructs a Rental object from the provided ResultSet. This method is used to convert database records
     * into Java objects. It fetches all necessary information about a rental, including associated user and item information.
     *
     * @param resultSet the ResultSet obtained from the database query for a rental record
     * @see Rental
     * @see SQLException
     * @see InvalidIDException
     * @see RetrievalException
     * @see ConstructionException
     */
    private static Rental constructRetrievedRental(ResultSet resultSet)
    {
        try
        {
            //Get userID and itemID
            int userID = resultSet.getInt("userID");
            int itemID = resultSet.getInt("itemID");

            //Convert Timestamps to LocalDateTimes
            LocalDateTime rentalDate = convertTimeStampToLocalDateTime(resultSet, "rentalDate");
            LocalDateTime rentalDueDate = convertTimeStampToLocalDateTime(resultSet, "rentalDueDate");
            LocalDateTime rentalReturnDate = convertTimeStampToLocalDateTime(resultSet, "rentalReturnDate");

            //Get username, itemTitle and itemType
            String username = UserHandler.getUserByID(userID).getUsername();
            Item item = ItemHandler.getItemByID(itemID);
            String itemTitle = item.getTitle(); //Throws RetrievalException
            String itemType = item.getType().toString();

            //Create and return the rental
            return new Rental(
                    resultSet.getInt("rentalID"),
                    userID,
                    itemID,
                    rentalDate,
                    rentalDueDate,
                    username,
                    itemTitle,
                    itemType,
                    rentalReturnDate,
                    resultSet.getFloat("lateFee"),
                    resultSet.getString("receipt"),
                    resultSet.getBoolean("deleted")
            );
        }
        catch (SQLException | InvalidIDException | RetrievalException | ConstructionException e)
        {
            ExceptionHandler.HandleFatalException("Failed to construct retrieved rental from database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        //Not reached, but needed for compilation
        return null;
    }

    /**
     * Converts a SQL Timestamp to a LocalDateTime object.
     * This method is used to accurately convert time data stored in the database into a format that is
     * more easily used in the Java environment.
     *
     * @param resultSet  the ResultSet obtained from the database query
     * @param columnName the name of the column in the result set that contains the Timestamp
     * @return a LocalDateTime object representing the time stored in the specified column of the result set
     * @throws SQLException if a database access error occurs or this method is called on a closed result set
     * @see LocalDateTime
     * @see SQLException
     */
    private static LocalDateTime convertTimeStampToLocalDateTime(ResultSet resultSet, String columnName)
    throws SQLException
    {
        Timestamp timestamp = resultSet.getTimestamp(columnName);
        LocalDateTime localDateTime = null;
        if (timestamp != null)
        {
            localDateTime = timestamp.toLocalDateTime();
        }
        return localDateTime;
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

    /**
     * Retrieves a list of all overdue rentals from the database. Overdue rentals are those whose
     * due date is earlier than the current date and time and have not yet been returned. The current
     * date and time are formatted to match the SQL DateTime format and used as a parameter for the
     * query.
     *
     * @return a List of Rental objects representing all overdue rentals in the system.
     * @see Rental
     * @see LocalDateTime
     */
    public static List<Rental> getOverdueRentals()
    {
        // Prepare a SQL suffix to select rentals that are overdue
        String suffix = "WHERE rentalDueDate < ? AND rentalReturnDate IS NULL";

        // Prepare parameters for query
        String[] params = {DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").
                format(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))};

        //Executor-class Star Dreadnought
        return getRentals(suffix, params, 0); //No settings
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
     *                         doesn't exist in the database.
     */
    public static void updateRental(Rental updatedRental)
    throws UpdateException
    {
        //Validate input
        try
        {
            validateRental(updatedRental);
        }
        catch (NullEntityException | EntityNotFoundException | InvalidIDException e)
        {
            throw new UpdateException("Rental Update failed: " + e.getMessage(), e);
        }

        //Prepare a SQL query to update the rental details
        String query = "UPDATE rentals " +
                "SET userID = ?, itemID = ?, rentalDate = ?, rentalDueDate = ?, rentalReturnDate = ?, lateFee = ?, " +
                "receipt = ? WHERE rentalID = ?";
        String[] params = {
                String.valueOf(updatedRental.getUserID()), //TODO-prio Should probably not be allowed
                String.valueOf(updatedRental.getItemID()), //TODO-prio Should probably not be allowed
                String.valueOf(updatedRental.getRentalDate()), //TODO-prio Should probably not be allowed
                String.valueOf(updatedRental.getRentalDueDate()),
                (updatedRental.getRentalReturnDate() == null) ? null : updatedRental.getRentalReturnDate().toString(),
                String.valueOf(updatedRental.getLateFee()),
                updatedRental.getReceipt(), //TODO-prio Should probably not be allowed
                String.valueOf(updatedRental.getRentalID())
        };

        //Executor-class Star Dreadnought
        DatabaseHandler.executePreparedUpdate(query, params);
    }

    /**
     * Softly deletes a rental, by marking it as deleted in the database.
     * The rental object is expected to be not null, with a valid rental ID.
     * If the rental object is invalid, a DeletionException is thrown.
     *
     * @param rentalToDelete the rental object to be softly deleted
     * @throws DeletionException if rental object is invalid
     */
    public static void deleteRental(Rental rentalToDelete)
    throws DeletionException
    {
        //Validate input
        try
        {
            validateRental(rentalToDelete);
        }
        catch (EntityNotFoundException | InvalidIDException | NullEntityException e)
        {
            throw new DeletionException("Rental Delete failed: " + e.getMessage(), e);
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
    public static void recoverRental(Rental rentalToRecover)
    throws RecoveryException
    {
        //Validate input
        try
        {
            validateRental(rentalToRecover);
        }
        catch (EntityNotFoundException | InvalidIDException | NullEntityException e)
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
     * If the rental object is invalid, a DeletionException is thrown.
     *
     * @param rentalToDelete the rental object to be removed from the database
     * @throws DeletionException if rental object is invalid
     */
    public static void hardDeleteRental(Rental rentalToDelete)
    throws DeletionException
    {
        //Validate input
        try
        {
            validateRental(rentalToDelete);
        }
        catch (NullEntityException | EntityNotFoundException | InvalidIDException e)
        {
            throw new DeletionException("Rental Delete failed: " + e.getMessage(), e);
        }

        //Prepare a SQL query to update the rentalToDelete details
        String query = "DELETE FROM rentals WHERE rentalID = ?";
        String[] params = {String.valueOf(rentalToDelete.getRentalID())};

        //Executor-class Star Dreadnought
        DatabaseHandler.executePreparedUpdate(query, params);
    }

    //RETRIEVING -------------------------------------------------------------------------------------------------------

    //TODO OPTIONAL

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

        //Need to truncate to seconds
        rentalDate = rentalDate.truncatedTo(ChronoUnit.SECONDS);

        // Prepare a SQL suffix to select rentals by rentalDate
        String suffix = " WHERE rentalDate = ?";

        // Prepare parameters for query
        String[] params = {rentalDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))};

        //Executor-class Star Dreadnought
        return getRentals(suffix, params, 0); //No settings
    }

    //TODO OPTIONAL

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

        //Create startOfDay and startOfDayPlusOne from rentalDay
        LocalDateTime startOfDay = rentalDay.atStartOfDay().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime startOfDayPlusOne = startOfDay.plusDays(1);

        //Prepare a SQL suffix to select rentals by rentalDay.
        String suffix = "WHERE rentalDate >= ? AND rentalDate < ?";

        // Prepare parameters for query
        String[] params = {startOfDay.toString(),
                startOfDayPlusOne.toString()};

        //Executor-class Star Dreadnought
        return getRentals(suffix, params, 0); //No settings
    }

    //TODO OPTIONAL
    public static List<Rental> getRentalsByTimePeriod(LocalDate startDate, LocalDate endDate)
    throws InvalidDateException
    {
        //Validate the inputs
        if (startDate == null || startDate.isAfter(LocalDate.now()))
            throw new InvalidDateException("Invalid dates: Start date cannot be null or in the future.");
        if (endDate == null || endDate.isAfter(LocalDate.now()))
            throw new InvalidDateException("Invalid dates: End date cannot be null or in the future.");
        if (startDate.isAfter(endDate))
            throw new InvalidDateException("Invalid dates: Start date must be before or equal to end date.");

        //Convert the LocalDate to LocalDateTime for database comparison
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atTime(23, 59, 59);

        //Prepare a SQL query to select rentals by rentalDate within a given period
        String suffix = "WHERE rentalDate >= ? AND rentalDate <= ?";
        String[] params = {startDateTime.toString(), endDateTime.toString()};

        //Return the list of rentals
        return getRentals(suffix, params, 0);
    }

    //TODO OPTIONAL
    public static List<Rental> getRentalsByUserID(int userID)
    throws InvalidIDException
    {
        //Validate the input
        if (userID <= 0)
            throw new InvalidIDException("Invalid userID: " + userID + ". userID must be greater than 0.");

        //Prepare a SQL query to select rentals by userID
        String suffix = "WHERE userID = ?";
        String[] params = {String.valueOf(userID)};

        //Return the list of rentals
        return getRentals(suffix, params, 0);
    }

    //TODO OPTIONAL
    public static List<Rental> getRentalsByItemID(int itemID)
    {
        //Validate the input
        if (itemID <= 0)
            throw new IllegalArgumentException("Invalid itemID: " + itemID + ". itemID must be greater than 0.");

        //Prepare a SQL query to select rentals by itemID
        String suffix = "WHERE itemID = ?";
        String[] params = {Integer.toString(itemID)};

        //Return the list of rentals
        return getRentals(suffix, params, 0);
    }

    //TODO OPTIONAL
    //TODO-PRIO won't work with getRentals
    public static List<Rental> getRentalsByUsername(String username)
    throws InvalidNameException
    {
        //Validate the input
        if (username == null || username.isEmpty())
            throw new InvalidNameException("Invalid username: username can't be null or empty.");

        //Prepare a SQL query to select rentals by username
        String suffix = "INNER JOIN users ON rentals.userID = users.userID " +
                "WHERE users.username = ?";
        String[] params = {username};

        //Return the list of rentals
        return getRentals(suffix, params, 0);
    }

    //TODO OPTIONAL
    //TODO-PRIO won't work with getRentals
    public static List<Rental> getRentalsByItemTitle(String title)
    throws InvalidTitleException
    {
        //Validate the input
        if (title == null || title.isEmpty())
            throw new InvalidTitleException("Invalid title: title can't be null or empty.");

        //Prepare a SQL query to select rentals by item title
        String suffix = "INNER JOIN items ON rentals.itemID = items.itemID WHERE items" +
                ".title = ?";
        String[] params = {title};

        //Return the list of rentals
        return getRentals(suffix, params, 0);
    }

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
     * @throws EntityNotFoundException if there's no User with the given userID
     * @throws InvalidIDException      if the userID is invalid
     */
    private static User getValidatedUser(int userID)
    throws
    EntityNotFoundException, InvalidIDException,
    RentalNotAllowedException
    {
        if (verbose)
            System.out.println("\nGetting valid user with ID " + userID);

        User user = UserHandler.getUserByID(userID);

        //Not null
        if (user == null)
            throw new EntityNotFoundException(
                    "User with ID " + userID + " not found.");
        if (verbose)
            System.out.println("Found non-null user with ID " + userID);

        //Not deleted
        if (user.isDeleted())
            throw new EntityNotFoundException(
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
    InvalidTitleException, RetrievalException, RentalNotAllowedException
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

        if (item.getAllowedRentalDays() <= 0)
            throw new RentalNotAllowedException("Item with ID: " + itemID + " is not allowed for rent.");
        if (verbose)
            System.out.println("Item with ID: " + itemID + " is allwed to be rented for " +
                    item.getAllowedRentalDays() + " days.");

        if (!item.isAvailable())
            item = getAvailableCopy(item.getTitle());
        if (verbose)
            System.out.println("Item with ID " + itemID + " is available: " + item.isAvailable());

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
     * @throws NullEntityException     if the rental is null.
     * @throws EntityNotFoundException if a rental with the provided rentalID doesn't exist in the database.
     * @throws InvalidIDException      if the provided rentalID is invalid (less than or equal to 0).
     *                                 <p>
     *                                                                                                 TODO add deletion checks
     */
    private static void validateRental(Rental rentalToValidate)
    throws
    NullEntityException, EntityNotFoundException, InvalidIDException
    {
        if (rentalToValidate == null)
            throw new NullEntityException(
                    "Error validating rental: rental is null.");
        if (getRentalByID(rentalToValidate.getRentalID()) == null)
            throw new EntityNotFoundException(
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