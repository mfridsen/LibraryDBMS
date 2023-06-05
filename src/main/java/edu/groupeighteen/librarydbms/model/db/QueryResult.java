package edu.groupeighteen.librarydbms.model.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model.db
 * @contact matfir-1@student.ltu.se
 * @date 5/8/2023
 * <p>
 * A utility class representing the result of a SQL query execution. This class is designed to encapsulate
 * both the ResultSet and the Statement, allowing for easier management of resources, especially when closing
 * the ResultSet and the Statement.
 * <p>
 * Implements {@link AutoCloseable} in order to be used with try-with-resources.
 */
public class QueryResult implements AutoCloseable
{
    private final ResultSet resultSet;
    private final Statement statement;

    /**
     * Constructs a new QueryResult object with the specified ResultSet and Statement.
     *
     * @param resultSet the ResultSet resulting from the execution of a SQL query
     * @param statement the Statement used to execute the SQL query
     */
    public QueryResult(ResultSet resultSet, Statement statement)
    {
        this.resultSet = resultSet;
        this.statement = statement;
    }

    /**
     * Closes both the ResultSet and the Statement associated with this QueryResult object.
     * Any SQLExceptions thrown during the closing process are caught and handled within this method.
     */
    public void close()
    {
        try
        {
            if (resultSet != null)
            {
                resultSet.close();
            }
            if (statement != null)
            {
                statement.close();
            }
        }
        catch (SQLException e)
        { //TODO-exceptions handle
            // Handle the exception or throw it, depending on your needs
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }

    /**
     * Returns the ResultSet associated with this QueryResult object.
     *
     * @return the ResultSet resulting from the execution of a SQL query
     */
    public ResultSet getResultSet()
    {
        return resultSet;
    }

    /**
     * Returns the Statement associated with this QueryResult object.
     *
     * @return the Statement used to execute the SQL query
     */
    public Statement getStatement()
    {
        return statement;
    }
}