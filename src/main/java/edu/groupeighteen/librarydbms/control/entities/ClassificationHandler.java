package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.Classification;
import edu.groupeighteen.librarydbms.model.exceptions.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 2023-05-27
 */
public class ClassificationHandler
{

    /**
     * Used to make the process of verifying if a username is taken or not, faster.
     */

    private static final ArrayList<String> storedClassificationNames = new ArrayList<>();

    /**
     * Performs setup tasks. In this case, syncing storedClassificationNames against the database.
     */
    public static void setup()
    {
        syncClassificationNames();
    }

    public static void reset()
    {
        storedClassificationNames.clear();
    }

    /**
     * Syncs the storedClassificationNames list against the usernames in the Classification table.
     */

    public static void syncClassificationNames()
    {
        if (!storedClassificationNames.isEmpty())
            storedClassificationNames.clear();
        retrieveClassificationNamesFromTable();
    }

    private static void retrieveClassificationNamesFromTable()
    {
        try
        {
            // Execute the query to retrieve all the ClassificationNames
            String query = "SELECT classificationName FROM classifications ORDER BY classificationID ASC";
            try (QueryResult result = DatabaseHandler.executeQuery(query))
            {

                // Add the retrieved classificationNames to the ArrayList
                while (result.getResultSet().next())
                {
                    storedClassificationNames.add(result.getResultSet().getString("username"));
                }
            }
        }
        catch (SQLException e)
        {
            ExceptionHandler.HandleFatalException("Failed to retrieve classifications from database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Prints all the classificationName in the ArrayList.
     */
    public static void printClassificationNames()
    {
        System.out.println("\nClassificationNames");
        int num = 1;
        for (String classificationName : storedClassificationNames)
        {
            System.out.println(num + ": " + classificationName);
            num++;
        }
    }

    /**
     * Returns the Arraylist of classificationName
     */

    public static ArrayList<String> getStoredClassificationNames()
    {
        return storedClassificationNames;
    }

    /**
     * Prints all non-sensitive data for all Classification's in a list.
     *
     * @param classificationList the list of Classification objects.
     */
    public static void printClassificationList(List<Classification> classificationList)
    {
        System.out.println("Classifications:");
        int count = 1;
        for (Classification classification : classificationList)
        {
            System.out.println(
                    count + " classificationID: " + classification.getClassificationID() + ", classification: " + classification.getClassificationName());
        }
    }

    //CREATE -----------------------------------------------------------------------------------------------------------

    /**
     * Creates a new classification with the specified classification. The method first checks if the provided classification is
     * already taken. If the classification is unique, a new Classification object is created and saved to the database. The Classification's ID
     * from the database is set in the Classification object before it is returned. The method also handles any potential
     * InvalidIDException.
     *
     * @param classificationName The classificationName for the new classification.
     * @return A User object representing the newly created user.
     */

    public static Classification createNewClassification(String classificationName)
    throws InvalidNameException
    {

        Classification newClassification = null;

        try
        {
            // Validate input
            validateClassificationName(classificationName);

            // Create and save the new classification, retrieving the ID.
            newClassification = new Classification(classificationName);
            newClassification.setClassificationID(saveClassification(newClassification));

            // Need to remember to add to the list
            storedClassificationNames.add(newClassification.getClassificationName());
        }
        catch (ConstructionException | InvalidIDException e)
        {
            ExceptionHandler.HandleFatalException(
                    String.format("Failed to create Classification with classificationName: " +
                            "'%s' due to %s: %s", classificationName, e.getClass().getName(), e.getMessage()), e);
        }

        return newClassification;
    }

    private static int saveClassification(Classification classification)
    {
        try
        {
            // Prepare query
            String query = "INSERT INTO classifications (classificationName, description, deleted" + "VALUES (?, ?, ?)";

            String[] params = {
                    classification.getClassificationName(),
                    classification.getDescription(),
                    "0"
            };

            // Execute query and get the generated classificationID, using try-with-resources.
            try (QueryResult queryResult =
                         DatabaseHandler.executePreparedQuery(query, params, Statement.RETURN_GENERATED_KEYS))
            {
                ResultSet generatedKeys = queryResult.getStatement().getGeneratedKeys();
                if (generatedKeys.next())
                {
                    return generatedKeys.getInt(1);
                }
            }
        }
        catch (SQLException e)
        {
            ExceptionHandler.HandleFatalException("Failed to save user to database due to " +
                    e.getClass().getName() + ":" + e.getMessage(), e);
        }
        return 0;
    }

    /**
     * Retrieves a Classification object from the database using the provided classificationID. The method first validates the provided
     * classificationID. It then prepares and executes an SQL query to select the classification's details from the database. If a classification
     * with the provided classificationID exists, a new Classification object is created with the retrieved details and returned.
     *
     * @param classificationID The classificationID of the classification to be retrieved.
     * @return A Classification object representing the classification with the provided classificationID. Returns null if the classification does not exist.
     */

    public static Classification getClassificationByID(int classificationID)
    throws InvalidIDException
    {
        try
        {

            // No point getting invalid Classifications, throws InvalidIDException
            checkValidClassificationID(classificationID);

            // Prepare a SQL query to select a classification by classificationID.
            String query = "SELECT classificationName, description, deleted " +
                    "FROM classifications WHERE classificationID = ?";
            String[] params = {String.valueOf(classificationID)};

            // Execute the query and store the result in a ResultSet.
            try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params))
            {
                ResultSet resultSet = queryResult.getResultSet();
                // If the ResultSet contains data, create a new Classification object using the retrieved classificationsName,
                // and set the classification's classificationsID.
                if (resultSet.next())
                {

                    return new Classification(classificationID,
                            resultSet.getString("classificationName"),
                            resultSet.getString("description"),
                            resultSet.getBoolean("deleted")
                    );
                }
            }
        }
        catch (SQLException | ConstructionException e)
        {
            ExceptionHandler.HandleFatalException("Failed to retrieve user by ID from database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
        // Return null if not found
        return null;


    }

    //UTILITY METHODS---------------------------------------------------------------------------------------------------

    private static void validateClassificationName(String classificationName)
    throws InvalidNameException
    {

        checkEmptyClassificationName(classificationName);
        checkClassificationNameTaken(classificationName);
    }

    /**
     * Checks whether a given classificationID is invalid (<= 0). If so, throws an InvalidIDException
     * which must be handled by the caller.
     *
     * @param classificationID the classificationID to validate.
     * @throws InvalidIDException if classificationID <= 0.
     */
    private static void checkValidClassificationID(int classificationID)
    throws InvalidIDException
    {
        if (classificationID <= 0)
        {
            throw new InvalidIDException("Invalid classificationID: " + classificationID);
        }
    }

    /**
     * Checks whether a given classificationName is null or empty. If so, throws an ClassificationEmptyException
     * which must be handled.
     *
     * @param classificationName the classificationName to check.
     * @throws InvalidNameException if classificationName is null or empty.
     */
    private static void checkEmptyClassificationName(String classificationName)
    throws InvalidNameException
    {
        if (classificationName == null || classificationName.isEmpty())
        {
            throw new InvalidNameException("Classification is null or or empty");
        }
    }

    /**
     * Checks if a given classificationName exists in the list of classificationName. If so, throws a ClassificationTakenException
     * which must be handled.
     *
     * @param classificationName the classificationName.
     * @throws InvalidNameException if the classificationName already exists in storedTitles.
     */
    private static void checkClassificationNameTaken(String classificationName)
    throws InvalidNameException
    {
        if (storedClassificationNames.contains(classificationName))
            throw new InvalidNameException("Classification " + classificationName + " already taken.");
    }

    // DELETE AND RECOVER ----------------------------------------------------------------------------------------------

    /**
     * Deletes a classification by marking them as deleted in the database.
     * Sets the 'deleted' field to true /and 'allowedToRent' field to false for the specified user./
     *
     * @param classificationToDelete the classification to be deleted
     * @throws DeletionException if an error occurs during the deletion process
     */

    public static void deleteClassification(Classification classificationToDelete)
    throws DeletionException
    {
        try
        {
            //Validate classification, throws NullEntityException/EntityNotFoundException
            validateClassificationObject(classificationToDelete);

            // Prepare a SQL command to set deleted to true for the specified classification.
            String sql = "UPDATE classifications SET deleted = 1 WHERE classificationID = ?";
            String[] params = {String.valueOf(classificationToDelete.getClassificationID())};

            // Execute the update.
            DatabaseHandler.executePreparedUpdate(sql, params);

            //Update the deleted field of the classification object
            classificationToDelete.setDeleted(true);

        }
        catch (NullEntityException | EntityNotFoundException | InvalidIDException e)
        {
            throw new DeletionException("Failed to delete Classification due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);

        }
    }

    private static void validateClassificationObject(Classification classificationToDelete)
            throws NullEntityException, EntityNotFoundException, InvalidIDException
    {
        //TODO-prio impement
    }

    /**
     * Recovers a deleted Classification by setting their 'deleted' field to false in the database.
     * Updates the 'deleted' fields of the specified classification based on their recovery eligibility.
     *
     * @param classificationToRecover the classification to be recovered
     * @throws RecoveryException if an error occurs during the recovery process
     */

    public static void recoverClassification(Classification classificationToRecover)
    throws RecoveryException
    {
        try
        {
            // Validate classification
            validateRecoverableClassificationObject(classificationToRecover);

            // Update the deleted field of the classification object
            classificationToRecover.setDeleted(false);

            //Prepare a SQL command to set deleted to false for the specified classification.
            String sql = "UPDATE classifications SET deleted = 0 WHERE classificationID = ?";
            String[] params = {String.valueOf(classificationToRecover.getClassificationID())};

            // Execute the update
            DatabaseHandler.executePreparedUpdate(sql, params);

        }
        catch (NullEntityException | EntityNotFoundException | InvalidIDException e)
        {
            throw new RecoveryException("Failed to recover Classification due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    private static void validateRecoverableClassificationObject(Classification classificationToRecover)
            throws NullEntityException, EntityNotFoundException, InvalidIDException
    {
        //TODO-prio impement
    }

    /**
     * Performs a hard delete of a classification by removing it's data from the database.
     * This operation is irreversible and removes the classifications's data completely.
     * Performs necessary validations to ensure the classifications can be hard deleted.
     *
     * @param classificationToDelete the classification to be hard deleted
     * @throws DeletionException if an error occurs during the hard deletion process
     */

    public static void hardDeleteClassification(Classification classificationToDelete)
    throws DeletionException
    {
        try
        {
            //Validate the input. Throws NullEntityException and EntityNotFoundException
            validateDeleteableClassificationObject(classificationToDelete);

            // Retrieve the unique classificationName
            String classificationName = classificationToDelete.getClassificationName();

            // Prepare a SQL command to delete a classificationToDelete by classificationID.
            String sql = "DELETE FROM classifications WHERE classificationID = ?";
            String[] params = {String.valueOf(classificationToDelete.getClassificationID())};

            // Execute the update
            DatabaseHandler.executePreparedUpdate(sql, params);

            // Set booleans
            classificationToDelete.setDeleted(true);

            // Remove the deleted classificationToDelete name from the list.
            storedClassificationNames.remove(classificationName);
        }
        catch (EntityNotFoundException | NullEntityException | InvalidIDException e)
        {
            throw new DeletionException("Failed to delete classificationToDelete from database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    private static void validateDeleteableClassificationObject(Classification classificationToDelete)
    throws EntityNotFoundException, NullEntityException, InvalidIDException
    {
        //TODO-prio impement
    }

    //UPDATE -----------------------------------------------------------------------------------------------------------

    //Comments
    public static void updateClassification(Classification updatedClassification)
    throws NullEntityException, InvalidNameException, UpdateException
    {
        try
        {
            // Let's check if the classification exists in the database before we go on.
            validateClassificationName(updatedClassification.getClassificationName());

            //Retrieve old classificationName
            String oldClassificationName = getClassificationByID(
                    updatedClassification.getClassificationID()).getClassificationName();

            // If classificationName has been changed...
            if (!updatedClassification.getClassificationName().equals(oldClassificationName))
            {
                //... and is taken. Throws ClassificationTakenException
                checkClassificationNameTaken(updatedClassification.getClassificationName());
                //... and is not taken: remove old classificationName from and add new classificationName
                // to storedClassificationName
                storedClassificationNames.remove(oldClassificationName);
                storedClassificationNames.add(updatedClassification.getClassificationName());
            }

            String sql = "UPDATE classifications SET classificationsName = ?, classificationID = ?, description = ?, WHERE classificationID  = ?";
            String[] params = {
                    updatedClassification.getClassificationName(),
                    updatedClassification.getDescription(),
                    String.valueOf(updatedClassification.getClassificationID())

            };

            //Execute the update
            DatabaseHandler.executePreparedUpdate(sql, params);
        }
        catch (InvalidIDException | InvalidNameException e)
        {
            throw new UpdateException("Failed to update classification in database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

    }

    private static void validateUpdateableClassification(Classification updatedClassification)
    throws InvalidIDException, InvalidNameException
    {
        Classification oldClassification = getClassificationByID(updatedClassification.getClassificationID());

        // If classification is different
        if (!updatedClassification.getClassificationName().equals(oldClassification.getClassificationName()))
            if (storedClassificationNames.contains(updatedClassification.getClassificationName()))
                throw new InvalidIDException("Cannot update classifications; classification " +
                        updatedClassification.getClassificationName() + "already taken.");
    }

    //RETRIEVING -------------------------------------------------------------------------------------------------------

    public static Classification getClassificationByClassificationName(String classificationName)
    throws InvalidNameException
    {
        return getClassificationByClassificationName(classificationName, false);
    }

    public static Classification getClassificationByClassificationName(String classificationName, boolean getDeleted)
    throws InvalidNameException
    {
        try
        {
            //No point in getting invalid Classifications, throws InvalidNameException
            checkEmptyClassificationName(classificationName);

            // Prepare a SQL query to select a classification by classificationName.
            // Include deleted condition based on getDeleted.
            String query = getDeleted ?
                    "SELECT * FROM classifications WHERE classificationName = ?" :
                    "SELECT * FROM classifications WHERE classificationName = ? AND deleted = false";
            String[] params = {classificationName};

            // Execute the query and store the result in a ResultSet
            try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params))
            {

                ResultSet resultSet = queryResult.getResultSet();
                // If the ResultSet contains data,
                // create a new Classification object using the retrieved classificationName
                // and set the classifications classificationsID.
                if (resultSet.next())
                {
                    return new Classification(
                            resultSet.getInt("classificationID"),
                            classificationName,
                            resultSet.getString("description"),
                            resultSet.getBoolean("deleted"));

                }
            }


        }
        catch (SQLException | ConstructionException e)
        {
            ExceptionHandler.HandleFatalException("Failed to retrieve user by username from database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);

        }
        return null;
    }

}



