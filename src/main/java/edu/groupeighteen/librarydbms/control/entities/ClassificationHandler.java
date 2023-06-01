package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.Classification;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidIDException;
import edu.groupeighteen.librarydbms.model.exceptions.InvalidNameException;
import org.junit.Test;

import javax.persistence.OrderBy;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 2023-05-27
 */
public class ClassificationHandler {

    /**
     *Used to make the process of verifying if a username is taken or not, faster.
     */

    private static final ArrayList<String> storedClassificationNames = new ArrayList<>();

    /**
     * Performs setup tasks. In this case, syncing storedClassificationNames against the database.
     */
    public static void setup() {syncClassificationNames();}

    public static void reset() {storedClassificationNames.clear();}

    /**
     * Syncs the storedClassificationNames list against the usernames in the Classification table.
     */

    public static void syncClassificationNames() {
        if (!storedClassificationNames.isEmpty())
            storedClassificationNames.clear();
        retrieveClassificationNamesFromTable();
    }

    private static void retrieveClassificationNamesFromTable() {
        try {
            // Execute the query to retrieve all the ClassificationNames
            String query = "SELECT classificationName FROM classifications ORDER BY classificationID ASC";
            try (QueryResult result = DatabaseHandler.executeQuery(query)) {

                // Add the retrieved classificationNames to the ArrayList
                while (result.getResultSet().next()) {
                    storedClassificationNames.add(result.getResultSet().getString("username"));
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.HandleFatalException("Failed to retrieve classifications from database due to " +
                    e.getClass().getName() + ": "+ e.getMessage(), e);
        }
    }

    /**
     * Prints all the classificationName in the ArrayList.
     */
    public static void printClassificationNames() {
        System.out.println("\nClassificationNames");
        int num = 1;
        for (String classificationName : storedClassificationNames) {
            System.out.println(num + ": " + classificationName);
            num++;
        }
    }

    /**
     * Returns the Arraylist of classificationName
     */

    public static ArrayList<String> getStoredClassificationNames() {return storedClassificationNames;}

    /**
     * Prints all non-sensitive data for all Classification's in a list.
     * @param classificationList the list of Classification objects.
     */
    public static void printClassificationList(List<Classification> classificationList) {
        System.out.println("Classifications:");
        int count = 1;
        for (Classification classification : classificationList) {
            System.out.println(count + " classificationID: " + classification.getClassificationID() + ", classification: " + classification.getClassificationName());
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

public static Classification createNewClassification(String classificationName) throws InvalidNameException {

    Classification newClassification = null;

    try {
        // Validate input
        validateClassificationName(classificationName);

        // Create and save the new classification, retrieving the ID.
        newClassification = new Classification(classificationName);
        newClassification.setClassificationID(saveClassification(newClassification));

        // Need to remember to add to the list
        storedClassificationNames.add(newClassification.getClassificationName());
    } catch (ConstructionException | InvalidIDException e) {
        ExceptionHandler.HandleFatalException(String.format("Failed to create Classification with classificationName: " +
                "'%s' due to %s: %s", classificationName, e.getClass().getName(), e.getMessage()), e);
    }

    return newClassification;
}

private static int saveClassification(Classification classification) {
    try {
        // Prepare query
        String query = "INSERT INTO classifications (classificationName, description, deleted" + "VALUES (?, ?, ?)";

        String[] params = {
                classification.getClassificationName()
                //Vilken data ska vara med?
        };

        // Execute query and get the generated classificationID, using try-with-resources.

    }
}
}
