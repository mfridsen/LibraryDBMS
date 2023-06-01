package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.Author;
import edu.groupeighteen.librarydbms.model.exceptions.*;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalDeleteException;
import edu.groupeighteen.librarydbms.model.exceptions.rental.RentalRecoveryException;

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

    public static void printAuthorList(List<Author> authorList) {
        System.out.println("Authors:");
        int count = 1;
        for (Author author : authorList) {
            System.out.println(count + " authorID: " + author.getAuthorID() + ", Author Name: " + author.getAuthorFirstname());
        }
    }

    //CREATE -----------------------------------------------------------------------------------------------------------

    public static Author createNewAuthor(String authorFirstname, String authorLastName ) throws InvalidNameException{
        Author newAuthor = null;

        try {
            //Validate input
            validateAuthorname(authorFirstname, authorLastName);

            // Create and save the new author, retrieving the ID
            newAuthor = new Author(authorFirstname, authorLastName);
            newAuthor.setAuthorID(saveAuthor(newAuthor));


        } catch (ConstructionException | InvalidIDException e) {
            ExceptionHandler.HandleFatalException(String.format("Failed to create Author with the given name: " +
                    "'%s' due to %s: %s", authorFirstname, e.getClass().getName(), e.getMessage()), e);
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
            String query = "INSERT INTO authors (authorFirstname, authorLastName, " +
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

            // Prepare a SQL query to select a author by authorID.
            String query = "SELECT authorFirstname, authorLastName, biography, deleted FROM authors WHERE authorID = ?";
            String[] params = {String.valueOf(authorID)};

            // Execute the query and store the result in a ResultSet.
            try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params)) {
                ResultSet resultSet = queryResult.getResultSet();
                // If the ResultSet contains data, create a new Author object using the retrieved authorFirstname and authorLastName,
                // and set the author's authorID.
                if (resultSet.next()) {

                    return new Author(authorID,
                            resultSet.getString("authorFirstname"),
                            resultSet.getString("authorLastName"),
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
        String sql = "UPDATE authors SET authorFirstname = ?, authorLastName = ?, + WHERE authorID = ?";
        String[] params = {
                updatedAuthor.getAuthorFirstname(),
                updatedAuthor.getAuthorLastName(),
                String.valueOf(updatedAuthor.getAuthorID())
        };

        // Execute the update.
        DatabaseHandler.executePreparedUpdate(sql, params);
    }

    public static void deleteAuthor(Author authorToDelete) throws  {
       throws AuthorDeleteException
        {
            //Validate input
            try
            {
                validateAuthor(authorToDelete);
            }
            catch (edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException | InvalidIDException | edu.groupeighteen.librarydbms.model.exceptions.NullEntityException e)
            {
                throw new AuthorDeleteException("Author Delete failed: " + e.getMessage(), e);
            }

            //Set deleted to true (doesn't need to be set before calling this method)
            authorToDelete.setDeleted(true);

            //Prepare a SQL query to update the rental details
            String query = "UPDATE authors SET deleted = ? WHERE authorID = ?";
            String[] params = {
                    authorToDelete.isDeleted() ? "1" : "0",
                    String.valueOf(authorToDelete.getAuthorID())
            };

            //Executor-class Star Dreadnought
            DatabaseHandler.executePreparedUpdate(query, params);
        }

    public static void undoDeleteAuthor(Author authorToRecover){
           throws AuthorRecoveryException
            {
                //Validate input
                try
                {
                    validateAuthor(authorToRecover);
                }
                catch (edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException | InvalidIDException | edu.groupeighteen.librarydbms.model.exceptions.NullEntityException e)
                {
                    throw new AuthorRecoveryException("Author Recovery failed: " + e.getMessage(), e);
                }

                //Set deleted to false
                authorToRecover.setDeleted(false);

                //Prepare a SQL query to update the rental details
                String query = "UPDATE rentals SET deleted = ? WHERE rentalID = ?";
                String[] params = {
                        authorToRecover.isDeleted() ? "1" : "0",
                        String.valueOf(authorToRecover.getAuthorID())
                };

                //Executor-class Star Dreadnought
                DatabaseHandler.executePreparedUpdate(query, params);
            }
        }

    }
    public static void hardDeleteAuthor(Author authorToDelete) throws NullEntityException, EntityNotFoundException {
        throws AuthorDeleteException
        {
            //Validate input
            try
            {
                validateAuthor(authorToDelete);
            }
            catch (edu.groupeighteen.librarydbms.model.exceptions.NullEntityException | edu.groupeighteen.librarydbms.model.exceptions.EntityNotFoundException | InvalidIDException e)
            {
                throw new AuthorDeleteException("Author Delete failed: " + e.getMessage(), e);
            }

            //Prepare a SQL query to update the rentalToDelete details
            String query = "DELETE FROM authors WHERE authorID = ?";
            String[] params = {String.valueOf(authorToDelete.getAuthorID())};

            //Executor-class Star Dreadnought
            DatabaseHandler.executePreparedUpdate(query, params);
        }
    }
    //RETRIEVING -------------------------------------------------------------------------------------------------------
    public static List<Author> getAuthorByAuthorName(String authorFirstname, String authorLastname) throws InvalidNameException {
        //both names cant be null or empty, only one can be
        if ((authorFirstname == null || authorFirstname.isEmpty()) && (authorLastname == null || authorLastname.isEmpty()))
            throw new InvalidNameException("Both first and last name can not be empty at the same time.");

        try {
            List<Author> authors = new ArrayList<>();

            // Prepare a SQL query to select a author by authorFirstname and authorLastname
            String query = "SELECT authorID, authorFirstname, authorLastname, biography, deleted FROM authors WHERE";
            List<String> params = new ArrayList<>();

            if (authorFirstname != null && !authorFirstname.trim().isEmpty()) {
                query += " LOWER(authorFirstname) = ?";
                params.add(authorFirstname.toLowerCase());
            }

            if (authorLastname != null && !authorLastname.trim().isEmpty()) {
                if (!params.isEmpty()) {
                    query += " AND";
                }
                query += " LOWER(authorLastname) = ?";
                params.add(authorLastname.toLowerCase());
            }

            String[] paramsArray = params.toArray(new String[0]);

            // Execute the query and store the result in a ResultSet
            try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, paramsArray)) {
                ResultSet resultSet = queryResult.getResultSet();
                // If the ResultSet contains data, create a new Author object using the retrieved ,
                // and set the author's authorID
                while (resultSet.next()) {
                    Author author = new Author(
                            resultSet.getInt("authorID"),
                            resultSet.getString("authorFirstname"),
                            resultSet.getString("authorLastname"),
                            resultSet.getString("biography"),
                            resultSet.getBoolean("deleted")
                    );
                    authors.add(author);
                }
            }
            return authors;
        } catch (SQLException | ConstructionException e) {
            throw new RuntimeException(e);//TODO-ändra
        }
    }
    //UTILITY METHODS---------------------------------------------------------------------------------------------------

    private static void validateAuthorname(String authorFirstname, String authorLastName) throws InvalidNameException {
        checkEmptyName(authorFirstname);
        checkEmptyName(authorLastName);
        if (authorFirstname.length() > Author.AUTHOR_FIRST_NAME_LENGTH)
            throw new InvalidNameException("Author first name too long. Must be at most "+ Author.AUTHOR_FIRST_NAME_LENGTH +
                    " characters, received " + authorFirstname.length());
        if (authorLastName.length() > Author.AUTHOR_LAST_NAME_LENGTH)
            throw new InvalidNameException("Author last name too long. Must be at most "+ Author.AUTHOR_LAST_NAME_LENGTH +
                " characters, received " + authorLastName.length());
    }
    private static void checkEmptyName(String authorName) throws InvalidNameException {
        if (authorName == null || authorName.isEmpty()) {
            throw new InvalidNameException("Author Name is null or empty.");
        }
    }

    private static void validateAuthor(Author author) throws EntityNotFoundException, InvalidIDException, NullEntityException {
        checkNullAuthor(author);
        int ID = author.getAuthorID();
        if (UserHandler.getUserByID(ID) == null)
            throw new EntityNotFoundException("Author with ID " + author + "not found in database.");
    }


    private static void checkNullAuthor(Author author) throws NullEntityException {
        if (author == null)
            throw new NullEntityException("Author is null.");
    }


    private static void checkValidAuthorID(int authorID) throws InvalidIDException {
        if (authorID <= 0) {
            throw new InvalidIDException("Invalid authorID: " + authorID);
        }
    }
}