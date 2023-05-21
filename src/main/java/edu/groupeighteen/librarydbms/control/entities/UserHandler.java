package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.control
 * @contact matfir-1@student.ltu.se
 * @date 4/5/2023
 *
 * This class contains database CRUD operation methods as well as other methods related to the User entity class.
 * It contains a list of all usernames for quicker validation.
 *
 * Note on Exceptions:
 *
 * "Exceptions should only be thrown in exceptional circumstances, and invalid user input is not exceptional".
 *
 * I've battled this one for long, but if finally clicked. This class is NOT handling user input. That is going
 * to be handled in UserCreateGUI. When I press the "Create User" button in that class, we perform an instant
 * check on whether the title, and any other needed fields, are empty.
 *
 * If so, we print an error message, reset all fields in the GUI and wait for new input.
 *
 * Meaning, createNewUser (as an example) should NEVER be called with an empty or null String as argument.
 * If it is, that IS exceptional.
 */
public class UserHandler {
    //Not needed, but very practical, since usernames must be unique
    private static final ArrayList<String> storedUsernames = new ArrayList<>();

    /**
     * Performs setup tasks. In this case, syncing storedUsernames against the database.
     */
    public static void setup() {
        syncUsernames();
    }

    public static void reset() {
        storedUsernames.clear();
    }

    /**
     * Syncs the storedUsernames list against the usernames in the users table.
     */
    public static void syncUsernames() {
        if (!storedUsernames.isEmpty())
            storedUsernames.clear();
        retrieveUsernamesFromTable();
    }

    /**
     * Method that retrieves the usernames in the users table and stores them in the static ArrayList.
     * Query needs to be ORDER BY user_id ASC or ids will be in the order of 10, 1, 2, ...
     */
    private static void retrieveUsernamesFromTable() {
        try {
            // Execute the query to retrieve all usernames
            String query = "SELECT username FROM users ORDER BY userID ASC";
            try (QueryResult result = DatabaseHandler.executeQuery(query)) {

                // Add the retrieved usernames to the ArrayList
                while (result.getResultSet().next()) {
                    storedUsernames.add(result.getResultSet().getString("username"));
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException(e);
        }
    }

    /**
     * Prints all usernames in the ArrayList.
     */
    public static void printUsernames() {
        System.out.println("\nUsernames:");
        int num = 1;
        for (String username : storedUsernames) {
            System.out.println(num + ": " + username);
            num++;
        }
    }

    /**
     * Returns the ArrayList of usernames.
     * @return the ArrayList of usernames
     */
    public static ArrayList<String> getStoredUsernames() {
        return storedUsernames;
    }

    //TODO-prio update when User class is finished
    /**
     * Prints all non-sensitive data for all Users in a list.
     * @param userList the list of User objects.
     */
    public static void printUserList(List<User> userList) {
        System.out.println("Users:");
        int count = 1;
        for (User user : userList) {
            System.out.println(count + " userID: " + user.getUserID() + ", username: " + user.getUsername());
        }
    }

    //CRUD stuff ------------------------------------------------------------------------------------------------------

    /**
     * Creates a new user with the specified username and password. The method first checks if the provided username is
     * already taken. If the username is unique, a new User object is created and saved to the database. The User's ID
     * from the database is set in the User object before it is returned. The method also handles any potential
     * InvalidPasswordException or InvalidUserIDException.
     *
     * @param username The username for the new user.
     * @param password The password for the new user.
     * @return A User object representing the newly created user.
     * @throws InvalidUsernameException If the provided username is already taken or incorrect.
     * @throws InvalidPasswordException If the provided password is incorrect.
     */
    public static User createNewUser(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        // Usernames must be unique, throws UsernameTakenException
        checkUsernameTaken(username);
        User newUser = null;

        try {
            // Create and save the new user, retrieving the ID
            newUser = new User(username, password);
            newUser.setUserID(saveUser(newUser));

            // Need to remember to add to the list
            storedUsernames.add(newUser.getUsername());
        } catch (InvalidUserIDException e) {
            ExceptionHandler.HandleFatalException(e);
        }

        return newUser;
    }

    /**
     * Saves a user to the database. The method prepares an SQL insert query with the user's details such as
     * username, password, allowed rentals, current rentals and late fee. The query is executed and the
     * auto-generated user ID from the database is retrieved and returned. If the query execution fails, the method
     * handles the SQLException and returns 0.
     *
     * @param user The user object to be saved.
     * @return The auto-generated ID of the user from the database.
     *          Returns 0 if an SQLException occurs. This won't happen because the exception will be thrown first.
     */
    private static int saveUser(User user) {
        try {
            // Prepare query
            String query = "INSERT INTO users (username, password, allowedRentals, currentRentals, lateFee) " +
                    "VALUES (?, ?, ?, ?, ?)";

            String[] params = {
                    user.getUsername(),
                    user.getPassword(),
                    String.valueOf(user.getAllowedRentals()),
                    String.valueOf(user.getCurrentRentals()),
                    String.valueOf(user.getLateFee())
            };

            // Execute query and get the generated userID, using try-with-resources
            try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params, Statement.RETURN_GENERATED_KEYS)) {
                ResultSet generatedKeys = queryResult.getStatement().getGeneratedKeys();
                if (generatedKeys.next()) return generatedKeys.getInt(1);
                else throw new SQLException("Failed to insert the user, no ID obtained.");
            }
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException(e);
        }
        return 0; // This should never happen
    }

