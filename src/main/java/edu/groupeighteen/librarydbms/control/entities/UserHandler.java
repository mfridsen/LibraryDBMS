package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.User;

import java.sql.*;
import java.util.ArrayList;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.control
 * @contact matfir-1@student.ltu.se
 * @date 4/5/2023
 *
 * This class contains database CRUD operation methods as well as other methods related to the User entity class.
 * It contains a list of all usernames for quicker validation.
 */
public class UserHandler {
    //Not needed, but very practical, since usernames must be unique
    private static ArrayList<String> storedUsernames = new ArrayList<>();

    /**
     * Performs setup tasks. In this case, syncing storedUsernames against the database.
     * @throws SQLException If an error occurs while interacting with the database.
     */
    public static void setup() throws SQLException {
        syncUsernames();
    }

    /**
     * Syncs the storedUsernames list against the usernames in the users table.
     * @throws SQLException If an error occurs while interacting with the database.
     */
    public static void syncUsernames() throws SQLException {
        if (!storedUsernames.isEmpty())
            storedUsernames.clear();
        storedUsernames = retrieveUsernamesFromTable();
    }

    /**
     * Method that retrieves the usernames in the users table and stores them in the static ArrayList.
     * Query needs to be ORDER BY user_id ASC or ids will be in the order of 10, 1, 2, ...
     * @throws SQLException If an error occurs while interacting with the database.
     */
    public static ArrayList<String> retrieveUsernamesFromTable() throws SQLException {
        //Execute the query to retrieve all usernames
        QueryResult result = DatabaseHandler.executeQuery("SELECT username FROM users ORDER BY userID ASC");
        ArrayList<String> usernames = new ArrayList<>();

        //Add the retrieved usernames to the ArrayList
        while (result.getResultSet().next()) {
            usernames.add(result.getResultSet().getString("username"));
        }

        //Close the resources
        result.close();
        return usernames;
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

    /**
     * Basic login method. Checks whether username exists in storedUsernames. If it does, check whether password
     * matches that user's password.
     * @param username the username attempting to login
     * @param password the password attempting to login
     * @return true if successful, otherwise false
     * @throws SQLException If an error occurs while interacting with the database
     */
    public static boolean login(String username, String password) throws SQLException {
        //No point verifying empty strings
        if (username == null ||username.isEmpty() || password == null || password.isEmpty()) {
            System.err.println("Login failed: Empty username or password."); //TODO-log
            return false;
        }

        boolean isAuthenticated = false;

        if (!storedUsernames.contains(username)) {
            //TODO-future log or otherwise present the problem to the user
            //TODO-future if user is staff, will still check against database just in case
            System.err.println(username + ": no such user in list of usernames.");
        }

        String query = "SELECT password FROM users WHERE username = ?";
        String[] params = {username};

        //Execute the query and check if the input password matches the retrieved password
        QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params);
        try (queryResult) {
            ResultSet resultSet = queryResult.getResultSet();
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                if (password.equals(storedPassword)) {
                    isAuthenticated = true;
                }
            }
        }

