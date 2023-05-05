package edu.groupeighteen.librarydbms.control.user;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.model.db.DatabaseConnection;
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
 * It contains a list of all usernames
 */
public class UserHandler {
    private static final ArrayList<String> usernames = new ArrayList<>();

    public static void setup(Connection connection) throws SQLException {
        retrieveUsernamesFromTable(connection);
    }

    //TODO exception handling
    /**
     * Method that retrieves the usernames of all users in the user table and stores them in the static list.
     * @throws SQLException
     */
    public static void retrieveUsernamesFromTable(Connection connection) throws SQLException {
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

    //TODO-future comment
    /**
     *
     * @param username
     * @param password
     * @return
     */
    public static User createNewUser(String username, String password) {
        try {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setUserID(saveUser(newUser));
            return newUser;
        } catch (SQLException e) {
            System.err.println("Error creating a new user: " + e.getMessage());
            e.printStackTrace(); //TODO-exception handle
            // Return null or throw a custom exception with a user-friendly error message
            return null;
            // OR
            // throw new CustomUserCreationException("Failed to create a new user. Please try again.");
        }
    }

    //TODO-future comment
    /**
     *
     * @param user
     * @return
     * @throws SQLException
     */
    public static int saveUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();

            // Get the generated userID
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to insert the user, no ID obtained.");
                }
            }
        }
    }

    //TODO-exception Handle
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

        Connection connection = DatabaseHandler.getConnection();
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