    /**
     * Retrieves a User object from the database using the provided userID. The method first validates the provided
     * userID. It then prepares and executes an SQL query to select the user's details from the database. If a user
     * with the provided userID exists, a new User object is created with the retrieved details and returned.
     *
     * @param userID The userID of the user to be retrieved.
     * @return A User object representing the user with the provided userID. Returns null if the user does not exist.
     * @throws InvalidUserIDException If the provided userID is invalid.
     */
    public static User getUserByID(int userID) throws InvalidUserIDException {
        // No point getting invalid users, throws InvalidUserIDException
        checkValidUserID(userID);

        // Prepare a SQL query to select a user by userID.
        String query = "SELECT username, password, allowedRentals, currentRentals, lateFee FROM users WHERE userID = ?";
        String[] params = {String.valueOf(userID)};

        // Execute the query and store the result in a ResultSet.
        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params)) {
            ResultSet resultSet = queryResult.getResultSet();
            // If the ResultSet contains data, create a new User object using the retrieved username and password,
            // and set the user's userID.
            if (resultSet.next()) {
                User user = new User(resultSet.getString("username"), resultSet.getString("password"));
                user.setUserID(userID);
                user.setCurrentRentals(resultSet.getInt("currentRentals"));
                user.setLateFee(resultSet.getFloat("lateFee"));
                return user;
            }
        } catch (SQLException | InvalidRentalException | InvalidLateFeeException | InvalidUsernameException | InvalidPasswordException e) {
            ExceptionHandler.HandleFatalException(e);
        }

        // Return null if not found.
        return null;
    }

    /**
     * Retrieves a User object from the database using the provided username. The method first validates the provided
     * username. It then prepares and executes an SQL query to select the user's details from the database. If a user
     * with the provided username exists, a new User object is created with the retrieved details and returned.
     *
     * @param username The username of the user to be retrieved.
     * @return A User object representing the user with the provided username. Returns null if the user does not exist.
     * @throws InvalidUsernameException If the provided username is empty.
     */
    public static User getUserByUsername(String username) throws InvalidUsernameException {
        // No point in getting invalid users, throws InvalidUsernameException
        checkEmptyUsername(username);

        // Prepare a SQL query to select a user by username
        String query = "SELECT userID, password, allowedRentals, currentRentals, lateFee FROM users WHERE username = ?";
        String[] params = {username};

        // Execute the query and store the result in a ResultSet
        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params)) {
            ResultSet resultSet = queryResult.getResultSet();
            // If the ResultSet contains data, create a new User object using the retrieved username and password,
            // and set the user's userID
            if (resultSet.next()) {
                User user = new User(username, resultSet.getString("password"));
                user.setUserID(resultSet.getInt("userID"));
                user.setCurrentRentals( resultSet.getInt("currentRentals"));
                user.setLateFee(resultSet.getFloat("lateFee"));
                return user;
            }
        } catch (SQLException | InvalidUsernameException | InvalidPasswordException | InvalidUserIDException | InvalidRentalException | InvalidLateFeeException e) {
            ExceptionHandler.HandleFatalException(e);
        }

        // Return null if not found
        return null;
    }

    public static User getUserByEmail(String email) {
        //Invalid email
        //No such user
        //Valid user
        // == 3
        return null;
    }

    public static List<User> getUsersByFirstname(String firstname) {
        //Invalid firstname
        //No such users
        //One valid user
        //Multiple valid users
        // == 4
        return null;
    }

    public static List<User> getUsersByLastname(String lastname) {
        //Invalid lastname
        //No such users
        //One valid user
        //Multiple valid users
        // == 4
        return null;
    }

    // == 11


    /**
     * Updates the data of an existing user in the database with the data of the provided User object. Before updating,
     * the method validates that the provided User object is not null and the old username is not empty. If the username of
     * the provided User object differs from the old username, the method checks if the new username is taken and updates
     * the username in the storedUsernames list if it isn't. The method then prepares an SQL command to update the user's
     * data in the database and executes the update.
     *
     * @param newUser The User object containing the updated user data.
     * @param oldUsername The old username of the user before the update.
     * @throws UserNullException If the provided User object is null.
     * @throws InvalidUsernameException If the provided oldUsername is empty.
     */
    public static void updateUser(User newUser, String oldUsername) throws UserNullException, InvalidUsernameException {
        //We can't create user objects with invalid usernames, so only need to validate user itself. Throws UserNullException
        checkNullUser(newUser);
        //Old username could be empty or null though. Throws InvalidUsernameException
        checkEmptyUsername(oldUsername);

        //Let's check if the user exists in the database before we go on, this is fatal
        try {
            UserHandler.getUserByID(newUser.getUserID());
        } catch (InvalidUserIDException e) {
            ExceptionHandler.HandleFatalException(new UserNotFoundException("Unable to update User: User with ID " + newUser.getUserID() + " not found."));
        }

        //If username has been changed...
        if (!newUser.getUsername().equals(oldUsername)) {
            //... and is taken. Throws UsernameTakenException
            checkUsernameTaken(newUser.getUsername());
            //... and is not taken: remove old username from and add new username to storedUsernames
            storedUsernames.remove(oldUsername);
            storedUsernames.add(newUser.getUsername());
        }

        // Prepare a SQL command to update a newUser's data by userID.
        String sql = "UPDATE users SET username = ?, password = ?, currentRentals = ?, lateFee = ? WHERE userID = ?";
        String[] params = {
                newUser.getUsername(),
                newUser.getPassword(),
                String.valueOf(newUser.getCurrentRentals()),
                String.valueOf(newUser.getLateFee()),
                String.valueOf(newUser.getUserID())
        };

        // Execute the update.
        DatabaseHandler.executePreparedUpdate(sql, params);
    }

    /**
     * Deletes a user from the database. Before deleting, the method checks if the provided User object is not null and if the
     * user with the ID of the provided User object exists in the database. If the user exists, the method prepares an SQL
     * command to delete the user from the database and executes the command. The username of the deleted user is then removed
     * from the storedUsernames list.
     *
     * @param user The User object representing the user to be deleted.
     * @throws UserNullException If the provided User object is null.
     * @throws UserNotFoundException If the user with the ID of the provided User object doesn't exist in the database.
     */
    public static void deleteUser(User user) throws UserNotFoundException, UserNullException {
        //Validate the input. Throws UserNullException
        checkNullUser(user);

        //Let's check if the user exists in the database before we go on, this can be fatal
        try {
            if (UserHandler.getUserByID(user.getUserID()) == null)
                throw new UserNotFoundException("Unable to update User: User with ID " + user.getUserID() + " not found.");
        } catch (InvalidUserIDException e) {
            ExceptionHandler.HandleFatalException(e);
        }

        //Prepare a SQL command to delete a user by userID.
        String sql = "DELETE FROM users WHERE userID = ?";
        String[] params = {String.valueOf(user.getUserID())};

        //Execute the update. //TODO-prio handle cascades in rentals
        DatabaseHandler.executePreparedUpdate(sql, params);

        //Remove the deleted user's username from the usernames ArrayList.
        storedUsernames.remove(user.getUsername());
    }

    // VALIDATION STUFF -----------------------------------------------------------------------------------------------

    /**
     * Basic login method. Checks whether username exists in storedUsernames. If it does, check whether password
     * matches that user's password.
     * @param username the username attempting to login
     * @param password the password attempting to login
     * @return true if successful, otherwise false
     * @throws InvalidUsernameException if the provided password is empty.
     * @throws InvalidUsernameException if the provided password is empty.
     */
    public static boolean login(String username, String password) throws InvalidUsernameException, InvalidPasswordException, UserNotFoundException {
        // No point verifying empty strings, throws UsernameEmptyException
        checkEmptyUsername(username);
        //Throws PasswordEmptyException
        checkEmptyPassword(password);

        //First check list
        if (!storedUsernames.contains(username))
            throw new UserNotFoundException("Login failed: User " + username + " does not exist.");

        //TODO-future add checking against database

        boolean isAuthenticated = false;

        String query = "SELECT password FROM users WHERE username = ?";
        String[] params = {username};

        // Execute the query and check if the input password matches the retrieved password
        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params)) {
            ResultSet resultSet = queryResult.getResultSet();
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                if (password.equals(storedPassword)) {
                    isAuthenticated = true;
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException(e);
        }

        return isAuthenticated;
    }

    /**
     * Validates the user's password.
     *
     * This method compares the password provided as an argument with the password stored in the User object.
     * If the provided password matches the stored password, the method returns true. Otherwise, it returns false.
     *
     * @param user The User object whose password is to be validated.
     * @param password The password to validate.
     * @return boolean Returns true if the provided password matches the User's stored password, false otherwise.
     * @throws UserNullException if the provided User is null.
     * @throws InvalidPasswordException if the provided password is empty.
     */
    public static boolean validateUser(User user, String password) throws UserNullException, InvalidPasswordException {
        checkNullUser(user);
        checkEmptyPassword(password);
        return user.getPassword().equals(password);
    }

    //UTILITY METHODS---------------------------------------------------------------------------------------------------

    /**
     * Checks if a given username exists in the list of usernames. If so, throws a UsernameTakenException
     * which must be handled.
     * @param username the username.
     * @throws InvalidUsernameException if the username already exists in storedTitles.
     */
    private static void checkUsernameTaken(String username) throws InvalidUsernameException {
        if (storedUsernames.contains(username))
            throw new InvalidUsernameException("Username " + username + " already taken.");
    }

    /**
     * Checks if a given user is null. If so, throws a UserNullException which must be handled.
     * @param user the user.
     * @throws UserNullException if the user is null.
     */
    private static void checkNullUser(User user) throws UserNullException {
        if (user == null)
            throw new UserNullException("User is null.");
    }

    /**
     * Checks whether a given userID is invalid (<= 0). If so, throws an InvalidUserIDException
     * which must be handled by the caller.
     * @param userID the userID to validate.
     * @throws InvalidUserIDException if userID <= 0.
     */
    private static void checkValidUserID(int userID) throws InvalidUserIDException {
        if (userID <= 0) {
            throw new InvalidUserIDException("Invalid userID: " + userID);
        }
    }

    /**
     * Checks whether a given username is null or empty. If so, throws an UsernameEmptyException
     * which must be handled.
     * @param username the username to check.
     * @throws InvalidUsernameException if username is null or empty.
     */
    private static void checkEmptyUsername(String username) throws InvalidUsernameException {
        if (username == null || username.isEmpty()) {
            throw new InvalidUsernameException("Username is null or empty.");
        }
    }

    /**
     * Checks whether a given password is null or empty. If so, throws an PasswordEmptyException
     * which must be handled.
     * @param password the password to check.
     * @throws InvalidPasswordException if password is null or empty.
     */
    private static void checkEmptyPassword(String password) throws InvalidPasswordException {
        if (password == null || password.isEmpty())
            throw new InvalidPasswordException("Password is empty.");
    }
}