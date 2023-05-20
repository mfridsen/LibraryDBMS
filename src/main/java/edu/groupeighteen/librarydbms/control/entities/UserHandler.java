package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.UserNotFoundException;

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


    public static User createNewUser(String username, String password) {
        // Usernames must be unique
        if (storedUsernames.contains(username))
            throw new IllegalArgumentException("Username " + username + " already taken.");

        // Create and save the new user, retrieving the ID
        User newUser = new User(username, password);
        newUser.setUserID(saveUser(newUser));

        // Need to remember to add to the list
        storedUsernames.add(newUser.getUsername());
        return newUser;
    }

    public static int saveUser(User user) {
        try {
            // Validate the input
            if (user == null)
                throw new IllegalArgumentException("Invalid user: user is null.");

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




    public static User getUserByID(int userID) throws UserNotFoundException {
        // No point getting invalid users
        if (userID <= 0) {
            throw new IllegalArgumentException("Invalid userID: " + userID);
        }

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
            } else throw new UserNotFoundException(userID);
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException(e);
        }

        // Return null if not found.
        return null;
    }


    public static User getUserByUsername(String username) throws UserNotFoundException {
        // No point in getting invalid users
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }

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
            } else throw new UserNotFoundException(username);
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException(e);
        }

        // Return null if not found
        return null;
    }

    public static User getUserByEmail(String email) {
        return null;
    }

    public static List<User> getUsersByFirstname(String firstname) {
        return null;
    }

    public static List<User> getUsersByLastname(String lastname) {
        return null;
    }


    public static void updateUser(User newUser, String oldUsername) {
        //We can't create user objects with invalid usernames, so only need to validate user itself
        if (newUser == null)
            throw new IllegalArgumentException("Invalid newUser: newUser is null.");
        //Old username could be empty or null though
        if (oldUsername == null || oldUsername.isEmpty())
            throw new IllegalArgumentException("Old username is empty.");

        //If username has been changed
        if (!newUser.getUsername().equals(oldUsername)) {
            //And is taken
            if (storedUsernames.contains(newUser.getUsername()))
                throw new IllegalArgumentException("New username is taken.");
            //And is not taken: remove old username from and add new username to storedUsernames
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
     * Deletes a user from the database.
     *
     * @param user The user to delete.
     */
    public static void deleteUser(User user) {
        //Validate the input
        if (user == null)
            throw new IllegalArgumentException("Invalid user: user is null.");
        //Validate user exists in database


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
     */
    public static boolean login(String username, String password) {
        // No point verifying empty strings
        if (username == null ||username.isEmpty())
            throw new IllegalArgumentException("Login failed: Empty username.");
        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("Login failed: Empty password.");
        if (!storedUsernames.contains(username)) {
            //TODO-future if user is staff, will still check against database just in case
            throw new IllegalArgumentException("Login failed: User " + username + " does not exist.");
        }

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
     */
    public static boolean validateUser(User user, String password) {
        return user.getPassword().equals(password);
    }

}