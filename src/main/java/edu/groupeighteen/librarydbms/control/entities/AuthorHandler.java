package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.Author;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.Author.AuthorNotFoundException;
import edu.groupeighteen.librarydbms.model.exceptions.Author.InvalidAuthornameException;
import edu.groupeighteen.librarydbms.model.exceptions.Author.NullAuthorException;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidPasswordException;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidUsernameException;
import edu.groupeighteen.librarydbms.model.exceptions.user.NullUserException;
import edu.groupeighteen.librarydbms.model.exceptions.user.UserNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.control.entities
 * @contact matfir-1@student.ltu.se
 * @date 5/25/2023
 * <p>
 * We plan as much as we can (based on the knowledge available),
 * When we can (based on the time and resources available),
 * But not before.
 * <p>
 * Brought to you by enough nicotine to kill a large horse.
 */
public class AuthorHandler {
    private static final ArrayList<String> storedAuthors = new ArrayList<>();

    /**
     * Performs setup tasks. In this case, syncing storedUsernames against the database.
     */
    public static void setup() {
        syncAuthors();
    }

    public static void reset() {
        storedAuthors.clear();
    }

    /**
     * Syncs the storedUsernames list against the usernames in the users table.
     */
    public static void syncAuthors() {
        if (!storedAuthors.isEmpty())
            storedAuthors.clear();
        retrieveAuthorsFromTable();
    }

