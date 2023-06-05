package edu.groupeighteen.librarydbms.control.entities;

import edu.groupeighteen.librarydbms.control.db.DatabaseHandler;
import edu.groupeighteen.librarydbms.control.exceptions.ExceptionHandler;
import edu.groupeighteen.librarydbms.model.db.QueryResult;
import edu.groupeighteen.librarydbms.model.entities.Classification;
import edu.groupeighteen.librarydbms.model.exceptions.ConstructionException;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @date 2023-05-27
 */
public class ClassificationHandler
{
    public static Classification getClassificationByID(int classificationID)
    {
        Classification classification = null;

        //Prepare statement
        String query = "SELECT classificationID, classificationName, description, deleted " +
                "FROM classifications WHERE classificationID = ?";
        String[] params = {String.valueOf(classificationID)};

        //Execute statement
        try (QueryResult queryResult = DatabaseHandler.executePreparedQuery(query, params))
        {
            ResultSet resultSet = queryResult.getResultSet();
            if (resultSet.next())
                classification = new Classification(
                        resultSet.getInt("classificationID"),
                        resultSet.getString("classificationName"),
                        resultSet.getString("description"),
                        resultSet.getBoolean("deleted")
                );
        }
        catch (SQLException | ConstructionException e)
        {
            ExceptionHandler.HandleFatalException("Failed to retrieve classification by ID from database due to " +
                    e.getClass().getName() + ": " + e.getMessage(), e);
        }

        return classification;
    }
}