        return isAuthenticated;
    }

    //CRUD stuff ------------------------------------------------------------------------------------------------------

    /**
     * Creates a new User with the specified username and password and saves it to the database.
     * If the User creation fails, this method returns null.
     *
     * @param username the username of the new User.
     * @param password the password of the new User.
     * @return the created User object on success, null on failure.
     * @throws SQLException If an error occurs while interacting with the database
     */
    public static User createNewUser(String username, String password) throws SQLException {
        //TODO-prio add to these two when more fields are added, as well as javadoc
        //No point creating invalid users
        if (username == null ||username.isEmpty() || password == null || password.isEmpty())
            throw new IllegalArgumentException("Empty username or password.");
        //Usernames must be unique
        if (storedUsernames.contains(username))
            throw new IllegalArgumentException("Username " + username + " already taken.");

        //Create and save the new user, retrieving the ID
        User newUser = new User(username, password);
        newUser.setUserID(saveUser(newUser));

        //Need to remember to add to the list
        storedUsernames.add(newUser.getUsername());
        return newUser;
    }

    /**
     * Saves a User object to the database.
     *
     * <p>This method attempts to insert a new user into the 'users' table. It uses the username and password
     * properties of the provided User object to populate the new record. The database is expected to generate
     * a unique ID for each new user, which is retrieved and returned by this method.</p>
     *
     * @param user The User object to be saved. This object should have a username and password set.
     * @return The unique ID generated by the database for the new user record.
     * @throws SQLException If an error occurs while interacting with the database, or if the database does not
     *         generate a new unique ID for the inserted user.
     */
    public static int saveUser(User user) throws SQLException {
        //Prepare query
        String query = "INSERT INTO users (username, password) VALUES (?, ?)"; //Update these two when more fields are added, as well as javadoc
        String[] params = {user.getUsername(), user.getPassword()}; //Update these two when more fields are added, as well as javadoc

        //Execute query and get the generated userID, using try-with-resources
        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet generatedKeys = queryResult.getStatement().getGeneratedKeys();
            if (generatedKeys.next()) return generatedKeys.getInt(1);
            else throw new SQLException("Failed to insert the user, no ID obtained.");
        }
    }

    //TODO-exception might want to throw a custom exception (like UserNotFoundException) instead of returning null,
    // to make error handling more consistent
    /**
     * Retrieves a User object from the database based on the provided user ID.
     *
     * <p>This method attempts to retrieve the user details from the 'users' table in the database
     * that correspond to the provided user ID. If a user with the given ID exists, a new User object
     * is created with the retrieved username and password, and the user's ID is set.</p>
     *
     * @param userID The unique ID of the user to be retrieved.
     * @return The User object corresponding to the provided ID, or null if no such user is found.
     * @throws SQLException If an error occurs while interacting with the database
     */
    public static User getUserByID(int userID) throws SQLException {
        //No point getting impossible users
        if (userID <= 0) {
            System.err.println("Error retrieving user by userID: invalid userID " + userID); //TODO-log
            return null;
        }

        //Prepare a SQL query to select a user by userID.
        String query = "SELECT username, password FROM users WHERE userID = ?";
        String[] params = {String.valueOf(userID)};

        //Execute the query and store the result in a ResultSet.
        QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params);
        ResultSet resultSet = queryResult.getResultSet();

        try {
            //If the ResultSet contains data, create a new User object using the retrieved username and password,
            //and set the user's userID.
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                User user = new User(username, password);
                user.setUserID(userID);
                return user;
            }
        } finally {
            //Close the resources.
            queryResult.close();
        }

        //Return null if not found.
        return null;
    }

    //TODO-exception might want to throw a custom exception (like UserNotFoundException) instead of returning null,
    // to make error handling more consistent
    /**
     * Retrieves a User object from the database based on the provided username.
     *
     * <p>This method attempts to retrieve the user details from the 'users' table in the database
     * that correspond to the provided username. If a user with the given username exists, a new User
     * object is created with the retrieved username and password.</p>
     *
     * @param username The username of the user to be retrieved.
     * @return The User object corresponding to the provided username, or null if no such user is found.
     * @throws SQLException If an error occurs while interacting with the database
     */
    public static User getUserByUsername(String username) throws SQLException {
        //No point getting invalid users
        if (username == null || username.isEmpty()) {
            System.err.println("Error retrieving user by username: empty username."); //TODO-log
            return null;
        }

        //Prepare a SQL query to select a user by username.
        String query = "SELECT userID, password FROM users WHERE username = ?";
        String[] params = {username};

        //Execute the query and store the result in a ResultSet.
        QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params);

        try {
            ResultSet resultSet = queryResult.getResultSet();
            //If the ResultSet contains data, create a new User object using the retrieved username and password,
            //and set the user's userID.
            if (resultSet.next()) {
                User user = new User(username, resultSet.getString("password"));
                user.setUserID(resultSet.getInt("userID"));
                return user;
            }
        } finally {
            //Close the resources.
            queryResult.close();
        }

        //Return null if not found.
        return null;
    }

    //TODO-future changing a username is not handled properly
    /**
     * Updates the corresponding user's record in the database with the details of the provided User object.
     *
     * This method prepares a SQL UPDATE command to modify the existing user's username and password based on
     * the User object's userID. It sets the values for the prepared statement using the User object's data and
     * executes the update.
     *
     * @param user The User object containing the updated details of the user.
     *             The user's userID should correspond to an existing user in the database.
     * @return true if the user's record was successfully updated, false otherwise.
     */
    public static boolean updateUser(User user) throws SQLException {
        //Prepare a SQL command to update a user's username and password by userID.
        String sql = "UPDATE users SET username = ?, password = ? WHERE userID = ?";
        String[] params = {user.getUsername(), user.getPassword(), String.valueOf(user.getUserID())};

        //Execute the update.
        int rowsAffected = DatabaseHandler.executeUpdate(sql, params);

        //Check if the update was successful (i.e., if any rows were affected)
        if (rowsAffected > 0) {
            //Return whether the user was updated successfully.
            return true;
        } else {
            throw new SQLException("Error updating user:");
        }
    }

    /**
     * Deletes a user from the database.
     *
     * @param user The user to delete.
     * @return true if the user was successfully deleted, false otherwise.
     */
    public static boolean deleteUser(User user) throws SQLException {
        boolean isDeleted = false;
        //Prepare a SQL command to delete a user by userID.
        String sql = "DELETE FROM users WHERE userID = ?";
        String[] params = {String.valueOf(user.getUserID())};

        //Execute the update.
        int rowsAffected = DatabaseHandler.executeUpdate(sql, params);

        //Remove the deleted user's username from the usernames ArrayList.
        if (rowsAffected > 0) {
            isDeleted = true;
            storedUsernames.remove(user.getUsername());
        }

        //Return whether the user was deleted successfully.
        return isDeleted;
    }
}