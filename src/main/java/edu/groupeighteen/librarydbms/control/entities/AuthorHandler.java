package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.Author;
import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidPasswordException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public static void printAuthorList(List<Author> authorList) {
        System.out.println("Authors:");
        int count = 1;
        for (Author author : authorList) {
            System.out.println(count + " authorID: " + author.getAuthorID() + ", Author Name: " + author.getAuthorFirstname());
        }
    }

    //CREATE -----------------------------------------------------------------------------------------------------------

    public static Author createNewUAuthor(String authorFirstName, String authorLastName ) throws InvalidNameException{
        Author newAuthor = null;

        try {
            //Validate input
            validateAuthorname(authorFirstName);
            validateAuthorname(authorLastName);

            // Create and save the new author, retrieving the ID
            newAuthor = new Author(authorFirstName, authorLastName);
            newAuthor.setAuthorID(saveAuthor(newAuthor));


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

            // Execute query and get the generated authorID, using try-with-resources
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
                            resultSet.getString("biography"),
                            resultSet.getBoolean("deleted")
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

    public static void updateAuthor(Author updatedAuthor) throws InvalidNameException {
        //Let's check if the author exists in the database before we go on
        updateAuthor(updatedAuthor);

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
    }

    public static void deleteAuthor(Author author) {

    }

    public static void undoDeleteAuthor(Author author) {

    }
    public static void hardDeleteAuthor(Author author) throws NullEntityException, EntityNotFoundException {
        try {
            //Validate the input. Throws NullEntityException
            validateAuthor(author);

            //Prepare a SQL command to delete a user by authorID.
            String sql = "DELETE FROM authors WHERE authorID = ?";
            String[] params = {String.valueOf(author.getAuthorID())};

            //Execute the update. //TODO-prio handle cascades in rentals
            DatabaseHandler.executePreparedUpdate(sql, params);

        } catch (InvalidIDException e) {
            ExceptionHandler.HandleFatalException("Failed to delete author from database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }
    //RETRIEVING -------------------------------------------------------------------------------------------------------
    public static Author getAuthorByAuthorname(String authorName) throws InvalidNameException {
        try {
            // No point in getting invalid users, throws InvalidUsernameException
            checkEmptyAuthorname(authorName);

            // Prepare a SQL query to select a user by username
            String query = "SELECT ID, password, allowedRentals, currentRentals, lateFee FROM users " +
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
                 AuthorNotAllowedException | InvalidNameException e) {
            ExceptionHandler.HandleFatalException("Failed to retrieve user by username from database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }*/ catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InvalidNameException | ConstructionException | InvalidIDException e) {
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

    private static void validateAuthorname(String authorName) throws InvalidNameException {
        checkEmptyAuthorname(authorName);
        if (authorName.length() > Author.AUTHOR_FIRST_NAME_LENGTH)
            throw new InvalidNameException("Username too long. Must be at most "+ Author.AUTHOR_FIRST_NAME_LENGTH +
                    " characters, received " + authorName.length());
    }
    private static void checkEmptyAuthorname(String authorName) throws InvalidNameException {
        if (authorName == null || authorName.isEmpty()) {
            throw new InvalidNameException("Author Name is null or empty.");
        }
    }

    private static void validateAuthor(Author author) throws EntityNotFoundException, InvalidIDException, NullEntityException {
        checkNullAuthor(author);
        int ID = author.getAuthorID();
        if (UserHandler.getUserByID(ID) == null)
            throw new EntityNotFoundException("User with ID " + author + "not found in database.");
    }


    private static void checkNullAuthor(Author author) throws NullEntityException {
        if (author == null)
            throw new NullEntityException("User is null.");
    }


    private static void checkValidAuthorID(int authorID) throws InvalidIDException {
        if (authorID <= 0) {
            throw new InvalidIDException("Invalid authorID: " + authorID);
        }
    }
}