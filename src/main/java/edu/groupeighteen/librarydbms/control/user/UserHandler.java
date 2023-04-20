package edu.groupeighteen.librarydbms.control.user;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;

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
        ResultSet resultSet = statement.executeQuery("SELECT username FROM user ORDER BY user_id ASC");

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

    //TODO exception handling
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


    public static ArrayList<String> getUsernames() {
        return usernames;
    }
}