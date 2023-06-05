package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidEmailException;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.user.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.control
 * @contact matfir-1@student.ltu.se
 * @date 4/5/2023
 * <p>
 * This class contains database CRUD operation methods as well as other methods related to the User entity class.
 * It contains a list of all usernames for quicker validation.
 * <p>
 * Note on Exceptions:
 * <p>
 * "Exceptions should only be thrown in exceptional circumstances, and invalid user input is not exceptional".
 * <p>
 * I've battled this one for long, but if finally clicked. This class is NOT handling user input. That is going
 * to be handled in UserCreateGUI. When I press the "Create User" button in that class, we perform an instant
 * check on whether the title, and any other needed fields, are empty.
 * <p>
 * If so, we print an error message, reset all fields in the GUI and wait for new input.
 * <p>
 * Meaning, createNewUser (as an example) should NEVER be called with an empty or null String as argument.
 * If it is, that IS exceptional.
 * <p>
 * And, it's a good way of reminding myself that I do, in fact, need to verify user input before performing
 * unnecessary method calls.
 */
public class UserHandler //TODO-future rewrite Get-methods according to ItemHandler and RentalHandler, re-test
{
    /**
     * Used to make the process of verifying if a username is taken or not faster.
     */
    private static final ArrayList<String> storedUsernames = new ArrayList<>();

    /**
     * Used to make the process of verifying if an email is registered or not faster.
     */
    private static final ArrayList<String> registeredEmails = new ArrayList<>();

    /**
     * Performs setup tasks. In this case, syncing storedUsernames against the database.
     */
    public static void setup()
    {
        sync();
    }

    /**
     * Clears and syncs the both lists against the Users table.
     */
    public static void sync()
    {
        syncUsernames();
        syncEmails();
    }

    /**
     * Clears and syncs the storedUsernames list against the usernames in the Users table.
     */
    public static void syncUsernames()
    {
        resetUsernames();
        retrieveUsernamesFromTable();
    }

    /**
     * Clears and syncs the registeredEmails list against the emails in the Users table.
     */
    public static void syncEmails()
    {
        resetEmails();
        retrieveEmailsFromTable();
    }

