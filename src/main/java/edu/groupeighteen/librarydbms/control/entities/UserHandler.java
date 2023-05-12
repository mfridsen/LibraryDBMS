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
    //Needed since usernames must be unique
    private static ArrayList<String> storedUsernames = new ArrayList<>();
    //The code is cleaner if every Handler class stores a reference to the Connection
    private static Connection connection;

    //TODO-exception handle
    /**
     * To ensure that things are done in the correct order, only DatabaseHandler will retrieve its connection
     * on its own. The rest of the Handlers need to be assigned the connection, by calling their setup methods
     * with the connection as argument after the DatabaseHandlers setup method has been called.
     * @param con the Connection to the database.
     * @throws SQLException
     */
    public static void setup(Connection con) throws SQLException {
        connection = con;
        syncUsernames();
    }

    //TODO-future
    public static void syncUsernames() throws SQLException {
        if (!storedUsernames.isEmpty())
            storedUsernames.clear();
        storedUsernames = retrieveUsernamesFromTable();
    }

    //login stuff -----------------------------------------------------------------------------------------------------

    //TODO-exception Handle
    /**
     * Basic login method. Checks whether username exists in usernames. If it does, check whether password
     * matches that user's password.
     * @param username the username attempting to login
     * @param password the password attempting to login
     * @return true if successful, otherwise false
     */
    public static boolean login(String username, String password) {
        //No point verifying empty strings
        if (username == null ||username.equals("") || password == null || password.equals("")) {
            return false;
        }

        boolean isAuthenticated = false;

        if (!storedUsernames.contains(username)) {
            //TODO-future log or otherwise present the problem to the user
            //TODO-future this should apply to staff logins, doesn't need to apply to patrons
            System.err.println(username + ": no such user in list of usernames.");
        }

        String query = "SELECT password FROM users WHERE username = ?";
        String[] params = {username};

        // Execute the query and check if the input password matches the retrieved password
        QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params);
        try {
            ResultSet resultSet = queryResult.getResultSet();
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                if (password.equals(storedPassword)) {
                    isAuthenticated = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while logging in: " + e.getMessage());
            e.printStackTrace();
        } finally {
            queryResult.close();
        }

        return isAuthenticated;
    }

    //TODO exception handling
    /**
     * Method that retrieves the usernames in the users table and stores them in the static ArrayList.
     * Query needs to be ORDER BY user_id ASC or ids will be in the order of 10, 1, 2, ...
     * @throws SQLException
     */
    private static ArrayList<String> retrieveUsernamesFromTable() throws SQLException {
        //Execute the query to retrieve all usernames
        QueryResult result = DatabaseHandler.executeQuery("SELECT username FROM users ORDER BY userID ASC");
        ArrayList<String> usernames = new ArrayList<>();

        //Add the retrieved usernames to the ArrayList
        while (result.getResultSet().next()) {
            usernames.add(result.getResultSet().getString("username"));
        }

        // Close the resources
        result.close();
        return usernames;
    }

    /**
     * Prints all usernames in the ArrayList.
     */
    public static void printUsernames() {
        System.out.println("\nUsernames:");
        for (String username : storedUsernames) {
            System.out.println(username);
        }
    }

    /**
     * Returns the ArrayList of usernames.
     * @return the ArrayList of usernames
     */
    public static ArrayList<String> getStoredUsernames() {
        return storedUsernames;
    }

    //CRUD stuff ------------------------------------------------------------------------------------------------------

    //TODO-exception handle
    /**
     * Creates a new user with the specified username and password and saves it to the database.
     * If the user creation fails, this method returns null.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     * @return the created User object, or null if the user creation fails
     */
    public static User createNewUser(String username, String password) {
        try {
            User newUser = new User(username, password);
            newUser.setUserID(saveUser(newUser));
            storedUsernames.add(newUser.getUsername()); //Need to remember to add to the list
            return newUser;
        } catch (SQLException e) {
            System.err.println("Error creating a new user: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //TODO-exception handle
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
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        String[] params = {user.getUsername(), user.getPassword()};
        QueryResult queryResult = DatabaseHandler.executePreparedQuery(sql, params, Statement.RETURN_GENERATED_KEYS);

        // Get the generated userID
        ResultSet generatedKeys = queryResult.getStatement().getGeneratedKeys();
        if (generatedKeys.next()) {
            int id = generatedKeys.getInt(1);
            queryResult.close();
            return id;
        } else {
            queryResult.close();
            throw new SQLException("Failed to insert the user, no ID obtained.");
        }
    }

    //TODO-exception handle
    /**
     * Retrieves a User object from the database based on the provided user ID.
     *
     * <p>This method attempts to retrieve the user details from the 'users' table in the database
     * that correspond to the provided user ID. If a user with the given ID exists, a new User object
     * is created with the retrieved username and password, and the user's ID is set.</p>
     *
     * @param userID The unique ID of the user to be retrieved.
     * @return The User object corresponding to the provided ID, or null if no such user is found.
     */
    public static User getUserByID(int userID) {
        //Prepare a SQL query to select a user by userID.
        String query = "SELECT username, password FROM users WHERE userID = ?";
        String[] params = {String.valueOf(userID)};

        //Execute the query and store the result in a ResultSet.
        QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params);

        try {
            ResultSet resultSet = queryResult.getResultSet();
            //If the ResultSet contains data, create a new User object using the retrieved username and password,
            // and set the user's userID.
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                User user = new User(username, password);
                user.setUserID(userID);
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user by userID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            //Close the resources.
            queryResult.close();
        }

        //Return null if not found.
        return null;
    }

    //TODO-exception handle
    /**
     * Retrieves a User object from the database based on the provided username.
     *
     * <p>This method attempts to retrieve the user details from the 'users' table in the database
     * that correspond to the provided username. If a user with the given username exists, a new User
     * object is created with the retrieved username and password.</p>
     *
     * @param username The username of the user to be retrieved.
     * @return The User object corresponding to the provided username, or null if no such user is found.
     */
    public static User getUserByUsername(String username) {
        // Prepare a SQL query to select a user by username.
        String query = "SELECT userID, password FROM users WHERE username = ?";
        String[] params = {username};

        // Execute the query and store the result in a ResultSet.
        QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params);

        try {
            ResultSet resultSet = queryResult.getResultSet();
            // If the ResultSet contains data, create a new User object using the retrieved username and password,
            // and set the user's userID.
            if (resultSet.next()) {
                User user = new User(username, resultSet.getString("password"));
                user.setUserID(resultSet.getInt("userID"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user by username: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the resources.
            queryResult.close();
        }

        // Return null if not found.
        return null;
    }

    //TODO-exception handle
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
    public static boolean updateUser(User user) {
        boolean isUpdated = false;

        // Prepare a SQL command to update a user's username and password by userID.
        String sql = "UPDATE users SET username = ?, password = ? WHERE userID = ?";
        String[] params = {user.getUsername(), user.getPassword(), String.valueOf(user.getUserID())};

        try {
            // Execute the update.
            int rowsAffected = DatabaseHandler.executeUpdate(sql, params);

            // Check if the update was successful (i.e., if any rows were affected)
            if (rowsAffected > 0) {
                isUpdated = true;
                storedUsernames.remove(user.getUsername());
            }
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
        }

        // Return whether the user was updated successfully.
        return isUpdated;
    }

    //TODO-exception handle
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

        return isDeleted;
    }

}