    /**
     * Method that retrieves the authors in the author table and stores them in the static ArrayList.
     * Query needs to be ORDER BY author_id ASC or ids will be in the order of 10, 1, 2, ...
     */
    private static void retrieveAuthorsFromTable() {
        try {
            // Execute the query to retrieve all authors
            String query = "SELECT author name FROM authors ORDER BY authorID ASC";
            try (QueryResult result = DatabaseHandler.executeQuery(query)) {

                // Add the retrieved author names to the ArrayList
                while (result.getResultSet().next()) {
                    storedAuthors.add(result.getResultSet().getString("authors"));
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException("Failed to retrieve authors from database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Prints all usernames in the ArrayList.
     */
    public static void printAuthors() {
        System.out.println("\nAuthor Names:");
        int num = 1;
        for (String authorName : storedAuthors) {
            System.out.println(num + ": " + storedAuthors);
            num++;
        }
    }

    /**
     * Returns the ArrayList of authors.
     * @return the ArrayList of authors
     */
    public static ArrayList<String> getStoredAuthors() {
        return storedAuthors;
    }

    //TODO-prio update when User class is finished
    /**
     * Prints all non-sensitive data for all Users in a list.
     * @param authorList the list of User objects.
     */
    public static void printAuthorList(List<Author> authorList) {
        System.out.println("Authors:");
        int count = 1;
        for (Author author : authorList) {
            System.out.println(count + " authorID: " + author.getAuthorID() + ", Author Name: " + author.getAuthorFirstname());
        }
    }

    //CREATE -----------------------------------------------------------------------------------------------------------

    /**
     * Creates a new author with the specified authorFirstName and authorLastName. The method first checks if the provided author name is
     * already taken. If the username is unique, a new User object is created and saved to the database. The Authors's ID
     * from the database is set in the Author object before it is returned. The method also handles any potential
     * InvalidPasswordException or InvalidIDException.
     *
     * @param authorFirstName The username for the new author.
     * @param authorLastName The password for the new author.
     * @return A User object representing the newly created author.
     */
    public static Author createNewUAuthor(String authorFirstName, String authorLastName ) throws InvalidAuthornameException,
            InvalidPasswordException {
        Author newAuthor = null;

        try {
            //Validate input
            validateAuthorname(authorFirstName);
            validateAuthorname(authorLastName);

            // Create and save the new user, retrieving the ID
            newAuthor = new Author(authorFirstName, authorLastName);
            newAuthor.setAuthorID(saveAuthor(newAuthor));

            // Need to remember to add to the list
            storedAuthors.add(newAuthor.getAuthorFirstname());
        } catch (ConstructionException | InvalidIDException e) {
            ExceptionHandler.HandleFatalException(String.format("Failed to create Author with the given name: " +
                    "'%s' due to %s: %s", authorFirstName, e.getClass().getName(), e.getMessage()), e);
        }

        return newAuthor;
    }

    /**
     * Saves a author to the database. The method prepares an SQL insert query with the user's details such as
     * username, password, allowed rentals, current rentals and late fee. The query is executed and the
     * auto-generated user ID from the database is retrieved and returned. If the query execution fails, the method
     * handles the SQLException and returns 0.
     *
     * @param author The user object to be saved.
     * @return The auto-generated ID of the user from the database.
     *          Returns 0 if an SQLException occurs. This won't happen because the exception will be thrown first.
     */
    private static int saveAuthor(Author author) {
        try {
            // Prepare query
            String query = "INSERT INTO authors (author first name, author last name, " +
                    "VALUES (?, ?)";

            String[] params = {
                    author.getAuthorFirstname(),
                    author.getAuthorLastName(),
            };

            // Execute query and get the generated userID, using try-with-resources
            try (QueryResult queryResult =
                         DatabaseHandler.executePreparedQuery(query, params, Statement.RETURN_GENERATED_KEYS)) {
                ResultSet generatedKeys = queryResult.getStatement().getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException("Failed to save author to database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        //Not reachable, but needed for compilation
        return 0;
    }

    /**
     * Retrieves a Author object from the database using the provided authorID. The method first validates the provided
     * authorID. It then prepares and executes an SQL query to select the user's details from the database. If a author
     * with the provided authorID exists, a new Author object is created with the retrieved details and returned.
     *
     * @param authorID The userID of the user to be retrieved.
     * @return A Author object representing the user with the provided authorID. Returns null if the user does not exist.
     */
    public static Author getAuthorByID(int authorID) throws InvalidIDException {
        try {
            // No point getting invalid users, throws InvalidIDException
            checkValidAuthorID(authorID);

            // Prepare a SQL query to select a user by userID.
            String query = "SELECT username, password, allowedRentals, currentRentals, " +
                    "lateFee, allowedToRent, deleted FROM users WHERE userID = ?";
            String[] params = {String.valueOf(authorID)};

            // Execute the query and store the result in a ResultSet.
            try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params)) {
                ResultSet resultSet = queryResult.getResultSet();
                // If the ResultSet contains data, create a new User object using the retrieved username and password,
                // and set the user's userID.
                if (resultSet.next()) {

                    return new Author(authorID,
                            resultSet.getString("Author First Name"),
                            resultSet.getString("Author Last Name"),
                    );
                }
            }
        } catch (SQLException | ConstructionException e) {
            ExceptionHandler.HandleFatalException("Failed to retrieve author by ID from database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        // Return null if not found.
        return null;
    }

    //UPDATE -----------------------------------------------------------------------------------------------------------

    /**
     * Updates the data of an existing user in the database with the data of the provided User object. Before updating,
     * the method validates that the provided User object is not null and the old username is not empty.
     * If the username of the provided User object differs from the old username, the method checks if the new username
     * is taken and updates the username in the storedUsernames list if it isn't. The method then prepares an
     * SQL command to update the user's data in the database and executes the update.
     *
     * @param updatedAuthor The User object containing the updated user data.
     */ //TODO-PRIO UPDATE EXCEPTION AND TESTS
    public static void updateAuthor(Author updatedAuthor) throws NullAuthorException, InvalidAuthornameException {
        try {
            //Let's check if the author exists in the database before we go on
            updateAuthor(updatedAuthor);

            //Retrieve old username
            String oldAuthorname = getAuthorByID(updatedAuthor.getAuthorID()).getAuthorFirstname();

            //If author name has been changed...
            if (!updatedAuthor.getAuthorFirstname().equals(oldAuthorname)) {
                //... and is taken. Throws UsernameTakenException
                checkAuthornameTaken(updatedAuthor.getAuthorFirstname());
                //... and is not taken: remove old username from and add new username to storedUsernames
                storedAuthors.remove(oldAuthorname);
                storedAuthors.add(updatedAuthor.getAuthorFirstname());
            }

            // Prepare a SQL command to update a updatedAuthors's data by authorID.
            String sql = "UPDATE users SET username = ?, password = ?, currentRentals = ?, " +
                    "lateFee = ?, allowedToRent = ? " + "WHERE userID = ?";
            String[] params = {
                    updatedAuthor.getAuthorFirstname(),
                    updatedAuthor.getAuthorLastName(),
                    String.valueOf(updatedAuthor.getAuthorID())
            };

            // Execute the update.
            DatabaseHandler.executePreparedUpdate(sql, params);
        } catch (AuthorNotFoundException | InvalidIDException e) {
            ExceptionHandler.HandleFatalException("Failed to update author in database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    public static void deleteAuthor(Author author) {

    }

    public static void undoDeleteAuthor(Author author) {

    }

    /**
     * Deletes a user from the database.
     *
     * This method first checks if the provided User object is not null and if
     * the user with the ID of the provided User object exists in the database. If the user does not exist,
     * a UserNotFoundException is thrown.
     *
     * If the user exists, an SQL command is prepared to delete the user from the database,
     * and this command is executed.
     *
     * The username of the deleted user is then removed from the storedUsernames list.
     *
     * @param author The User object representing the user to be deleted.
     */ //TODO-PRIO UPDATE EXCEPTION AND TESTS
    public static void hardDeleteAuthor(Author author) throws NullAuthorException, AuthorNotFoundException {
        try {
            //Validate the input. Throws NullAuthorException
            validateAuthor(author);

            //Prepare a SQL command to delete a user by authorID.
            String sql = "DELETE FROM authors WHERE authorID = ?";
            String[] params = {String.valueOf(author.getAuthorID())};

            //Execute the update. //TODO-prio handle cascades in rentals
            DatabaseHandler.executePreparedUpdate(sql, params);

            //Remove the deleted user's username from the usernames ArrayList.
            storedAuthors.remove(author.getAuthorFirstname());
        } catch (InvalidIDException e) {
            ExceptionHandler.HandleFatalException("Failed to delete author from database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    // VALIDATION STUFF -----------------------------------------------------------------------------------------------

    /**
     * Basic login method. Checks whether username exists in storedUsernames. If it does, check whether password
     * matches that user's password.
     * @param username the username attempting to login
     * @param password the password attempting to login
     * @return true if successful, otherwise false
     */
    /*public static boolean login(String username, String password)
            throws InvalidUsernameException, UserNotFoundException, InvalidPasswordException {
        try {
            // No point verifying empty strings, throws UsernameEmptyException
            checkEmptyUsername(username);
            //Throws PasswordEmptyException
            checkEmptyPassword(password);

            //First check list
            if (!storedUsernames.contains(username))
                throw new UserNotFoundException("Login failed: User " + username + " does not exist.");

            String query = "SELECT password FROM users WHERE username = ?";
            String[] params = {username};

            // Execute the query and check if the input password matches the retrieved password
            try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params)) {
                ResultSet resultSet = queryResult.getResultSet();
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    if (password.equals(storedPassword)) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException("Login failed due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        //Incorrect password
        return false;
    }*/

    /**
     * Validates the user's password.
     *
     * This method compares the password provided as an argument with the password stored in the User object.
     * If the provided password matches the stored password, the method returns true. Otherwise, it returns false.
     *
     * @param author The User object whose password is to be validated.
     * @param password The password to validate.
     * @return boolean Returns true if the provided password matches the User's stored password, false otherwise.
     */
    public static boolean validate(Author author, String password) throws NullAuthorException, InvalidPasswordException {
        checkNullAuthor(author);
        checkEmptyPassword(password);
        return author.getAuthorFirstname().equals(password);
    }

    //RETRIEVING -------------------------------------------------------------------------------------------------------

    /**
     * Retrieves a User object from the database using the provided username. The method first validates the provided
     * username. It then prepares and executes an SQL query to select the user's details from the database. If a user
     * with the provided username exists, a new User object is created with the retrieved details and returned.
     *
     * @param authorName The username of the user to be retrieved.
     * @return A User object representing the user with the provided username. Returns null if the user does not exist.
     */
    public static Author getAuthorByAuthorname(String authorName) throws InvalidAuthornameException {
        try {
            // No point in getting invalid users, throws InvalidUsernameException
            checkEmptyAuthorname(authorName);

            // Prepare a SQL query to select a user by username
            String query = "SELECT userID, password, allowedRentals, currentRentals, lateFee FROM users " +
                    "WHERE username = ?";
            String[] params = {authorName};

            // Execute the query and store the result in a ResultSet
            try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params)) {
                ResultSet resultSet = queryResult.getResultSet();
                // If the ResultSet contains data, create a new User object using the retrieved username and password,
                // and set the user's userID
                if (resultSet.next()) { //TODO-PRIO UPDATE
                    Author author = new Author(authorName, resultSet.getString("Author Name"));
                    author.setAuthorID(resultSet.getInt("authorID"));
                    return author;
                }
            }
        }/* catch (SQLException | InvalidIDException | InvalidLateFeeException | ConstructionException |
                 AuthorNotAllowedException | InvalidAuthornameException e) {
            ExceptionHandler.HandleFatalException("Failed to retrieve user by username from database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }*/ catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InvalidAuthornameException e) {
            throw new RuntimeException(e);
        }

        // Return null if not found
        return null;
    }




    public static List<Author> getAuthorsByFirstname(String firstname) {
        //Invalid firstname
        //No such users
        //One valid user
        //Multiple valid users
        // == 4
        return null;
    }

    public static List<Author> getAuthorsByLastname(String lastname) {
        //Invalid lastname
        //No such users
        //One valid user
        //Multiple valid users
        // == 4
        return null;
    }

    //UTILITY METHODS---------------------------------------------------------------------------------------------------

    private static void validateAuthorname(String authorName) throws InvalidAuthornameException {
        checkEmptyAuthorname(authorName);
        checkAuthornameTaken(authorName);
        if (authorName.length() < Author.AUTHOR_FIRST_NAME_LENGTH)
            throw new InvalidAuthornameException("Author Name too short. Must be at least " + Author.MIN_AUTHORNAME_LENGTH +
                    " characters, received " + authorName.length());
        if (authorName.length() > Author.MAX_AUTHORNAME_LENGTH)
            throw new InvalidAuthornameException("Username too long. Must be at most "+ Author.MAX_AUTHORNAME_LENGTH +
                    " characters, received " + authorName.length());
    }

    private static void validatePassword(String password) throws InvalidPasswordException {
        checkEmptyPassword(password);
        if (password.length() < User.MIN_PASSWORD_LENGTH)
            throw new InvalidPasswordException("Password too short. Must be at least " + User.MIN_PASSWORD_LENGTH +
                    " characters, received " + password.length());
        if (password.length() > User.MAX_PASSWORD_LENGTH)
            throw new InvalidPasswordException("Password too long. Must be at most "+ User.MAX_PASSWORD_LENGTH +
                    " characters, received " + password.length());
    }


    /**
     * Checks whether a given username is null or empty. If so, throws an UsernameEmptyException
     * which must be handled.
     * @param authorName the username to check.
     * @throws InvalidAuthornameException if username is null or empty.
     */
    private static void checkEmptyAuthorname(String authorName) throws InvalidAuthornameException {
        if (authorName == null || authorName.isEmpty()) {
            throw new InvalidAuthornameException("Author Name is null or empty.");
        }
    }

    /**
     * Checks if a given username exists in the list of usernames. If so, throws a UsernameTakenException
     * which must be handled.
     * @param authorName the username.
     * @throws InvalidAuthornameException if the username already exists in storedTitles.
     */
    private static void checkAuthornameTaken(String authorName) throws InvalidAuthornameException {
        if (storedAuthors.contains(authorName))
            throw new InvalidAuthornameException("Author Name " + authorName + " already taken.");
    }

    private static void validateAuthor(Author author) throws AuthorNotFoundException, InvalidIDException, NullAuthorException {
        checkNullAuthor(author);
        int ID = author.getAuthorID();
        if (UserHandler.getUserByID(ID) == null)
            throw new AuthorNotFoundException("User with ID " + author + "not found in database.");
    }

    /**
     * Checks if a given user is null. If so, throws a NullUserException which must be handled.
     * @param author the user.
     * @throws NullAuthorException if the user is null.
     */
    private static void checkNullAuthor(Author author) throws NullAuthorException {
        if (author == null)
            throw new NullAuthorException("User is null.");
    }

    /**
     * Checks whether a given authorID is invalid (<= 0). If so, throws an InvalidIDException
     * which must be handled by the caller.
     * @param authorID the userID to validate.
     * @throws InvalidIDException if authorID <= 0.
     */
    private static void checkValidAuthorID(int authorID) throws InvalidIDException {
        if (authorID <= 0) {
            throw new InvalidIDException("Invalid authorID: " + authorID);
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