    /**
     * Method that retrieves the usernames in the Users table and stores them in the static ArrayList.
     * Query needs to be ORDER BY user_id ASC or ids will be in the order of 10, 1, 2, ...
     */
    private static void retrieveUsernamesFromTable()
    {
        try
        {
            //Execute the query to retrieve all usernames
            String query = "SELECT username FROM users ORDER BY userID ASC";
            try (QueryResult result = DatabaseHandler.executeQuery(query))
            {

                //Add the retrieved usernames to the ArrayList
                while (result.getResultSet().next())
                {
                    storedUsernames.add(result.getResultSet().getString("username"));
                }
            }
        }
        catch (SQLException e)
        {
            ExceptionHandler.HandleFatalException("Failed to retrieve usernames from database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Method that retrieves the emails in the Users table and stores them in the static ArrayList.
     * Query needs to be ORDER BY user_id ASC or ids will be in the order of 10, 1, 2, ...
     */
    private static void retrieveEmailsFromTable()
    {
        try
        {
            //Execute the query to retrieve all emails
            String query = "SELECT email FROM users ORDER BY userID ASC";
            try (QueryResult result = DatabaseHandler.executeQuery(query))
            {

                //Add the retrieved usernames to the ArrayList
                while (result.getResultSet().next())
                {
                    registeredEmails.add(result.getResultSet().getString("email"));
                }
            }
        }
        catch (SQLException e)
        {
            ExceptionHandler.HandleFatalException("Failed to retrieve emails from database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Clears both lists.
     */
    public static void reset()
    {
        resetUsernames();
        resetEmails();
    }

    /**
     * Clears the list of storedUsernames.
     */
    public static void resetUsernames()
    {
        storedUsernames.clear();
    }

    /**
     * Clears the list of registeredEmails.
     */
    public static void resetEmails()
    {
        registeredEmails.clear();
    }

    /**
     * Prints all usernames in the ArrayList.
     */
    public static void printUsernames()
    {
        System.out.println("\nUsernames:");
        int num = 1;
        for (String username : storedUsernames)
        {
            System.out.println(num + ": " + username);
            num++;
        }
    }

    /**
     * Prints all emails in the ArrayList.
     */
    public static void printEmails()
    {
        System.out.println("\nEmails:");
        int num = 1;
        for (String email : registeredEmails)
        {
            System.out.println(num + ": " + email);
            num++;
        }
    }

    /**
     * Returns the ArrayList of usernames.
     *
     * @return the ArrayList of usernames
     */
    public static ArrayList<String> getStoredUsernames()
    {
        return storedUsernames;
    }

    /**
     * Returns the ArrayList of emails.
     *
     * @return the ArrayList of emails
     */
    public static ArrayList<String> getRegisteredEmails()
    {
        return registeredEmails;
    }

    /**
     * Prints all non-sensitive data for all Users in a list.
     *
     * @param userList the list of User objects.
     */
    public static void printUserList(List<User> userList)
    {
        System.out.println("Users:");
        int count = 1;
        for (User user : userList)
        {
            System.out.println(count + " userID: " + user.getUserID() + ", username: " + user.getUsername());
        }
    }

    //CREATE -----------------------------------------------------------------------------------------------------------

    /**
     * Creates a new User with the provided username, password, userType, and email, then stores it.
     * <p>
     * This method validates the input parameters, constructs a new User object, and saves it.
     * It also adds the username and email to the respective lists in UserHandler.
     * If an exception is encountered during the construction or saving process, it will be
     * handled accordingly.
     *
     * @param username the username for the new user. Must be a unique, non-null, non-empty string
     *                 with length between the limits set in User class.
     * @param password the password for the new user. Must be a non-null, non-empty string
     *                 with length between the limits set in User class.
     * @param email    the email for the new user. Must be a unique, non-null, non-empty string
     *                 with length between the limits set in User class, and properly formatted.
     * @param userType the UserType of the new user. Must be a non-null value from UserType enum.
     * @return the newly created User object.
     * @throws CreationException if validation of input parameters fails.
     */
    public static User createNewUser(String username, String password, String email, User.UserType userType)
    throws CreationException
    {
        User newUser = null;

        try
        {
            //Validate input
            validateUsername(username);
            validatePassword(password);
            validateEmail(email);
            validateUserType(userType);

            //Create and save the new user, retrieving the ID
            newUser = new User(username, password, email, userType);
            newUser.setUserID(saveUser(newUser));

            //Need to remember to add to the lists
            storedUsernames.add(username);
            registeredEmails.add(email);
        }
        catch (ConstructionException | InvalidIDException e)
        {
            ExceptionHandler.HandleFatalException(String.format("Failed to create User with username: " +
                    "'%s' due to %s: %s", username, e.getClass().getName(), e.getMessage()), e);
        }
        catch (InvalidEmailException | InvalidNameException | InvalidPasswordException | InvalidTypeException e)
        {
            throw new CreationException("Failed to create User due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        return newUser;
    }

    /**
     * Saves a user to the database. The method prepares an SQL insert query with the user's details such as
     * username, password, userType, email, allowed rentals, current rentals, late fee, allowedToRent and deleted status.
     * The query is executed and the auto-generated user ID from the database is retrieved and returned. If the query
     * execution fails, the method handles the SQLException and terminates the application.
     *
     * @param user The user object to be saved.
     * @return The auto-generated ID of the user from the database.
     * Returns 0 if an SQLException occurs. This won't happen because the application will terminate first.
     */
    private static int saveUser(User user)
    {
        try
        {
            //Prepare query
            String query = "INSERT INTO users (username, password, userType, email, allowedRentals, " +
                    "currentRentals, lateFee, allowedToRent, deleted) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            String[] params = {
                    user.getUsername(),
                    user.getPassword(),
                    user.getUserType().toString(),
                    user.getEmail(),
                    String.valueOf(user.getAllowedRentals()),
                    String.valueOf(user.getCurrentRentals()),
                    String.valueOf(user.getLateFee()),
                    user.isAllowedToRent() ? "1" : "0",
                    user.isDeleted() ? "1" : "0"
            };

            //Execute query and get the generated userID, using try-with-resources
            try (QueryResult queryResult =
                         DatabaseHandler.executePreparedQuery(query, params, Statement.RETURN_GENERATED_KEYS))
            {
                ResultSet generatedKeys = queryResult.getStatement().getGeneratedKeys();
                if (generatedKeys.next())
                {
                    return generatedKeys.getInt(1);
                }
            }
        }
        catch (SQLException e)
        {
            ExceptionHandler.HandleFatalException("Failed to save user to database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        //Not reachable, but needed for compilation
        return 0;
    }

    /**
     * Retrieves a user from the database by the specified userID.
     * <p>
     * Does not retrieve deleted users.
     *
     * @param userID the ID of the user to retrieve
     * @return the User object representing the retrieved user, or null if not found
     * @throws InvalidIDException if the provided userID is invalid
     */
    public static User getUserByID(int userID)
    throws InvalidIDException
    {
        return getUserByID(userID, false);
    }

    /**
     * Retrieves a user from the database by the specified userID, with an option to include deleted users.
     *
     * @param userID     the ID of the user to retrieve
     * @param getDeleted specifies whether to include deleted users in the retrieval
     * @return the User object representing the retrieved user, or null if not found
     * @throws InvalidIDException if the provided userID is invalid
     */
    public static User getUserByID(int userID, boolean getDeleted) //TODO-test
    throws InvalidIDException
    {
        try
        {
            //No point getting invalid Users, throws InvalidIDException
            checkValidUserID(userID);

            //Prepare a SQL query to select a user by userID. Include deleted condition based on getDeleted
            String query = getDeleted ?
                    "SELECT * FROM users WHERE userID = ?" :
                    "SELECT * FROM users WHERE userID = ? AND deleted = false";
            String[] params = {String.valueOf(userID)};

            //Execute the query and store the result in a ResultSet.
            try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params))
            {
                ResultSet resultSet = queryResult.getResultSet();
                //If the ResultSet contains data, create a new User object using the retrieved username and password,
                //and set the user's userID.
                if (resultSet.next())
                {
                    return new User(
                            userID,
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("email"),
                            User.UserType.valueOf(resultSet.getString("userType")),
                            resultSet.getInt("allowedRentals"),
                            resultSet.getInt("currentRentals"),
                            resultSet.getFloat("lateFee"),
                            resultSet.getBoolean("allowedToRent"),
                            resultSet.getBoolean("deleted"));
                }
            }
        }
        catch (SQLException | ConstructionException e)
        {
            ExceptionHandler.HandleFatalException("Failed to retrieve user by ID from database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        //Return null if not found.
        return null;
    }

    // DELETE AND RECOVER ----------------------------------------------------------------------------------------------

    /**
     * Deletes a user by marking them as deleted in the database.
     * Sets the 'deleted' field to true and 'allowedToRent' field to false for the specified user.
     * Does not remove username and email from lists due to integrity.
     *
     * @param userToDelete the user to be deleted
     * @throws DeletionException if an error occurs during the deletion process
     */
    //TODO-PRIO if userToDelete != STAFF, ADMIN, currentUser must be STAFF or ADMIN and must be validated
    //TODO-PRIO userToDelete == STAFF, ADMIN, currentUser must be ADMIN and must be validated
    public static void deleteUser(User userToDelete) //TODO-test //TODO-comment
    throws DeletionException
    {
        try
        {
            //Validate user, throws NullEntityException/EntityNotFoundException
            validateUserObject(userToDelete);

            //Check if user has lateFee or currentRentals, throws InvalidUserRentalsException/InvalidLateFeeException
            validateAllowedToDeleteUser(userToDelete);

            //Prepare a SQL command to set deleted to true for the specified user.
            String sql = "UPDATE users SET allowedToRent = 0, deleted = 1 WHERE userID = ?";
            String[] params = {String.valueOf(userToDelete.getUserID())};

            //Execute the update.
            DatabaseHandler.executePreparedUpdate(sql, params);

            //Update the deleted field of the user object
            userToDelete.setDeleted(true);

            //Update allowedToRent field
            userToDelete.setAllowedToRent(false);
        }
        catch (NullEntityException | EntityNotFoundException | InvalidIDException | InvalidRentalStatusChangeException
               | InvalidUserRentalsException | InvalidLateFeeException e)
        {
            throw new DeletionException("Failed to delete User due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Recovers a deleted user by setting their 'deleted' field to false in the database.
     * Updates the 'deleted' and 'allowedToRent' fields of the specified user based on their recovery eligibility.
     *
     * @param userToRecover the user to be recovered
     * @throws RecoveryException if an error occurs during the recovery process
     */
    //TODO-PRIO if userToRecover != STAFF or ADMIN, currentUser must be STAFF or ADMIN and must be validated
    //TODO-PRIO userToRecover == STAFF or ADMIN, currentUser must be ADMIN and must be validated
    public static void recoverUser(User userToRecover) //TODO-test //TODO-comment
    throws RecoveryException
    {
        try
        {
            //Validate user
            validateRecoverableUserObject(userToRecover);

            //Update the deleted field of the user object
            userToRecover.setDeleted(false);

            //Update allowedToRent field
            if ((userToRecover.getLateFee() == 0) && (userToRecover.getCurrentRentals() < userToRecover.getAllowedRentals()))
                userToRecover.setAllowedToRent(true);

            //Prepare a SQL command to set deleted to false for the specified user.
            String sql = "UPDATE users SET allowedToRent = ?, deleted = 0 WHERE userID = ?";
            String[] params = {userToRecover.isAllowedToRent() ? "1" : "0",
                    String.valueOf(userToRecover.getUserID())};

            //Execute the update.
            DatabaseHandler.executePreparedUpdate(sql, params);
        }
        catch (NullEntityException | EntityNotFoundException | InvalidIDException |
               InvalidRentalStatusChangeException e)
        {
            throw new RecoveryException("Failed to recover User due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Performs a hard delete of a user by removing their data from the database.
     * This operation is irreversible and removes the user's data completely.
     * Performs necessary validations to ensure the user can be hard deleted.
     *
     * @param userToDelete the user to be hard deleted
     * @throws DeletionException if an error occurs during the hard deletion process
     */
    //TODO-PRIO if userToDelete != STAFF, ADMIN, currentUser must be ADMIN and must be validated
    //TODO-PRIO userToDelete == STAFF, ADMIN, currentUser must be ADMIN and must be validated
    public static void hardDeleteUser(
            User userToDelete) //TODO-test //TODO-prio handle cascades in rentals //TODO-comment
    throws DeletionException
    {
        try
        {
            //Validate the input. Throws NullEntityException and EntityNotFoundException
            validateDeletableUserObject(userToDelete);

            //Validate that userToDelete has no current rentals and no late fee. Throws InvalidUserRentalsException
            validateAllowedToDeleteUser(userToDelete); // and InvalidLateFeeException

            //Retrieve the unique username and email
            String username = userToDelete.getUsername();
            String email = userToDelete.getEmail();

            //Prepare a SQL command to delete a userToDelete by userID.
            String sql = "DELETE FROM users WHERE userID = ?";
            String[] params = {String.valueOf(userToDelete.getUserID())};

            //Execute the update.
            DatabaseHandler.executePreparedUpdate(sql, params);

            //Set booleans
            userToDelete.setDeleted(true);
            userToDelete.setAllowedToRent(false);

            //Remove the deleted userToDelete's username and email from the lists
            storedUsernames.remove(username);
            registeredEmails.remove(email);
        }
        catch (EntityNotFoundException | NullEntityException | InvalidIDException |
               InvalidRentalStatusChangeException | InvalidUserRentalsException | InvalidLateFeeException e)
        {
            throw new DeletionException("Failed to delete userToDelete from database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    //UPDATE -----------------------------------------------------------------------------------------------------------

    public static void updateUser(User updatedUser) //TODO-test //TODO-fix
    throws NullEntityException, UpdateException
    {
        try
        {
            //Validate input
            validateUserObject(updatedUser);

            //Validate email and username not taken
            validateUpdatableUser(updatedUser);

            //Retrieve old username
            User oldUser = getUserByID(updatedUser.getUserID());
            String oldUsername = oldUser.getUsername();
            String oldEmail = oldUser.getEmail();

            //If username has been changed...
            if (!updatedUser.getUsername().equals(oldUsername))
            {
                //... remove old username from and add new username to storedUsernames
                storedUsernames.remove(oldUsername);
                storedUsernames.add(updatedUser.getUsername());
            }

            //If email has been changed
            if (!updatedUser.getEmail().equals(oldEmail))
            {
                //... remove old email from and add new email to registeredEmails
                registeredEmails.remove(oldEmail);
                registeredEmails.add(updatedUser.getEmail());
            }

            //Prepare a SQL command to update a updatedUser's data by userID.
            String sql = "UPDATE users SET username = ?, password = ?, userType = ?, email = ?, allowedRentals = ?, " +
                    "currentRentals = ?, lateFee = ?, allowedToRent = ? WHERE userID = ?";
            String[] params = {
                    updatedUser.getUsername(),
                    updatedUser.getPassword(),
                    updatedUser.getUserType().toString(),
                    updatedUser.getEmail(),
                    String.valueOf(updatedUser.getAllowedRentals()),
                    String.valueOf(updatedUser.getCurrentRentals()),
                    String.valueOf(updatedUser.getLateFee()),
                    updatedUser.isAllowedToRent() ? "1" : "0",
                    String.valueOf(updatedUser.getUserID())
            };

            //Execute the update.
            DatabaseHandler.executePreparedUpdate(sql, params);
        }
        catch (InvalidIDException | InvalidNameException | EntityNotFoundException | InvalidEmailException e)
        {
            throw new UpdateException("Failed to update user in database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    private static void validateUpdatableUser(User updatedUser)
    throws InvalidIDException, InvalidNameException, InvalidEmailException
    {
        User oldUser = getUserByID(updatedUser.getUserID());

        //If username is different
        if (!updatedUser.getUsername().equals(oldUser.getUsername()))
            if (storedUsernames.contains(updatedUser.getUsername()))
                throw new InvalidNameException("Cannot update username; username " + updatedUser.getUsername() +
                        " already taken.");

        //If email is different
        if (!updatedUser.getEmail().equals(oldUser.getEmail()))
            if (registeredEmails.contains(updatedUser.getEmail()))
                throw new InvalidEmailException("Cannot update email; email " + updatedUser.getEmail() +
                        " already taken.");
    }

    //VALIDATION STUFF -----------------------------------------------------------------------------------------------

    /**
     * Basic login method. Checks whether username exists in storedUsernames. If it does, check whether password
     * matches that user's password.
     *
     * @param username the username attempting to login
     * @param password the password attempting to login
     * @return true if successful, otherwise false
     */
    public static boolean login(String username, String password) //TODO-test //TODO-deleted
    throws UserValidationException
    {
        try
        {
            //No point verifying empty strings, throws UsernameEmptyException
            checkEmptyUsername(username);
            //Throws PasswordEmptyException
            checkEmptyPassword(password);

            //First check list
            if (!storedUsernames.contains(username))
                throw new EntityNotFoundException("User " + username + " does not exist.");

            String query = "SELECT password FROM users WHERE username = ?";
            String[] params = {username};

            //Execute the query and check if the input password matches the retrieved password
            try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params))
            {
                ResultSet resultSet = queryResult.getResultSet();
                if (resultSet.next())
                {
                    String storedPassword = resultSet.getString("password");
                    if (password.equals(storedPassword))
                    {
                        return true;
                    }
                }
            }
        }
        catch (SQLException e)
        {
            ExceptionHandler.HandleFatalException("Login failed due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
        catch (EntityNotFoundException | InvalidNameException | InvalidPasswordException e)
        {
            throw new UserValidationException("Login failed: " + e.getMessage(), e);
        }

        //Incorrect password
        return false;
    }

    /**
     * Validates the user's password.
     * <p>
     * This method compares the password provided as an argument with the password stored in the User object.
     * If the provided password matches the stored password, the method returns true. Otherwise, it returns false.
     *
     * @param user     The User object whose password is to be validated.
     * @param password The password to validate.
     * @return boolean Returns true if the provided password matches the User's stored password, false otherwise.
     */
    public static boolean validate(User user, String password) //TODO-test //TODO-deleted
    throws UserValidationException
    {
        try
        {
            validateUserObject(user);
            checkEmptyPassword(password);
        }
        catch (EntityNotFoundException | InvalidIDException | NullEntityException | InvalidPasswordException e)
        {
            throw new UserValidationException("Validation failed due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
        return user.getPassword().equals(password);
    }

    //RETRIEVING -------------------------------------------------------------------------------------------------------

    public static User getUserByUsername(String username)
    throws InvalidNameException
    {
        return getUserByUsername(username, false);
    }


    public static User getUserByUsername(String username, boolean getDeleted) //TODO-test
    throws InvalidNameException
    {
        try
        {
            //No point in getting invalid Users, throws InvalidNameException
            checkEmptyUsername(username);

            //Prepare a SQL query to select a user by username. Include deleted condition based on getDeleted
            String query = getDeleted ?
                    "SELECT * FROM users WHERE username = ?" :
                    "SELECT * FROM users WHERE username = ? AND deleted = false";

            String[] params = {username};

            //Execute the query and store the result in a ResultSet
            try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params))
            {
                ResultSet resultSet = queryResult.getResultSet();
                //If the ResultSet contains data, create a new User object using the retrieved username and password,
                //and set the user's userID
                if (resultSet.next())
                {
                    return new User(
                            resultSet.getInt("userID"),
                            username,
                            resultSet.getString("password"),
                            resultSet.getString("email"),
                            User.UserType.valueOf(resultSet.getString("userType")),
                            resultSet.getInt("allowedRentals"),
                            resultSet.getInt("currentRentals"),
                            resultSet.getFloat("lateFee"),
                            resultSet.getBoolean("allowedToRent"),
                            resultSet.getBoolean("deleted"));
                }
            }
        }
        catch (SQLException | ConstructionException e)
        {
            ExceptionHandler.HandleFatalException("Failed to retrieve user by username from database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        //Return null if not found
        return null;
    }

    //TODO OPTIONAL
    public static List<User> getUsersByFirstname(String firstname)
    {
        //Invalid firstname
        //No such Users
        //One valid user
        //Multiple valid Users
        //== 4
        return null;
    }

    //TODO OPTIONAL
    public static List<User> getUsersByLastname(String lastname)
    {
        //Invalid lastname
        //No such Users
        //One valid user
        //Multiple valid Users
        //== 4
        return null;
    }

    //TODO OPTIONAL
    public static User getUserByEmail(String email)
    {
        //Invalid email
        //No such user
        //Valid user
        //== 3
        return null;
    }

    //UTILITY METHODS---------------------------------------------------------------------------------------------------

    //TODO-comment
    private static void validateUsername(String username)
    throws InvalidNameException
    {
        checkEmptyUsername(username);
        checkUsernameTaken(username);
        checkUsernameLength(username);
    }

    /**
     * Checks whether a given username is null or empty. If so, throws an UsernameEmptyException
     * which must be handled.
     *
     * @param username the username to check.
     * @throws InvalidNameException if username is null or empty.
     */
    private static void checkEmptyUsername(String username)
    throws InvalidNameException
    {
        if (username == null || username.isEmpty())
        {
            throw new InvalidNameException("Username is null or empty.");
        }
    }

    /**
     * Checks if a given username exists in the list of usernames. If so, throws a UsernameTakenException
     * which must be handled.
     *
     * @param username the username.
     * @throws InvalidNameException if the username already exists in storedTitles.
     */
    private static void checkUsernameTaken(String username)
    throws InvalidNameException
    {
        if (storedUsernames.contains(username))
            throw new InvalidNameException("Username " + username + " already taken.");
    }

    //TODO-comment
    private static void checkUsernameLength(String username)
    throws InvalidNameException
    {
        if (username.length() < User.MIN_USERNAME_LENGTH)
            throw new InvalidNameException("Username too short, must be at least " + User.MIN_USERNAME_LENGTH +
                    " characters. Received: " + username);
        if (username.length() > User.MAX_USERNAME_LENGTH)
            throw new InvalidNameException("Username too long, must be at most " + User.MAX_USERNAME_LENGTH +
                    " characters. Received: " + username);
    }


    //TODO-comment
    private static void validatePassword(String password)
    throws InvalidPasswordException
    {
        checkEmptyPassword(password);
        checkPasswordLength(password);
    }

    /**
     * Checks whether a given password is null or empty. If so, throws an PasswordEmptyException
     * which must be handled.
     *
     * @param password the password to check.
     * @throws InvalidPasswordException if password is null or empty.
     */
    private static void checkEmptyPassword(String password)
    throws InvalidPasswordException
    {
        if (password == null || password.isEmpty())
            throw new InvalidPasswordException("Password is empty.");
    }

    //TODO-comment
    private static void checkPasswordLength(String password)
    throws InvalidPasswordException
    {
        if (password.length() < User.MIN_PASSWORD_LENGTH)
            throw new InvalidPasswordException("Password too short, must be at least " + User.MIN_PASSWORD_LENGTH +
                    " characters. Received: " + password.length());
        if (password.length() > User.MAX_PASSWORD_LENGTH)
            throw new InvalidPasswordException("Password too long, must be at most " + User.MAX_PASSWORD_LENGTH +
                    " characters. Received: " + password.length());
    }

    //TODO-comment
    private static void validateEmail(String email)
    throws InvalidEmailException
    {
        checkEmailEmpty(email);
        checkEmailTaken(email);
        checkEmailLength(email);
    }


    //TODO-comment
    private static void checkEmailEmpty(String email)
    throws InvalidEmailException
    {
        if (email == null || email.isEmpty())
            throw new InvalidEmailException("Email cannot be null or empty.");
    }

    //TODO-comment
    private static void checkEmailTaken(String email)
    throws InvalidEmailException
    {
        if (registeredEmails.contains(email))
            throw new InvalidEmailException("Email is already registered.");
    }

    //TODO-comment
    private static void checkEmailLength(String email)
    throws InvalidEmailException
    {
        if (email.length() < User.MIN_EMAIL_LENGTH)
            throw new InvalidEmailException("Email cannot be shorter than " + User.MIN_EMAIL_LENGTH + " characters. " +
                    "Received: " + email.length());
        if (email.length() > User.MAX_EMAIL_LENGTH)
            throw new InvalidEmailException("Email cannot be longer than " + User.MAX_EMAIL_LENGTH + " characters. " +
                    "Received: " + email.length());
    }

    //TODO-comment
    private static void validateUserType(User.UserType userType)
    throws InvalidTypeException
    {
        if (userType == null)
            throw new InvalidTypeException("User Type cannot be null.");
    }

    //TODO-comment
    private static void validateUserObject(User user)
    throws EntityNotFoundException, InvalidIDException, NullEntityException
    {
        checkNullUser(user);
        int ID = user.getUserID();
        if (UserHandler.getUserByID(ID) == null)
            throw new EntityNotFoundException("User with ID " + user + "not found in database.");
    }

    private static void validateDeletableUserObject(User user)
    throws EntityNotFoundException, InvalidIDException, NullEntityException
    {
        checkNullUser(user);
        int ID = user.getUserID();
        if (UserHandler.getUserByID(ID, true) == null)
            throw new EntityNotFoundException("User with ID " + user + "not found in database.");
    }

    private static void validateRecoverableUserObject(User user)
    throws EntityNotFoundException, InvalidIDException, NullEntityException
    {
        checkNullUser(user);
        int ID = user.getUserID();
        if (UserHandler.getUserByID(ID, true) == null)
            throw new EntityNotFoundException("User with ID " + user + "not found in database.");
    }

    /**
     * Checks if a given user is null. If so, throws a NullEntityException which must be handled.
     *
     * @param user the user.
     * @throws NullEntityException if the user is null.
     */
    private static void checkNullUser(User user)
    throws NullEntityException
    {
        if (user == null)
            throw new NullEntityException("User is null.");
    }

    /**
     * Checks whether a given userID is invalid (<= 0). If so, throws an InvalidIDException
     * which must be handled by the caller.
     *
     * @param userID the userID to validate.
     * @throws InvalidIDException if userID <= 0.
     */
    private static void checkValidUserID(int userID)
    throws InvalidIDException
    {
        if (userID <= 0)
        {
            throw new InvalidIDException("Invalid userID: " + userID);
        }
    }

    private static void validateAllowedToDeleteUser(User userToDelete)
    throws InvalidUserRentalsException, InvalidLateFeeException
    {
        if (userToDelete.getLateFee() > 0)
            throw new InvalidLateFeeException("User with late fee cannot be deleted. Late fee: " +
                    userToDelete.getLateFee());
        if (userToDelete.getCurrentRentals() > 0)
            throw new InvalidUserRentalsException("User with current rentals cannot be deleted. Current rentals: " +
                    userToDelete.getCurrentRentals());
    }
}