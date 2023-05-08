package edu.groupeighteen.librarydbms.control.entities;

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
 * This class contains database operation methods related to the User class.
 * It contains a list of all usernames for quicker validation.
 *
 * When working with a database, it's common to retrieve specific data from the database when needed,
 * perform operations, and then persist any changes back to the database. This way, you avoid keeping a large number
 * of objects in memory, which could lead to performance issues and increased memory usage.
 *
 * Here are some scenarios in which you would retrieve a user from the database and create a new User object:
 *
 * Authenticating a user during login: Retrieve the user based on the provided username, check the password,
 * and then create a session for the authenticated user.
 *
 * Updating user information: Retrieve the user based on the username, modify the necessary attributes,
 * and then persist the changes back to the database.
 *
 * Displaying user information: Retrieve the user based on the username and display the relevant information in the UI.
 */
public class UserHandler {
    //Needed since usernames must be unique
    private static final ArrayList<String> usernames = new ArrayList<>();
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
        retrieveUsernamesFromTable();
    }

    //Username stuff --------------------------------------------------------------------------------------------------

    //TODO exception handling
    //TODO-test
    /**
     * Method that retrieves the usernames of all users in the user table and stores them in the static list.
     * @throws SQLException
     */
    private static void retrieveUsernamesFromTable() throws SQLException {
        // Execute the query to retrieve all usernames
        Statement statement = connection.createStatement();

        //Needs to be ORDER BY user_id ASC or id 10 will be printed before ids 1, 2, 3 etc
        ResultSet resultSet = statement.executeQuery("SELECT username FROM users ORDER BY userID ASC");

        // Add the retrieved usernames to the ArrayList
        while (resultSet.next()) {
            usernames.add(resultSet.getString("username"));
        }

        // Close the resources
        resultSet.close();
        statement.close();
    }

    /**
     * Prints all usernames in usernames.
     */
    public static void printUsernames() {
        System.out.println("\nUsernames:");
        for (String username : usernames) {
            System.out.println(username);
        }
    }

    public static ArrayList<String> getUsernames() {
        return usernames;
    }

    //User stuff ------------------------------------------------------------------------------------------------------

    //TODO-test
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
            usernames.add(newUser.getUsername()); //Need to remember to add to the list
            return newUser;
        } catch (SQLException e) {
            System.err.println("Error creating a new user: " + e.getMessage());
            e.printStackTrace(); //TODO-exception handle
            return null;
        }
    }

    //TODO-future comment
    //TODO-test
    //TODO-exception handle
    /**
     *
     * @param user
     * @return
     * @throws SQLException
     */
    public static int saveUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        //Try-with-resources, useful!
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();

            // Get the generated userID
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) { //Try-with-resources, useful!
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to insert the user, no ID obtained.");
                }
            }
        }
    }

    //TODO-exception Handle
    //TODO-test
    //TODO change to taking a User object as argument instead of Strings
    /**
     * Basic login method. Checks whether username exists in usernames. If it does, check whether password
     * matches that user's password.
     * @param username the username attempting to login
     * @param password the password attempting to login
     * @return true if successful, otherwise false
     */
    public static boolean login(String username, String password) throws SQLException {
        boolean isAuthenticated = false;

        if (!usernames.contains(username)) {
            System.out.println(username + ": no such user in database.");
            return false;
        }

        String query = "SELECT password FROM User WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);

        // Execute the query and check if the input password matches the retrieved password
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String storedPassword = resultSet.getString("password");
            if (password.equals(storedPassword)) {
                isAuthenticated = true;
            }
        }

        // Close the resources
        resultSet.close();
        preparedStatement.close();

        return isAuthenticated;
    }
}